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

public class AshOfSinDualBladesEvent {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (holdDualBlades(serverPlayer)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                    haste(serverPlayer);
                }
            }
        }
    }

    private static boolean holdDualBlades(ServerPlayer serverPlayer) {
        ItemStack mainHand = serverPlayer.getMainHandItem();
        ItemStack offHand = serverPlayer.getOffhandItem();
        boolean holdDualBlades = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "elucidator")) && offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "dark_repulser"));
        if (!(mainHand.isEmpty()) && !(offHand.isEmpty()) && (holdDualBlades)) {
            return true;
        }
        return false;
    }

    private static void haste(ServerPlayer serverPlayer) {
        MobEffect haste = MobEffects.DIG_SPEED;
        boolean alreadyHoldDualBlades = serverPlayer.getActiveEffects().stream()
                .anyMatch(existingEffect -> existingEffect.getEffect().equals(haste) && existingEffect.getAmplifier() >= 0);
        boolean alreadyStarBurstStream = serverPlayer.getActiveEffects().stream()
                .anyMatch(existingEffect -> existingEffect.getEffect().equals(haste) && existingEffect.getAmplifier() == 4);
        if (!alreadyHoldDualBlades) {
            MobEffectInstance hasteEffect = new MobEffectInstance(haste, 7 * 20, 0);
            serverPlayer.addEffect(hasteEffect);
        } else {
            int amplifier = serverPlayer.getEffect(haste).getAmplifier();
            if (!alreadyStarBurstStream) {
                MobEffectInstance bleedEffect = new MobEffectInstance(haste, 7 * 20, amplifier + 1);
                serverPlayer.addEffect(bleedEffect);
            } else {
                MobEffectInstance bleedEffect = new MobEffectInstance(haste, 13 * 20, 4);
                serverPlayer.addEffect(bleedEffect);
            }
        }
    }
}
