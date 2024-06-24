package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.config.EternalEntityConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinEternalEntityEvent {
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        LivingEntity eternalEntity = (LivingEntity) event.getEntity();
        if (EternalEntityConfig.isEternalEntity(eternalEntity)) {
            ServerLevel serverLevel = (ServerLevel) eternalEntity.level;
            ChunkPos centerChunkPos = new ChunkPos(eternalEntity.blockPosition());
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    serverLevel.setChunkForced(centerChunkPos.x + dx, centerChunkPos.z + dz, true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide() && event.getWorld() instanceof ServerLevel) {
            if (event.getEntity() instanceof LivingEntity eternalEntity && EternalEntityConfig.isEternalEntity(eternalEntity)) {
                preventDespawn(eternalEntity);
            }
        }
    }

    public static void preventDespawn(Entity eternalEntity) {
        if (eternalEntity instanceof Mob){
            ((Mob) eternalEntity).setPersistenceRequired();
        }
        if (eternalEntity instanceof LivingEntity) {
            eternalEntity.getPersistentData().putBoolean("PersistenceRequired", true);
        }
    }
}