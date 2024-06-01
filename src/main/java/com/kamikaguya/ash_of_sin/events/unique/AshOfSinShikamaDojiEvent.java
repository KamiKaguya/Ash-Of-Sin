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
    private static final Random RANDOM = new Random();

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
        if (attacker instanceof ServerPlayer player) {
            if (holdShikamaDoji(player)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(player))) {
                    bloodSoaked(target);
                }
            }
        }
    }

    private static boolean holdShikamaDoji(ServerPlayer player) {
        ItemStack mainHand = player.getMainHandItem();
        boolean holdShikamaDoji = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "shikama_doji"));
        if (!(mainHand.isEmpty()) && (holdShikamaDoji)) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void immuneBleed(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof ServerPlayer) || !(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.getEntityLiving();
        MobEffect bleed = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","bleeding"));
        if (holdShikamaDoji(player)) {
            if (bleed != null) {
                player.removeEffect(bleed);
            }
        }
    }

    private static void bloodSoaked(LivingEntity target) {
        MobEffect bleed = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","bleeding"));
        if (bleed != null) {
            boolean alreadyBloodSoaked = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(bleed) && existingEffect.getAmplifier() >= 0);
            boolean alreadyGrievousBodilyHarm = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(bleed) && existingEffect.getAmplifier() == 2);
            if (!alreadyBloodSoaked) {
                MobEffectInstance bleedEffect = new MobEffectInstance(bleed, 13 * 20, 0);
                target.addEffect(bleedEffect);
            } else {
                int amplifier = target.getEffect(bleed).getAmplifier();
                if (!alreadyGrievousBodilyHarm) {
                    MobEffectInstance bleedEffect = new MobEffectInstance(bleed, 13 * 20, amplifier + 1);
                    target.addEffect(bleedEffect);
                } else {
                    MobEffectInstance bleedEffect = new MobEffectInstance(bleed, 60 * 20, 2);
                    target.addEffect(bleedEffect);
                    if (RANDOM.nextFloat() < 0.15F) {
                        MobEffect sundering = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","sundering"));
                        MobEffectInstance sunderingEffect = new MobEffectInstance(sundering, 7 * 20, 0);
                        target.addEffect(sunderingEffect);
                    }
                }
            }
        }
    }
}
