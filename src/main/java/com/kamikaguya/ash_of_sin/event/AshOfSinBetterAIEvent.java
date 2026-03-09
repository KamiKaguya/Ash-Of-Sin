package com.kamikaguya.ash_of_sin.event;

import com.kamikaguya.ash_of_sin.config.BetterAIConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinBetterAIEvent {

    private static final Map<UUID, Map<UUID, Float>> HATE_MAP = new ConcurrentHashMap<>();
    private static final Map<UUID, List<UUID>> ACTIVE_ATTACKERS = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> LAST_ROTATION_TIME = new HashMap<>();

    private static final int BATTLE_LIMIT = BetterAIConfig.BATTLE_LIMIT.get();
    private static final double HATE_DECAY_RATE = 0.99; // 每20刻衰减1%
    private static final int ROTATION_INTERVAL = 10 * 20;
    private static final double TRACKING_RANGE = 32.0; // 玩家附近范围
    private static final double IDEAL_DISTANCE_SQ = 25.0;
    private static final double DISTANCE_TOLERANCE = 1.0;

    /**
     * 增加仇恨值
     */
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (!BetterAIConfig.BETTER_AI_ON.get()) return;
        if (event.getEntity().level().isClientSide()) return;

        Entity source = event.getSource().getEntity();
        if (!(source instanceof ServerPlayer player)) return;

        if (player.isCreative() || player.isSpectator()) return;

        LivingEntity target = event.getEntity();
        if (!(target instanceof Mob)) return;

        UUID mobId = target.getUUID();
        UUID playerId = player.getUUID();

        float damage = event.getAmount();
        HATE_MAP.computeIfAbsent(mobId, k -> new ConcurrentHashMap<>())
                .merge(playerId, damage, Float::sum);
    }

    /**
     * 清理数据并补充攻击者
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;

        UUID deadMobId = mob.getUUID();

        for (List<UUID> attackers : ACTIVE_ATTACKERS.values()) {
            attackers.remove(deadMobId);
        }

        HATE_MAP.remove(deadMobId);
    }

    /**
     * 处理衰减、目标更新、轮换和名额补充
     */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!BetterAIConfig.BETTER_AI_ON.get()) return;

        MinecraftServer server = event.getServer();
        if (server == null) return;

        long gameTime = server.overworld().getGameTime();

        if (gameTime % 20 == 0) {
            decayHate();
        }

        cleanupInvalidEntities(server);

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return;

        List<ServerPlayer> survivalPlayers = players.stream()
                .filter(p -> !p.isCreative() && !p.isSpectator())
                .collect(Collectors.toList());

        for (ServerPlayer player : survivalPlayers) {
            UUID playerId = player.getUUID();
            List<UUID> candidateMobs = getCandidateMobsForPlayer(player);
            List<UUID> activeList = ACTIVE_ATTACKERS.getOrDefault(playerId, new ArrayList<>());

            activeList.removeIf(mobId -> !candidateMobs.contains(mobId));

            long lastRot = LAST_ROTATION_TIME.getOrDefault(playerId, 0L);
            if (gameTime - lastRot >= ROTATION_INTERVAL) {
                Collections.shuffle(candidateMobs);
                List<UUID> newActive = candidateMobs.stream()
                        .limit(BATTLE_LIMIT)
                        .collect(Collectors.toList());
                ACTIVE_ATTACKERS.put(playerId, newActive);
                LAST_ROTATION_TIME.put(playerId, gameTime);
            } else {
                while (activeList.size() < BATTLE_LIMIT && !candidateMobs.isEmpty()) {
                    List<UUID> available = candidateMobs.stream()
                            .filter(id -> !activeList.contains(id))
                            .collect(Collectors.toList());
                    if (available.isEmpty()) break;
                    Collections.shuffle(available);
                    activeList.add(available.get(0));
                }
                ACTIVE_ATTACKERS.put(playerId, activeList);
            }
        }

        updateMobTargets(server);

        handleInactiveMobs(server, survivalPlayers);
    }

    /**
     * 衰减所有仇恨值（乘以衰减率）
     */
    private static void decayHate() {
        for (Map<UUID, Float> playerHate : HATE_MAP.values()) {
            for (Iterator<Map.Entry<UUID, Float>> it = playerHate.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<UUID, Float> entry = it.next();
                float newValue = entry.getValue() * (float) HATE_DECAY_RATE;
                if (newValue < 1.0f) {
                    it.remove();
                } else {
                    entry.setValue(newValue);
                }
            }
        }
        HATE_MAP.values().removeIf(Map::isEmpty);
    }

    private static void cleanupInvalidEntities(MinecraftServer server) {
        HATE_MAP.keySet().removeIf(uuid -> getEntityByUUID(server, uuid) == null);

        Set<UUID> onlinePlayers = server.getPlayerList().getPlayers().stream()
                .map(Player::getUUID)
                .collect(Collectors.toSet());
        ACTIVE_ATTACKERS.keySet().removeIf(uuid -> !onlinePlayers.contains(uuid));
        LAST_ROTATION_TIME.keySet().removeIf(uuid -> !onlinePlayers.contains(uuid));

        for (Map<UUID, Float> playerHate : HATE_MAP.values()) {
            playerHate.keySet().removeIf(uuid -> !onlinePlayers.contains(uuid));
        }
    }

    private static List<UUID> getCandidateMobsForPlayer(ServerPlayer player) {
        List<UUID> candidates = new ArrayList<>();
        for (Map.Entry<UUID, Map<UUID, Float>> entry : HATE_MAP.entrySet()) {
            UUID mobId = entry.getKey();
            Map<UUID, Float> hateForMob = entry.getValue();
            if (!hateForMob.containsKey(player.getUUID())) continue;

            Entity mob = getEntityByUUID(player.server, mobId);
            if (!(mob instanceof Mob livingMob)) continue;

            double dist = livingMob.distanceToSqr(player);
            if (dist <= TRACKING_RANGE * TRACKING_RANGE) {
                candidates.add(mobId);
            }
        }

        for (ServerLevel level : player.server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (!(entity instanceof Mob mob)) continue;
                if (candidates.contains(mob.getUUID())) continue; // 已在列表中
                if (!isNaturallyHostileTo(mob, player)) continue;
                if (mob.distanceToSqr(player) > TRACKING_RANGE * TRACKING_RANGE) continue;

                UUID mobId = mob.getUUID();
                HATE_MAP.computeIfAbsent(mobId, k -> new ConcurrentHashMap<>())
                        .putIfAbsent(player.getUUID(), 10.0f);
                candidates.add(mobId);
            }
        }

        return candidates;
    }

    private static boolean isNaturallyHostileTo(Mob mob, ServerPlayer player) {
        if (mob.getType().getCategory() != MobCategory.MONSTER) return false;
        return true;
    }

    private static Entity getEntityByUUID(MinecraftServer server, UUID uuid) {
        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(uuid);
            if (entity != null) return entity;
        }
        return null;
    }

    private static void updateMobTargets(MinecraftServer server) {
        for (Map.Entry<UUID, Map<UUID, Float>> mobEntry : HATE_MAP.entrySet()) {
            UUID mobId = mobEntry.getKey();
            Entity mobEntity = getEntityByUUID(server, mobId);
            if (!(mobEntity instanceof Mob mob)) continue;

            Map<UUID, Float> hateToPlayers = mobEntry.getValue();

            UUID bestTargetId = null;
            float maxHate = 0;

            for (Map.Entry<UUID, Float> playerHate : hateToPlayers.entrySet()) {
                UUID playerId = playerHate.getKey();
                float hate = playerHate.getValue();

                List<UUID> activeForPlayer = ACTIVE_ATTACKERS.get(playerId);
                if (activeForPlayer != null && activeForPlayer.contains(mobId)) {
                    if (hate > maxHate) {
                        maxHate = hate;
                        bestTargetId = playerId;
                    }
                }
            }

            if (bestTargetId != null) {
                Entity targetEntity = getEntityByUUID(server, bestTargetId);
                if (targetEntity instanceof LivingEntity livingTarget) {
                    mob.setTarget(livingTarget);
                } else {
                    mob.setTarget(null);
                }
            } else {
                if (mob.getTarget() instanceof Player) {
                    mob.setTarget(null);
                }
            }
        }
    }

    private static void handleInactiveMobs(MinecraftServer server, List<ServerPlayer> survivalPlayers) {
        if (survivalPlayers.isEmpty()) return;

        boolean exclusionEnabled = BetterAIConfig.EXCLUSION_ENABLED.get();
        List<String> exclusionList = exclusionEnabled ? BetterAIConfig.EXCLUSION_LIST.get().stream().map(s -> (String) s).toList() : Collections.emptyList();

        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (!(entity instanceof Mob mob)) continue;

                boolean isActive = false;
                for (ServerPlayer player : survivalPlayers) {
                    List<UUID> activeList = ACTIVE_ATTACKERS.get(player.getUUID());
                    if (activeList != null && activeList.contains(mob.getUUID())) {
                        isActive = true;
                        break;
                    }
                }
                if (isActive) continue;

                if (exclusionEnabled) {
                    ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
                    if (id != null && exclusionList.contains(id.toString())) {
                        continue;
                    }
                }

                ServerPlayer nearestPlayer = null;
                double nearestDistSq = Double.MAX_VALUE;
                for (ServerPlayer player : survivalPlayers) {
                    double distSq = mob.distanceToSqr(player);
                    if (distSq < nearestDistSq && distSq <= TRACKING_RANGE * TRACKING_RANGE) {
                        nearestDistSq = distSq;
                        nearestPlayer = player;
                    }
                }
                if (nearestPlayer == null) continue;

                if (nearestDistSq > IDEAL_DISTANCE_SQ + DISTANCE_TOLERANCE) {
                    continue;
                }

                Vec3 toPlayer = nearestPlayer.position().subtract(mob.position()).normalize();
                RandomSource rand = mob.getRandom();

                Vec3 targetSpeed = Vec3.ZERO;

                if (nearestDistSq < IDEAL_DISTANCE_SQ - DISTANCE_TOLERANCE) {
                    Vec3 awayDir = toPlayer.scale(-1); // 远离方向
                    if (canMoveInDirection(mob, awayDir, 1.0)) {
                        targetSpeed = awayDir.scale(0.2);
                    }
                }

                if (Math.abs(nearestDistSq - IDEAL_DISTANCE_SQ) <= DISTANCE_TOLERANCE) {
                    double sideways = (rand.nextDouble() - 0.5) * 0.2;
                    Vec3 right = toPlayer.cross(new Vec3(0, 1, 0)).normalize();
                    Vec3 lateral = right.scale(sideways);
                    targetSpeed = targetSpeed.add(lateral);
                }

                targetSpeed = new Vec3(targetSpeed.x, 0, targetSpeed.z);

                mob.setDeltaMovement(targetSpeed);
            }
        }
    }

    private static boolean canMoveInDirection(Mob mob, Vec3 dir, double distance) {
        AABB bb = mob.getBoundingBox().move(dir.scale(distance));
        return mob.level().noCollision(mob, bb);
    }
}