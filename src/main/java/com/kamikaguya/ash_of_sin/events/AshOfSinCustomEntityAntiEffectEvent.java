package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomEntityAntiEffectConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomEntityAntiEffectEvent {

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (CustomEntityAntiEffectConfig.ANTI_ON.get()) {
            if (event.getEntityLiving().level.isClientSide()) {
                return;
            }
            LivingEntity livingEntity = event.getEntityLiving();
            ResourceLocation entityResourceLocation = EntityType.getKey(livingEntity.getType());

            List<? extends String> antiEffectEntityList = CustomEntityAntiEffectConfig.ANTI_EFFECT_ENTITY.get();
            List<? extends String> antiEffectList = CustomEntityAntiEffectConfig.ANTI_EFFECT.get();

            if (antiEffectEntityList.contains(entityResourceLocation.toString())) {
                for (MobEffectInstance effectInstance : new ArrayList<>(livingEntity.getActiveEffects())) {
                    String effectId = effectInstance.getEffect().getRegistryName().toString();
                    if (antiEffectList.stream().anyMatch(s -> s.equals(effectId))) {
                        livingEntity.removeEffect(effectInstance.getEffect());
                        float livingEntityMaxHealth = livingEntity.getMaxHealth();
                        livingEntity.heal(livingEntityMaxHealth);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (CustomEntityAntiEffectConfig.ANTI_ON.get()) {
            if (event.getEntityLiving().level.isClientSide()) {
                return;
            }
            LivingEntity livingEntity = event.getEntityLiving();
            ResourceLocation entityResourceLocation = EntityType.getKey(livingEntity.getType());

            List<? extends String> antiEffectEntityList = CustomEntityAntiEffectConfig.ANTI_EFFECT_ENTITY.get();
            List<? extends String> antiEffectList = CustomEntityAntiEffectConfig.ANTI_EFFECT.get();

            if (antiEffectEntityList.contains(entityResourceLocation.toString())) {
                for (MobEffectInstance effectInstance : new ArrayList<>(livingEntity.getActiveEffects())) {
                    String effectId = effectInstance.getEffect().getRegistryName().toString();
                    if (antiEffectList.stream().anyMatch(s -> s.equals(effectId))) {
                        event.setAmount(0);
                        livingEntity.removeEffect(effectInstance.getEffect());
                        float livingEntityMaxHealth = livingEntity.getMaxHealth();
                        livingEntity.heal(livingEntityMaxHealth);
                    }
                }
            }
        }
    }
}