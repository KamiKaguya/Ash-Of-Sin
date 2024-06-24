package com.kamikaguya.ash_of_sin.events.special;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.effect.AshOfSinEffects;
import com.kamikaguya.ash_of_sin.world.entity.AshOfSinEntities;
import com.kamikaguya.ash_of_sin.world.entity.Assassin;
import com.kamikaguya.ash_of_sin.world.entity.KamiKaguya;
import com.kamikaguya.ash_of_sin.world.entity.Doppelganger;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

public class AshOfSinEntityEvent {
    public static final long DAY_LENGTH_IN_TICKS = 24000L;
    public static final long DAYS_BEFORE_SPAWN_AGAIN = 13L;
    public static final long ASSASSIN_SPAWN_INTERVAL = DAY_LENGTH_IN_TICKS * DAYS_BEFORE_SPAWN_AGAIN;
    public static final Map<UUID, Long> lastAssassinSpawnTimes = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayer playerEntity = (ServerPlayer) event.getPlayer();
        if (playerEntity.getPersistentData().getBoolean("UsedNetherPortal") && event.getTo() == Level.OVERWORLD) {
            playerEntity.getPersistentData().putBoolean("PassedThroughPortal", true);
        }

        playerEntity.getPersistentData().remove("UsedNetherPortal");
    }

    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer playerEntity)) return;

        ResourceKey<Level> targetDimension = event.getDimension();
        MobEffectInstance wrathOfGodEffectInstance = playerEntity.getEffect(AshOfSinEffects.WRATH_OF_GOD.get());

        if (wrathOfGodEffectInstance != null && !targetDimension.equals(Level.OVERWORLD)) {
            event.setCanceled(true);
            teleportPlayerToNetherSafeLocation(playerEntity);
        }
    }

    public static void teleportPlayerToNetherSafeLocation(ServerPlayer player) {
        ServerLevel nether = Objects.requireNonNull(player.getServer()).getLevel(Level.NETHER);
        if (nether != null) {
            BlockPos safeLocation = findSafeLocationInNether(nether);

            if (safeLocation != null) {
                player.teleportTo(nether, safeLocation.getX() + 0.5, safeLocation.getY(), safeLocation.getZ() + 0.5, player.getYRot(), player.getXRot());
            } else {
                BlockPos defaultSpawn = nether.getSharedSpawnPos();
                player.teleportTo(nether, defaultSpawn.getX() + 0.5, defaultSpawn.getY(), defaultSpawn.getZ() + 0.5, player.getYRot(), player.getXRot());
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayer playerEntity = (ServerPlayer) event.getPlayer();
        Vec3 playerPosition = playerEntity.position();
        CompoundTag data = playerEntity.getPersistentData();
        UUID playerUUID = playerEntity.getUUID();
        Doppelganger doppelganger = KamiKaguya.getStoredDoppelganger(playerUUID);

        MobEffectInstance wrathOfGodEffectInstance = playerEntity.getEffect(AshOfSinEffects.WRATH_OF_GOD.get());
        if (data.getBoolean("TeleportToNetherOnRespawn")) {
            data.remove("TeleportToNetherOnRespawn");

            ServerLevel nether = Objects.requireNonNull(playerEntity.getServer()).getLevel(Level.NETHER);
            if (wrathOfGodEffectInstance != null && nether != null) {
                teleportPlayerToNetherSafeLocation(playerEntity);
            }
        }

        if (doppelganger != null && doppelganger.isAlive()) {
            Vec3 doppelgangerPosition = doppelganger.position();
            double distance = doppelgangerPosition.distanceTo(playerPosition);
            final double MAX_DISTANCE = 32.0;

            if (distance > MAX_DISTANCE) {
                BlockPos safeLocation = findSafeLocationAround(playerEntity);
                doppelganger.teleportTo(safeLocation.getX() + 0.5, safeLocation.getY(), safeLocation.getZ() + 0.5);
            }
        }
    }

    public static final int TOTAL_MAX_ATTEMPTS = 3939;

    public static BlockPos findSafeLocationInNether(ServerLevel nether) {
        BlockPos respawnPoint = nether.getSharedSpawnPos();
        Random rand = new Random();
        int minRadius = 500;
        int maxRadius = 3939;
        int maxAttempts = 39;
        int totalAttempts = 0;

        BlockPos safeLocation = null;
        while (totalAttempts < TOTAL_MAX_ATTEMPTS && safeLocation == null) {
            int attempts = 0;
            while (attempts < maxAttempts) {
                double angle = rand.nextDouble() * Math.PI * 2;
                int radius = minRadius + rand.nextInt(maxRadius - minRadius);
                int x = respawnPoint.getX() + (int)(radius * Math.cos(angle));
                int z = respawnPoint.getZ() + (int)(radius * Math.sin(angle));
                int y = nether.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

                if (isSafeLocation(nether, x, y, z)) {
                    safeLocation = new BlockPos(x, y, z);
                    break;
                }
                attempts++;
                totalAttempts++;
            }
        }
        return safeLocation;
    }

    public static boolean isSafeLocation(ServerLevel level, int x, int y, int z) {
        if (y < 32 || y > 180) {
            return false;
        }
        BlockPos groundPos = new BlockPos(x, y - 1, z);
        BlockPos playerPos = new BlockPos(x, y, z);
        BlockPos abovePlayerPos = playerPos.above();

        boolean isGroundSafe = level.getBlockState(groundPos).getMaterial().isSolid();
        boolean isPlayerSpaceSafe = level.getBlockState(playerPos).isAir() && level.getBlockState(abovePlayerPos).isAir();

        boolean isSolidGround = level.getBlockState(groundPos.below()).getMaterial().isSolid();

        boolean isSafeHeadroom = level.getBlockState(abovePlayerPos.above()).isAir();

        boolean isSafeGround = !level.getBlockState(groundPos).is(BlockTags.FIRE) &&
                level.getFluidState(groundPos).isEmpty();

        return isGroundSafe && isPlayerSpaceSafe && isSolidGround && isSafeHeadroom && isSafeGround;
    }

    public static BlockPos findSafeLocationAround(ServerPlayer player) {
        BlockPos playerPos = player.blockPosition();
        ServerLevel world = player.getLevel();
        int radius = 8;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos pos = playerPos.offset(dx, 0, dz);
                int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
                BlockPos groundPos = new BlockPos(pos.getX(), y - 1, pos.getZ());

                if (isSafeLocation(world, pos.getX(), y, pos.getZ())) {
                    return groundPos;
                }
            }
        }
        return playerPos;
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer playerEntity) {
            MobEffectInstance wrathOfGodEffectInstance = playerEntity.getEffect(AshOfSinEffects.WRATH_OF_GOD.get());

            if (wrathOfGodEffectInstance != null) {
                CompoundTag data = playerEntity.getPersistentData();
                data.putBoolean("TeleportToNetherOnRespawn", true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        ServerPlayer originalPlayer = (ServerPlayer) event.getOriginal();
        ServerPlayer newPlayer = (ServerPlayer) event.getPlayer();
        CompoundTag originalData = originalPlayer.getPersistentData();

        if (originalData.getBoolean("TeleportToNetherOnRespawn")) {
            CompoundTag newData = newPlayer.getPersistentData();
            newData.putBoolean("TeleportToNetherOnRespawn", true);
        }
        MobEffectInstance wrathOfGodEffectInstance = originalPlayer.getEffect(AshOfSinEffects.WRATH_OF_GOD.get());
        if (wrathOfGodEffectInstance != null) {
            newPlayer.addEffect(new MobEffectInstance(wrathOfGodEffectInstance));

            if (!newPlayer.level.dimension().equals(Level.NETHER)) {
                Objects.requireNonNull(newPlayer.getServer()).submit(() -> {
                    teleportPlayerToNetherSafeLocation(newPlayer);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START && event.player.isAlive() && event.player instanceof ServerPlayer) {

            ServerPlayer player = (ServerPlayer) event.player;
            ServerLevel world = (ServerLevel) player.level;

            if (player.gameMode.getGameModeForPlayer() == GameType.CREATIVE) {
                return;
            }

            boolean isNightTime = world.isNight();
            boolean isLowLight = world.getMaxLocalRawBrightness(player.blockPosition()) < 10;

            long lastSpawnTime = lastAssassinSpawnTimes.getOrDefault(player.getUUID(), Long.MIN_VALUE);
            boolean enoughTimePassed = world.getGameTime() - lastSpawnTime >= ASSASSIN_SPAWN_INTERVAL;

            if (isNightTime && isLowLight && enoughTimePassed) {
                spawnAssassinNearPlayer(world, player);
                lastAssassinSpawnTimes.put(player.getUUID(), world.getGameTime());
            }
        }

        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            ServerPlayer player = (ServerPlayer) event.player;

            if (player.isOnPortalCooldown()) {
                player.getPersistentData().putBoolean("UsedNetherPortal", true);
            } else {
                player.getPersistentData().remove("UsedNetherPortal");
            }
        }
    }

    public static void spawnAssassinNearPlayer(ServerLevel world, ServerPlayer player) {
        EntityType<Assassin> assassinType = AshOfSinEntities.ASSASSIN.get();
        Assassin assassin = assassinType.create(world);

        if (assassin != null) {
            BlockPos spawnPos = findSpawnPositionNearPlayer(world, player);
            if (spawnPos != null) {
                assassin.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                world.addFreshEntity(assassin);
            }
        }
    }

    public static BlockPos findSpawnPositionNearPlayer(ServerLevel world, ServerPlayer player) {
        Random random = player.getRandom();
        for (int i = 0; i < 10; i++) {
            int x = (int)Math.floor(player.getX() - 13 + random.nextInt(42));
            int y = (int)Math.floor(player.getY());
            int z = (int)Math.floor(player.getZ() - 13 + random.nextInt(42));

            int groundY = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, groundY, z);

            if (world.getBlockState(pos).getMaterial().isSolid() && world.getBlockState(pos.above()).isAir() && canSpawnHere(world, pos.above())) {
                return pos.above();
            }
        }
        return null;
    }

    public static boolean canSpawnHere(LevelAccessor world, BlockPos pos) {
        BlockState state = world.getBlockState(pos.below());
        return state.getMaterial().isSolid() && world.isEmptyBlock(pos) && world.isEmptyBlock(pos.above());
    }
}