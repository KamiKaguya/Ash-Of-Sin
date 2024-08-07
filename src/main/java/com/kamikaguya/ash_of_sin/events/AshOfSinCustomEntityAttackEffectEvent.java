package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomAttackEffectConfig;
import com.kamikaguya.ash_of_sin.config.CustomAttackEntityConfig;
import com.kamikaguya.ash_of_sin.config.CustomEntityAttackEffectConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomEntityAttackEffectEvent {
    public static final CustomEntityAttackEffectConfig customEntityAttackEffectConfig = new CustomEntityAttackEffectConfig();
    static {
        customEntityAttackEffectConfig.loadConfig();
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker) {
            LivingEntity target = event.getEntityLiving();

            if (target == null) {
                return;
            }

            List<CustomAttackEntityConfig> customAttackEffectConfig = customEntityAttackEffectConfig.getCustomEntityAttackEffectConfig();

            for (CustomAttackEntityConfig config : customAttackEffectConfig) {
                if (config.getEntity().equals(attacker.getType().getRegistryName().toString())) {
                    applyEffects(target, config.getEffect());
                }
            }
        }

        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            LivingEntity target = event.getEntityLiving();

            if (target == null) {
                return;
            }

            List<CustomAttackEntityConfig> customAttackEffectConfig = customEntityAttackEffectConfig.getCustomEntityAttackEffectConfig();

            for (CustomAttackEntityConfig config : customAttackEffectConfig) {
                if (config.getEntity().equals(attacker.getType().getRegistryName().toString())) {
                    applyEffects(target, config.getEffect());
                }
            }
        }
    }

    public static void applyEffects(LivingEntity target, List<CustomAttackEffectConfig> effects) {
        for (CustomAttackEffectConfig attackEffectConfig : effects) {
            MobEffect attackEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(attackEffectConfig.getEffect()));
            if (attackEffect != null) {
                boolean hasStrongerEffect = target.getActiveEffects().stream()
                        .anyMatch(existingEffect -> existingEffect.getEffect().equals(attackEffect) && existingEffect.getAmplifier() > attackEffectConfig.getAmplifier());

                if (!hasStrongerEffect) {
                    int duration = attackEffectConfig.getDuration();
                    int amplifier = attackEffectConfig.getAmplifier();
                    MobEffectInstance effectInstance = new MobEffectInstance(attackEffect, duration * 20, amplifier);
                    target.addEffect(effectInstance);
                }
            }
        }
    }
}