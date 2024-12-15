package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.EternalEntityConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinEternalEntityEvent {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        LivingEntity eternalEntity = event.getEntity();
        List<String> eternalEntityList = EternalEntityConfig.ETERNAL_ENTITY.get().stream().map(s -> (String) s).toList();
        for (String eternalEntityID : eternalEntityList) {
            EntityType<?> eternalEntityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(eternalEntityID));
            if (eternalEntity.getType().equals(eternalEntityType)) {
                if (eternalEntity.level instanceof ServerLevel serverLevel) {
                    serverLevel.getChunkSource().addEntity(eternalEntity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (event.getEntity() instanceof LivingEntity eternalEntity) {
        List<String> eternalEntityList = EternalEntityConfig.ETERNAL_ENTITY.get().stream().map(s -> (String) s).toList();
        for (String eternalEntityID : eternalEntityList) {
            EntityType<?> eternalEntityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(eternalEntityID));
            if (eternalEntity.getType().equals(eternalEntityType)) {
                preventDespawn(eternalEntity);
            }
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