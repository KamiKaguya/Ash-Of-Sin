package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinDespairScytheEvent {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (holdDespairScythe(serverPlayer)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                    diffuseDespair(target);
                }
            }
        }
    }

    public static boolean holdDespairScythe(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdDespairScythe = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "despair_scythe"));
        return !(mainHand.isEmpty()) && (holdDespairScythe);
    }

    public static void diffuseDespair(LivingEntity target) {
        MobEffect darknessEffect  = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("wildbackport", "darkness"));
        MobEffect sunderingEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","sundering"));
        MobEffect blindnessEffect = MobEffects.BLINDNESS;
        MobEffect witherEffect = MobEffects.WITHER;
        if (darknessEffect != null && sunderingEffect != null) {
            boolean alreadyBlindness = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(blindnessEffect) && existingEffect.getAmplifier() >= 0);
            boolean alreadyDeepBlindness = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(blindnessEffect) && existingEffect.getAmplifier() == 2);
            boolean alreadyDarkness = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(darknessEffect) && existingEffect.getAmplifier() >= 0);
            boolean alreadyDeepDarkness = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(darknessEffect) && existingEffect.getAmplifier() == 2);
            boolean alreadySundering = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(sunderingEffect) && existingEffect.getAmplifier() >= 0);
            boolean alreadyDeepSundering = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(sunderingEffect) && existingEffect.getAmplifier() == 2);

            if (!alreadyBlindness) {
                MobEffectInstance blindness = new MobEffectInstance(blindnessEffect, 66 * 20, 0);
                target.addEffect(blindness);
            } else {
                int amplifier = target.getEffect(blindnessEffect).getAmplifier();
                if (!alreadyDeepBlindness) {
                    MobEffectInstance blindness = new MobEffectInstance(blindnessEffect, 66 * 20, amplifier + 1);
                    target.addEffect(blindness);
                } else {
                    MobEffectInstance blindness = new MobEffectInstance(blindnessEffect, 66 * 20, 2);
                    target.addEffect(blindness);
                }
            }

            if (!alreadyDarkness) {
                MobEffectInstance darkness = new MobEffectInstance(darknessEffect, 66 * 20, 0);
                target.addEffect(darkness);
            } else {
                int amplifier = target.getEffect(darknessEffect).getAmplifier();
                if (!alreadyDeepDarkness) {
                    MobEffectInstance darkness = new MobEffectInstance(darknessEffect, 66 * 20, amplifier + 1);
                    target.addEffect(darkness);
                } else {
                    MobEffectInstance darkness = new MobEffectInstance(darknessEffect, 66 * 20, 2);
                    target.addEffect(darkness);
                }
            }

            if (!alreadySundering) {
                MobEffectInstance sundering = new MobEffectInstance(sunderingEffect, 66 * 20, 0);
                target.addEffect(sundering);
            } else {
                int amplifier = target.getEffect(sunderingEffect).getAmplifier();
                if (!alreadyDeepSundering) {
                    MobEffectInstance sundering = new MobEffectInstance(sunderingEffect, 66 * 20, amplifier + 1);
                    target.addEffect(sundering);
                } else {
                    MobEffectInstance sundering = new MobEffectInstance(sunderingEffect, 66 * 20, 2);
                    target.addEffect(sundering);
                }
            }

            boolean alreadyDespair = alreadyDeepDarkness || alreadyDeepSundering;
            if (alreadyDespair) {
                MobEffectInstance wither = new MobEffectInstance(witherEffect, 66 * 20, 12);
                target.addEffect(wither);
            }
        }
    }
}