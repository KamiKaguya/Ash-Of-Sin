package com.kamikaguya.ash_of_sin.event;

import com.kamikaguya.ash_of_sin.config.BetterAIConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinBetterAIEvent {

    private static final Map<UUID, Map<UUID, Float>> HATE_MAP = new ConcurrentHashMap<>();
    private static final Map<UUID, Map<UUID, Long>> LAST_ATTACK_TIME = new ConcurrentHashMap<>();
    private static final Map<UUID, List<UUID>> ACTIVE_ATTACKERS = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> LAST_ROTATION_TIME = new HashMap<>();

    private static final double HATE_DECAY_RATE = 0.90; // 每20刻衰减10%
    private static final int ROTATION_INTERVAL = 10 * 20;
    private static final double IDEAL_DISTANCE_SQ = 25.0;
    private static final double DISTANCE_TOLERANCE = 1.0;

    private static final Set<EntityType<?>> NEUTRAL_MONSTERS = loadNeutralMonsters();

    private static Set<EntityType<?>> loadNeutralMonsters() {
        List<? extends String> ids = BetterAIConfig.NEUTRAL_MONSTER_LIST.get();
        Set<EntityType<?>> set = new HashSet<>();
        for (String id : ids) {
            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(id));
            if (type != null) {
                set.add(type);
            } else {
                System.err.println("[Ash Of Sin-Better AI] Unknown entity type in neutral monster list: " + id);
            }
        }
        return set;
    }

    private static final Set<EntityType<?>> FRIENDLY_MOBS = loadFriendlyMobs();

    private static Set<EntityType<?>> loadFriendlyMobs() {
        List<? extends String> ids = BetterAIConfig.FRIENDLY_LIST.get();
        Set<EntityType<?>> set = new HashSet<>();
        for (String id : ids) {
            EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(id));
            if (type != null) {
                set.add(type);
            } else {
                System.err.println("[Ash Of Sin-Better AI] Unknown entity type in friendly list: " + id);
            }
        }
        return set;
    }

    private static boolean isExcluded(Entity entity) {
        if (!BetterAIConfig.EXCLUSION_ENABLED.get()) return false;
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return BetterAIConfig.EXCLUSION_LIST.get().contains(id.toString());
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (!BetterAIConfig.BETTER_AI_ON.get()) return;
        if (event.getEntity().level().isClientSide()) return;

        Entity source = event.getSource().getEntity();
        if (!(source instanceof ServerPlayer player)) return;

        if (player.isCreative() || player.isSpectator()) return;

        LivingEntity target = event.getEntity();
        if (FRIENDLY_MOBS.contains(target.getType())) return;
        if (!(target instanceof Mob)) return;

        UUID mobId = target.getUUID();
        UUID playerId = player.getUUID();

        float damage = event.getAmount();
        HATE_MAP.computeIfAbsent(mobId, k -> new ConcurrentHashMap<>())
                .merge(playerId, damage, Float::sum);

        long gameTime = player.server.overworld().getGameTime();
        LAST_ATTACK_TIME.computeIfAbsent(mobId, k -> new ConcurrentHashMap<>())
                .put(playerId, gameTime);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (event.getEntity() instanceof Mob mob) {
            UUID deadMobId = mob.getUUID();
            for (List<UUID> attackers : ACTIVE_ATTACKERS.values()) {
                attackers.remove(deadMobId);
            }
            HATE_MAP.remove(deadMobId);
            LAST_ATTACK_TIME.remove(deadMobId);
        } else if (event.getEntity() instanceof ServerPlayer player) {
            UUID deadPlayerId = player.getUUID();
            MinecraftServer server = player.server;

            List<UUID> mobsToClearTarget = new ArrayList<>();
            Iterator<Map.Entry<UUID, Map<UUID, Float>>> mobIt = HATE_MAP.entrySet().iterator();
            while (mobIt.hasNext()) {
                Map.Entry<UUID, Map<UUID, Float>> mobEntry = mobIt.next();
                UUID mobId = mobEntry.getKey();
                Map<UUID, Float> playerMap = mobEntry.getValue();
                playerMap.remove(deadPlayerId);
                if (playerMap.isEmpty()) {
                    mobIt.remove();
                    LAST_ATTACK_TIME.remove(mobId);
                    mobsToClearTarget.add(mobId);
                }
            }
            LAST_ATTACK_TIME.values().forEach(map -> map.remove(deadPlayerId));
            LAST_ATTACK_TIME.values().removeIf(Map::isEmpty);

            for (UUID mobId : mobsToClearTarget) {
                clearTargetIfNeeded(mobId, server);
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!BetterAIConfig.BETTER_AI_ON.get()) return;

        MinecraftServer server = event.getServer();
        if (server == null) return;

        long gameTime = server.overworld().getGameTime();

        if (gameTime % 20 == 0) {
            decayHate(server, gameTime);
        }

        cleanupInvalidEntities(server);

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return;

        List<ServerPlayer> normalPlayers = players.stream()
                .filter(p -> !p.isCreative() && !p.isSpectator())
                .collect(Collectors.toList());

        for (ServerPlayer player : normalPlayers) {
            UUID playerId = player.getUUID();
            List<UUID> allCandidates = getCandidateMobsForPlayer(player);

            List<UUID> excludedCandidates = allCandidates.stream()
                    .filter(id -> {
                        Entity e = getEntityByUUID(server, id);
                        return e != null && isExcluded(e);
                    })
                    .collect(Collectors.toList());
            List<UUID> normalCandidates = allCandidates.stream()
                    .filter(id -> {
                        Entity e = getEntityByUUID(server, id);
                        return e != null && !isExcluded(e);
                    })
                    .collect(Collectors.toList());

            List<UUID> activeList = ACTIVE_ATTACKERS.getOrDefault(playerId, new ArrayList<>());

            activeList.removeIf(mobId -> !allCandidates.contains(mobId));

            for (UUID excludedId : excludedCandidates) {
                if (!activeList.contains(excludedId)) {
                    activeList.add(excludedId);
                }
            }

            long lastRot = LAST_ROTATION_TIME.getOrDefault(playerId, 0L);
            int battleLimit = BetterAIConfig.BATTLE_LIMIT.get();
            if (gameTime - lastRot >= ROTATION_INTERVAL) {
                Collections.shuffle(normalCandidates);
                List<UUID> newNormalActive = normalCandidates.stream()
                        .limit(battleLimit)
                        .collect(Collectors.toList());

                List<UUID> newActive = new ArrayList<>();
                newActive.addAll(excludedCandidates);
                newActive.addAll(newNormalActive);
                ACTIVE_ATTACKERS.put(playerId, newActive);
                LAST_ROTATION_TIME.put(playerId, gameTime);
            } else {
                List<UUID> currentNormalActive = activeList.stream()
                        .filter(id -> !excludedCandidates.contains(id))
                        .collect(Collectors.toList());
                while (currentNormalActive.size() < battleLimit && !normalCandidates.isEmpty()) {
                    List<UUID> available = normalCandidates.stream()
                            .filter(id -> !currentNormalActive.contains(id))
                            .collect(Collectors.toList());
                    if (available.isEmpty()) break;
                    Collections.shuffle(available);
                    currentNormalActive.add(available.get(0));
                }

                List<UUID> newActive = new ArrayList<>();
                newActive.addAll(excludedCandidates);
                newActive.addAll(currentNormalActive);
                ACTIVE_ATTACKERS.put(playerId, newActive);
            }
        }

        updateMobTargets(server);
        handleInactiveMobs(server, normalPlayers);

        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (entity instanceof Mob mob && FRIENDLY_MOBS.contains(mob.getType())) {
                    if (mob.getTarget() instanceof Player) {
                        mob.setTarget(null);
                    }
                }
            }
        }

//        for (ServerPlayer player : normalPlayers) {
//            AABB range = new AABB(player.blockPosition()).inflate(BetterAIConfig.TRACKING_RANGE.get());
//            for (Mob mob : player.serverLevel().getEntitiesOfClass(Mob.class, range,
//                    mob -> FRIENDLY_MOBS.contains(mob.getType()) && mob.getTarget() == player)) {
//                mob.setTarget(null);
//            }
//        }
    }

    // ==================== 仇恨管理 ====================

    private static void decayHate(MinecraftServer server, long currentGameTime) {
        List<UUID> mobsToClearTarget = new ArrayList<>();

        Iterator<Map.Entry<UUID, Map<UUID, Float>>> mobIt = HATE_MAP.entrySet().iterator();
        while (mobIt.hasNext()) {
            Map.Entry<UUID, Map<UUID, Float>> mobEntry = mobIt.next();
            UUID mobId = mobEntry.getKey();
            Map<UUID, Float> playerHateMap = mobEntry.getValue();
            Map<UUID, Long> lastAttackMap = LAST_ATTACK_TIME.get(mobId);
            if (lastAttackMap == null) continue;

            Iterator<Map.Entry<UUID, Float>> hateIt = playerHateMap.entrySet().iterator();
            while (hateIt.hasNext()) {
                Map.Entry<UUID, Float> entry = hateIt.next();
                UUID playerId = entry.getKey();
                Long lastTime = lastAttackMap.get(playerId);

                if (lastTime == null || currentGameTime - lastTime > 10 * 20) {
                    hateIt.remove();
                    lastAttackMap.remove(playerId);
                    continue;
                }

                float newValue = entry.getValue() * (float) HATE_DECAY_RATE;
                if (newValue < 1.0f) {
                    hateIt.remove();
                    lastAttackMap.remove(playerId);
                } else {
                    entry.setValue(newValue);
                }
            }

            if (playerHateMap.isEmpty()) {
                mobIt.remove();
                LAST_ATTACK_TIME.remove(mobId);
                mobsToClearTarget.add(mobId);
            }
        }

        LAST_ATTACK_TIME.values().removeIf(Map::isEmpty);

        for (UUID mobId : mobsToClearTarget) {
            clearTargetIfNeeded(mobId, server);
        }
    }

    private static void clearTargetIfNeeded(UUID mobId, MinecraftServer server) {
        Entity entity = getEntityByUUID(server, mobId);
        if (entity instanceof Mob mob) {
            if (mob.getTarget() instanceof Player) {
                mob.setTarget(null);
            }
        }
    }

    private static void cleanupInvalidEntities(MinecraftServer server) {
        HATE_MAP.keySet().removeIf(uuid -> getEntityByUUID(server, uuid) == null);
        LAST_ATTACK_TIME.keySet().removeIf(uuid -> getEntityByUUID(server, uuid) == null);

        Set<UUID> onlinePlayers = server.getPlayerList().getPlayers().stream()
                .map(Player::getUUID)
                .collect(Collectors.toSet());

        ACTIVE_ATTACKERS.keySet().removeIf(uuid -> !onlinePlayers.contains(uuid));
        LAST_ROTATION_TIME.keySet().removeIf(uuid -> !onlinePlayers.contains(uuid));

        for (Map<UUID, Float> playerHate : HATE_MAP.values()) {
            playerHate.keySet().removeIf(uuid -> !onlinePlayers.contains(uuid));
        }
        for (Map<UUID, Long> lastAttack : LAST_ATTACK_TIME.values()) {
            lastAttack.keySet().removeIf(uuid -> !onlinePlayers.contains(uuid));
        }

        HATE_MAP.values().removeIf(Map::isEmpty);
        LAST_ATTACK_TIME.values().removeIf(Map::isEmpty);
    }

    // ==================== 候选列表相关 ====================

    private static List<UUID> getCandidateMobsForPlayer(ServerPlayer player) {
        List<UUID> candidates = new ArrayList<>();
        double trackingRange = BetterAIConfig.TRACKING_RANGE.get();
        for (Map.Entry<UUID, Map<UUID, Float>> entry : HATE_MAP.entrySet()) {
            UUID mobId = entry.getKey();
            Map<UUID, Float> hateForMob = entry.getValue();
            if (!hateForMob.containsKey(player.getUUID())) continue;

            Entity mob = getEntityByUUID(player.server, mobId);
            if (!(mob instanceof Mob livingMob)) continue;
            if (FRIENDLY_MOBS.contains(mob.getType())) continue;

            double dist = livingMob.distanceToSqr(player);
            if (dist <= trackingRange * trackingRange) {
                candidates.add(mobId);
            }
        }

        for (ServerLevel level : player.server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (!(entity instanceof Mob mob)) continue;
                if (candidates.contains(mob.getUUID())) continue;
                if (FRIENDLY_MOBS.contains(mob.getType())) continue;
                if (!isHostileTo(mob, player)) continue;
                if (mob.distanceToSqr(player) > trackingRange * trackingRange) continue;

                UUID mobId = mob.getUUID();
                HATE_MAP.computeIfAbsent(mobId, k -> new ConcurrentHashMap<>())
                        .putIfAbsent(player.getUUID(), 10.0f);
                LAST_ATTACK_TIME.computeIfAbsent(mobId, k -> new ConcurrentHashMap<>())
                        .putIfAbsent(player.getUUID(), player.server.overworld().getGameTime());
                candidates.add(mobId);
            }
        }

        return candidates;
    }

    private static boolean isHostileTo(Mob mob, ServerPlayer player) {
        EntityType<?> type = mob.getType();
        if (NEUTRAL_MONSTERS.contains(type) || type.getCategory() != MobCategory.MONSTER) {
            return mob.getTarget() == player;
        }
        return true;
    }

    // ==================== 目标更新 ====================

    private static void updateMobTargets(MinecraftServer server) {
        for (Map.Entry<UUID, Map<UUID, Float>> mobEntry : HATE_MAP.entrySet()) {
            UUID mobId = mobEntry.getKey();
            Entity mobEntity = getEntityByUUID(server, mobId);
            if (!(mobEntity instanceof Mob mob)) continue;

            if (FRIENDLY_MOBS.contains(mob.getType())) {
                if (mob.getTarget() instanceof Player) {
                    mob.setTarget(null);
                }
                continue;
            }

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

    // ==================== 非活跃生物行为控制 ====================

    private static void handleInactiveMobs(MinecraftServer server, List<ServerPlayer> normalPlayers) {
        if (normalPlayers.isEmpty()) return;

        boolean exclusionEnabled = BetterAIConfig.EXCLUSION_ENABLED.get();
        List<String> exclusionList = exclusionEnabled ? BetterAIConfig.EXCLUSION_LIST.get().stream().map(s -> (String) s).toList() : Collections.emptyList();

        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (!(entity instanceof Mob mob)) continue;

                if (FRIENDLY_MOBS.contains(mob.getType())) continue;

                if (mob.getType().getCategory() != MobCategory.MONSTER) continue;

                if (NEUTRAL_MONSTERS.contains(mob.getType())) {
                    boolean isHostileNow = false;
                    for (ServerPlayer player : normalPlayers) {
                        if (mob.getTarget() == player) {
                            isHostileNow = true;
                            break;
                        }
                    }
                    if (!isHostileNow) continue;
                }

                boolean isActive = false;
                for (ServerPlayer player : normalPlayers) {
                    List<UUID> activeList = ACTIVE_ATTACKERS.get(player.getUUID());
                    if (activeList != null && activeList.contains(mob.getUUID())) {
                        isActive = true;
                        break;
                    }
                }
                if (isActive) continue;

                // 排除列表检查
                if (exclusionEnabled) {
                    ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
                    if (exclusionList.contains(id.toString())) continue;
                }

                ServerPlayer nearestPlayer = null;
                double nearestDistSq = Double.MAX_VALUE;
                double trackingRange = BetterAIConfig.TRACKING_RANGE.get();
                for (ServerPlayer player : normalPlayers) {
                    double distSq = mob.distanceToSqr(player);
                    if (distSq < nearestDistSq && distSq <= trackingRange * trackingRange) {
                        nearestDistSq = distSq;
                        nearestPlayer = player;
                    }
                }
                if (nearestPlayer == null) continue;

                // 距离大于5格时不干预
                if (nearestDistSq > IDEAL_DISTANCE_SQ + DISTANCE_TOLERANCE) continue;

                Vec3 toPlayer = nearestPlayer.position().subtract(mob.position()).normalize();
                RandomSource rand = mob.getRandom();

                Vec3 targetSpeed = Vec3.ZERO;

                // 距离小于5格时尝试后退
                if (nearestDistSq < IDEAL_DISTANCE_SQ - DISTANCE_TOLERANCE) {
                    Vec3 awayDir = toPlayer.scale(-1);
                    if (canMoveInDirection(mob, awayDir, 1.0)) {
                        targetSpeed = awayDir.scale(0.2);
                    }
                }

                // 距离约为5格时添加左右移动
                if (Math.abs(nearestDistSq - IDEAL_DISTANCE_SQ) <= DISTANCE_TOLERANCE) {
                    double sideways = (rand.nextDouble() - 0.5) * 0.2;
                    Vec3 right = toPlayer.cross(new Vec3(0, 1, 0)).normalize();
                    Vec3 lateral = right.scale(sideways);
                    targetSpeed = targetSpeed.add(lateral);
                }

                targetSpeed = new Vec3(targetSpeed.x, mob.getDeltaMovement().y, targetSpeed.z);
                mob.setDeltaMovement(targetSpeed);
            }
        }
    }

    private static boolean canMoveInDirection(Mob mob, Vec3 dir, double distance) {
        AABB bb = mob.getBoundingBox().move(dir.scale(distance));
        return mob.level().noCollision(mob, bb);
    }

    private static Entity getEntityByUUID(MinecraftServer server, UUID uuid) {
        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(uuid);
            if (entity != null) return entity;
        }
        return null;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!FRIENDLY_MOBS.contains(mob.getType())) return;
        if (event.getNewTarget() instanceof Player) {
            mob.setTarget(null);
        }
    }
}