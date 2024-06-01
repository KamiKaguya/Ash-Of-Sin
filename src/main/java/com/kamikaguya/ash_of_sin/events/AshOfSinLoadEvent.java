package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.AshOfSinEntities;
import com.kamikaguya.ash_of_sin.world.entity.Gate;
import com.kamikaguya.ash_of_sin.config.AshOfSinConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.Level;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinLoadEvent {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();

        ResourceKey<Level> ABSOLUTE_SPACE_TIME_REALM = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(AshOfSin.MODID, "absolute_space_time_realm"));
        ServerLevel targetDimensionLevel = server.getLevel(ABSOLUTE_SPACE_TIME_REALM);

        if (targetDimensionLevel != null) {
            createGateInRealm(targetDimensionLevel, AshOfSinConfig.getGateLocation());
            int chunkRadius = AshOfSinConfig.CHUNK_LOAD_RADIUS.get();
            loadChunksAt(targetDimensionLevel, 0, 0, chunkRadius);
        }
    }

    private static void createGateInRealm(ServerLevel world, BlockPos pos) {
        if (!world.isClientSide) {
            Gate gate = new Gate(AshOfSinEntities.GATE.get(), world);
            gate.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            world.addFreshEntity(gate);
        }
    }

    private static void loadChunksAt(ServerLevel world, int centerX, int centerZ, int radius) {
        for (int dx = -radius; dx <= radius; ++dx) {
            for (int dz = -radius; dz <= radius; ++dz) {
                world.getChunk(centerX + dx, centerZ + dz, ChunkStatus.FULL, true);
            }
        }
    }
}

