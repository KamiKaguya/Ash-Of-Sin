package com.kamikaguya.ash_of_sin.events.special;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinGoblinEvent {
    public static final Map<UUID, Long> lastSpawnTimes = new ConcurrentHashMap<>();
    public static final Map<UUID, Set<UUID>> markedGoblins = new ConcurrentHashMap<>();
    public static final long SPAWN_COOLDOWN = 20 * 20;
    public static final ResourceLocation goblinkingID = new ResourceLocation("goblinsanddungeons:goblin_king");
    public static final ResourceLocation[] GOBLIN_TYPES = {
            new ResourceLocation("goblinsanddungeons:gob"),
            new ResourceLocation("goblinsanddungeons:hobgob"),
            new ResourceLocation("goblinsanddungeons:goblo"),
            new ResourceLocation("goblinsanddungeons:garch"),
            new ResourceLocation("goblinsanddungeons:goom"),
            new ResourceLocation("goblinsanddungeons:mimic"),
            new ResourceLocation("goblinsanddungeons:gobber")
    };

    @SubscribeEvent
    public static void createGoblinSlayer(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving().level instanceof ServerLevel serverWorld)) {
            return;
        }
        LivingEntity goblinKingEntity = event.getEntityLiving();

        if (goblinKingEntity.getType().getRegistryName() == goblinkingID) {

            for (ServerPlayer player : serverWorld.players()) {
                List<Entity> goblinKings = serverWorld.getEntitiesOfClass(Entity.class,
                        player.getBoundingBox().inflate(32),
                        e -> e.getType().equals(goblinkingID));

                goblinKings.forEach(goblinKing -> {
                    long lastSpawnTime = lastSpawnTimes.getOrDefault(goblinKing.getUUID(), Long.MIN_VALUE);
                    boolean shouldSpawn = serverWorld.getGameTime() - lastSpawnTime > SPAWN_COOLDOWN;
                    if (shouldSpawn && canSpawnGoblins(serverWorld, player)) {
                            spawnGoblins(serverWorld, player, goblinKing);
                            lastSpawnTimes.put(goblinKing.getUUID(), serverWorld.getGameTime());
                    }
                });
            }
        }
    }

    public static boolean canSpawnGoblins(ServerLevel world, ServerPlayer player) {
        AABB playerCheckBox = player.getBoundingBox().inflate(32, 16, 32);
        List<Entity> goblinKings = world.getEntitiesOfClass(Entity.class, playerCheckBox, e -> e.getType().equals(goblinkingID));

        for (Entity goblinKing : goblinKings) {
            long lastSpawnTime = lastSpawnTimes.getOrDefault(goblinKing.getUUID(), Long.MIN_VALUE);
            boolean shouldSpawn = world.getGameTime() - lastSpawnTime > SPAWN_COOLDOWN;
            if (shouldSpawn) {
                return true;
            }
        }
        return false;
    }

    public static void spawnGoblins(ServerLevel serverWorld, ServerPlayer player, Entity goblinKing) {
        Set<UUID> spawnedGoblins = markedGoblins.computeIfAbsent(player.getUUID(), k -> new HashSet<>());

        for (ResourceLocation goblinTypeLocation : GOBLIN_TYPES) {
            EntityType<?> goblinType = ForgeRegistries.ENTITIES.getValue(goblinTypeLocation);
            if (goblinType == null) continue;

            for (int i = 0; i < 10; i++) {
                Entity goblin = goblinType.create(serverWorld);
                if (goblin == null) continue;

                double xOffset = 5 + serverWorld.random.nextInt(5) * (serverWorld.random.nextBoolean() ? 1 : -1);
                double zOffset = 5 + serverWorld.random.nextInt(5) * (serverWorld.random.nextBoolean() ? 1 : -1);
                double yOffset = serverWorld.random.nextInt(11) - 5;

                double x = goblinKing.getX() + xOffset;
                double y = Math.max(1, goblinKing.getY() + yOffset);
                double z = goblinKing.getZ() + zOffset;

                goblin.moveTo(x, y, z, serverWorld.random.nextFloat() * 360.0F, 0.0F);
                goblin.addTag("MarkedGoblin");
                serverWorld.addFreshEntity(goblin);
                spawnedGoblins.add(goblin.getUUID());
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;

        if (entity.level.isClientSide() || !(entity.level instanceof ServerLevel)) {
            return;
        }

        if (!entity.getTags().contains("MarkedGoblin")) {
            return;
        }

        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            UUID playerUUID = player.getUUID();
            Set<UUID> markedSet = markedGoblins.get(playerUUID);
            if (markedSet != null) {
                markedSet.remove(entity.getUUID());
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        for (ServerLevel serverWorld : ServerLifecycleHooks.getCurrentServer().getAllLevels()) {
            for (ServerPlayer player : serverWorld.players()) {
                if (canSpawnGoblins(serverWorld, player)) {
                    cleanUpGoblinTags(serverWorld, player);
                }
            }
        }
    }

    public static void cleanUpGoblinTags(ServerLevel serverWorld, ServerPlayer player) {
        Set<UUID> goblinIDs = markedGoblins.getOrDefault(player.getUUID(), Collections.emptySet());
        goblinIDs.removeIf(goblinID -> {
            Entity goblinEntity = serverWorld.getEntity(goblinID);
            if (goblinEntity != null && goblinEntity.distanceToSqr(player) > 64 * 64) {
                goblinEntity.discard();
                return true;
            }
            return false;
        });
        if (goblinIDs.isEmpty()) {
            markedGoblins.remove(player.getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            cleanupOnPlayerLeave((ServerPlayer) event.getPlayer());
        }
    }

    public static void cleanupOnPlayerLeave(ServerPlayer player) {
        markedGoblins.remove(player.getUUID());
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        markedGoblins.clear();
        lastSpawnTimes.clear();
    }
}