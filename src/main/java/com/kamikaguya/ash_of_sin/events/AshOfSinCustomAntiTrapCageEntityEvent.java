package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomAntiTrapCageEntityConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomAntiTrapCageEntityEvent {
    public static final int DISTANCE = CustomAntiTrapCageEntityConfig.CHECK_DISTANCE.get();

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (CustomAntiTrapCageEntityConfig.ANTI_ON.get()) {
            if (event.getEntityLiving().level.isClientSide()) {
                return;
            }
            if (!(event.getEntityLiving() instanceof ServerPlayer player)) {
                return;
            }

            Level world = player.level;
            double playerX = player.getX();
            double playerY = player.getY();
            double playerZ = player.getZ();

            List<String> customAntiTrapCageEntityList = CustomAntiTrapCageEntityConfig.ANTI_TRAP_CAGE_ENTITY.get().stream()
                    .map(s -> (String) s)
                    .toList();

            for (String entityName : customAntiTrapCageEntityList) {
                EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityName));
                if (entityType == null) {
                    continue;
                }

                List<LivingEntity> nearbyEntities = world.getEntitiesOfClass(LivingEntity.class, new AABB(
                        playerX - DISTANCE, playerY - 8, playerZ - DISTANCE,
                        playerX + DISTANCE, playerY + 8, playerZ + DISTANCE
                ));

                for (LivingEntity nearbyEntity : nearbyEntities) {
                    if (entityType.equals(nearbyEntity.getType())) {
                        breakTrapCageIfInDistance(world, nearbyEntity);
                    }
                }
            }
        }
    }

    public static void breakTrapCageIfInDistance(Level world, LivingEntity entity) {
        Block trapCageBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("untamedwilds:trap_cage"));
        if (trapCageBlock == null) {
            return;
        }

        AABB entityBoundingBox = new AABB(
                entity.getX() - DISTANCE, entity.getY() - 1, entity.getZ() - DISTANCE,
                entity.getX() + DISTANCE, entity.getY() + 1, entity.getZ() + DISTANCE
        );

        for (BlockPos blockPos : BlockPos.betweenClosed(
                new BlockPos(entityBoundingBox.minX, entityBoundingBox.minY, entityBoundingBox.minZ),
                new BlockPos(entityBoundingBox.maxX, entityBoundingBox.maxY, entityBoundingBox.maxZ))) {
            if (world.isEmptyBlock(blockPos)) {
                continue;
            }
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.is(trapCageBlock)) {
                world.destroyBlock(blockPos, true);
            }
        }
    }
}