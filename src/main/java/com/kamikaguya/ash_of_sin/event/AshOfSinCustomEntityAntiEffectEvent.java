package com.kamikaguya.ash_of_sin.event;

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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomEntityAntiEffectEvent {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (CustomEntityAntiEffectConfig.ANTI_ON.get()) {
            if (event.getEntity().level().isClientSide()) {
                return;
            }

            LivingEntity livingEntity = event.getEntity();
            ResourceLocation entityResourceLocation = EntityType.getKey(livingEntity.getType());

            List<? extends String> antiEffectEntityList = CustomEntityAntiEffectConfig.ANTI_EFFECT_ENTITY.get();
            List<? extends String> antiEffectList = CustomEntityAntiEffectConfig.ANTI_EFFECT.get();

            if (antiEffectEntityList.contains(entityResourceLocation.toString())) {
                for (MobEffectInstance effectInstance : new ArrayList<>(livingEntity.getActiveEffects())) {
                    String effectId = ForgeRegistries.MOB_EFFECTS.getKey(effectInstance.getEffect()).toString();
                    if (antiEffectList.stream().anyMatch(s -> s.equals(effectId))) {
                        livingEntity.removeEffect(effectInstance.getEffect());
                        if (CustomEntityAntiEffectConfig.HEAL.get()) {
                            float livingEntityMaxHealth = livingEntity.getMaxHealth();
                            livingEntity.heal(livingEntityMaxHealth);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (CustomEntityAntiEffectConfig.ANTI_ON.get()) {
            if (event.getEntity().level().isClientSide()) {
                return;
            }

            LivingEntity livingEntity = event.getEntity();
            ResourceLocation entityResourceLocation = EntityType.getKey(livingEntity.getType());

            List<? extends String> antiEffectEntityList = CustomEntityAntiEffectConfig.ANTI_EFFECT_ENTITY.get();
            List<? extends String> antiEffectList = CustomEntityAntiEffectConfig.ANTI_EFFECT.get();

            if (antiEffectEntityList.contains(entityResourceLocation.toString())) {
                for (MobEffectInstance effectInstance : new ArrayList<>(livingEntity.getActiveEffects())) {
                    String effectId = ForgeRegistries.MOB_EFFECTS.getKey(effectInstance.getEffect()).toString();
                    if (antiEffectList.stream().anyMatch(s -> s.equals(effectId))) {
                        event.setAmount(0);
                        livingEntity.removeEffect(effectInstance.getEffect());
                        if (CustomEntityAntiEffectConfig.HEAL.get()) {
                            float livingEntityMaxHealth = livingEntity.getMaxHealth();
                            livingEntity.heal(livingEntityMaxHealth);
                        }
                    }
                }
            }
        }
    }
}