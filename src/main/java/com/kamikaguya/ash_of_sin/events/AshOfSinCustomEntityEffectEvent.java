package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomEffectConfig;
import com.kamikaguya.ash_of_sin.config.CustomEntityEffectConfig;
import com.kamikaguya.ash_of_sin.config.CustomEntityEffectConfigManager;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomEntityEffectEvent {
    private static final CustomEntityEffectConfigManager customEntityEffectConfigManager = new CustomEntityEffectConfigManager();
    static {
        customEntityEffectConfigManager.loadConfig();
    }

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        LivingEntity livingEntity = event.getEntityLiving();
        ResourceLocation entityResourceLocation = EntityType.getKey(livingEntity.getType());
        String entityName = entityResourceLocation.toString();

        List<CustomEntityEffectConfig> customEffectConfig = customEntityEffectConfigManager.getCustomEntityEffectConfigManager();

        for (CustomEntityEffectConfig config : customEffectConfig) {
            if (config.getEntity().equals(entityName)) {
                for (CustomEffectConfig effectConfig : config.getEffect()) {
                    MobEffect desiredEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectConfig.getEffect()));
                    if (desiredEffect != null) {
                        boolean hasEffect = livingEntity.getActiveEffects().stream()
                                .anyMatch(existingEffect -> existingEffect.getEffect() == desiredEffect);

                        boolean isWeakerEffect = hasEffect && livingEntity.getActiveEffects().stream()
                                .filter(existingEffect -> existingEffect.getEffect() == desiredEffect)
                                .anyMatch(existingEffect -> existingEffect.getAmplifier() > effectConfig.getAmplifier());

                        if (!hasEffect || isWeakerEffect) {
                            int duration = "infinite".equalsIgnoreCase(effectConfig.getDuration())
                                    ? Integer.MAX_VALUE
                                    : Integer.parseInt(effectConfig.getDuration());
                            int amplifier = effectConfig.getAmplifier();
                            MobEffectInstance effectInstance = new MobEffectInstance(desiredEffect, duration, amplifier);
                            livingEntity.addEffect(effectInstance);
                        }
                    }
                }
            }
        }
    }
}