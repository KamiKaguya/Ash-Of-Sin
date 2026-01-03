package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (holdShikamaDoji(serverPlayer)) {
                bloodSoaked(target);
            }
        }
    }

    public static boolean holdShikamaDoji(ServerPlayer serverPlayer) {
        ItemStack mainHand = serverPlayer.getMainHandItem();
        boolean holdShikamaDoji = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "shikama_doji"));
        return !(mainHand.isEmpty()) && (holdShikamaDoji);
    }

    @SubscribeEvent
    public static void immunefragility(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }

        MobEffect fragility = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("minecraft","slowness"));
        if (holdShikamaDoji(serverPlayer)) {
            if (fragility != null) {
                serverPlayer.removeEffect(fragility);
            }
        }
    }

    public static void bloodSoaked(LivingEntity target) {
        MobEffect fragilityEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("minecraft","slowness"));
        if (fragilityEffect != null) {
            boolean alreadyBloodSoaked = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(fragilityEffect) && existingEffect.getAmplifier() >= 0);
            boolean alreadyGrievousBodilyHarm = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(fragilityEffect) && existingEffect.getAmplifier() == 2);
            if (!alreadyBloodSoaked) {
                MobEffectInstance fragility = new MobEffectInstance(fragilityEffect, 13 * 20, 0);
                target.addEffect(fragility);
            } else {
                int amplifier = target.getEffect(fragilityEffect).getAmplifier();
                if (!alreadyGrievousBodilyHarm) {
                    MobEffectInstance fragility = new MobEffectInstance(fragilityEffect, 13 * 20, amplifier + 1);
                    target.addEffect(fragility);
                } else {
                    MobEffectInstance fragility = new MobEffectInstance(fragilityEffect, 60 * 20, 2);
                    target.addEffect(fragility);
                    if (RANDOM.nextFloat() <= 0.15F) {
                        MobEffect insanityEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("minecraft","weakness"));
                        MobEffectInstance insanity = new MobEffectInstance(insanityEffect, 7 * 20, 0);
                        target.addEffect(insanity);
                    }
                }
            }
        }
    }
}