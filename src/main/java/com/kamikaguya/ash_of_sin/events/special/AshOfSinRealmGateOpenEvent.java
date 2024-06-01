package com.kamikaguya.ash_of_sin.events.special;

import com.kamikaguya.ash_of_sin.config.AshOfSinConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Gate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinRealmGateOpenEvent {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        Entity deadEntity = event.getEntity();
        Level world = deadEntity.level;

        if (!world.isClientSide() && AshOfSinConfig.ENTITY_DEAD.get().contains(EntityType.getKey(deadEntity.getType()).toString())) {
            CompoundTag entityData = deadEntity.getPersistentData();
            String key = "RealmGateOpened";

            if (!entityData.getBoolean(key)) {

                EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation("ash_of_sin:gate"));
                if (entityType != null && entityType.create(world) instanceof Gate) {
                    Gate gate = (Gate) entityType.create(world);
                    if (gate != null) {
                        gate.moveTo(deadEntity.getX(), deadEntity.getY(), deadEntity.getZ(), deadEntity.getYRot(), deadEntity.getXRot());
                        world.addFreshEntity(gate);

                        entityData.putBoolean(key, true);
                    }
                }
            }
        }
    }
}
