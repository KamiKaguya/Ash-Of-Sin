package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinShikamaDojiEvent {
    public static final Random RANDOM = new Random();

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
            if (holdShikamaDoji(serverPlayer)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                    bloodSoaked(target);
                }
            }
        }
    }

    public static boolean holdShikamaDoji(ServerPlayer serverPlayer) {
        ItemStack mainHand = serverPlayer.getMainHandItem();
        boolean holdShikamaDoji = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "shikama_doji"));
        return !(mainHand.isEmpty()) && (holdShikamaDoji);
    }

    @SubscribeEvent
    public static void immuneBleed(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof ServerPlayer serverPlayer) || !(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        MobEffect bleed = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","bleeding"));
        if (holdShikamaDoji(serverPlayer)) {
            if (bleed != null) {
                serverPlayer.removeEffect(bleed);
            }
        }
    }

    public static void bloodSoaked(LivingEntity target) {
        MobEffect bleedEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","bleeding"));
        if (bleedEffect != null) {
            boolean alreadyBloodSoaked = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(bleedEffect) && existingEffect.getAmplifier() >= 0);
            boolean alreadyGrievousBodilyHarm = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(bleedEffect) && existingEffect.getAmplifier() == 2);
            if (!alreadyBloodSoaked) {
                MobEffectInstance bleed = new MobEffectInstance(bleedEffect, 13 * 20, 0);
                target.addEffect(bleed);
            } else {
                int amplifier = target.getEffect(bleedEffect).getAmplifier();
                if (!alreadyGrievousBodilyHarm) {
                    MobEffectInstance bleed = new MobEffectInstance(bleedEffect, 13 * 20, amplifier + 1);
                    target.addEffect(bleed);
                } else {
                    MobEffectInstance bleed = new MobEffectInstance(bleedEffect, 60 * 20, 2);
                    target.addEffect(bleed);
                    if (RANDOM.nextFloat() <= 0.15F) {
                        MobEffect sunderingEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","sundering"));
                        MobEffectInstance sundering = new MobEffectInstance(sunderingEffect, 7 * 20, 0);
                        target.addEffect(sundering);
                    }
                }
            }
        }
    }
}