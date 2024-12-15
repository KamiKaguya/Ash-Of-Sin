package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.EternalEntityConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinEternalEntityEvent {

    public static final String ETERNAL_ENTITY = EternalEntityConfig.ETERNAL_ENTITY.get().toString();

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        LivingEntity eternalEntity = event.getEntity();
        EntityType<?> highATKEntityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(ETERNAL_ENTITY));
        if (eternalEntity.getType().equals(highATKEntityType) || eternalEntity.getCustomName().equals(ETERNAL_ENTITY)) {
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
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel) {
            LivingEntity eternalEntity = (LivingEntity) event.getEntity();
            EntityType<?> highATKEntityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(ETERNAL_ENTITY));
            if (eternalEntity.getType().equals(highATKEntityType) || eternalEntity.getCustomName().equals(ETERNAL_ENTITY)) {
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