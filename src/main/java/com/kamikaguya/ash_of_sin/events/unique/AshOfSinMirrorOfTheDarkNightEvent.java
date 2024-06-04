package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinMirrorOfTheDarkNightEvent {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        DamageSource damageSource = event.getSource();
        LivingEntity playerEntity = (LivingEntity) event.getEntity();
        LivingEntity anotherEntity = (LivingEntity) event.getEntity();
        if (playerEntity instanceof ServerPlayer serverPlayer) {
            if (holdMirrorOfTheDarkNight(serverPlayer)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                    float originalDamage = event.getAmount();
                    float afterReductionDamage = originalDamage * 0.2F;
                    event.setAmount(afterReductionDamage);
                }
            }
        }
        if (anotherEntity instanceof Another another) {
            if (holdMirrorOfTheDarkNight(another)) {
                if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        float originalDamage = event.getAmount();
                        float afterReductionDamage = originalDamage * 0.2F;
                        event.setAmount(afterReductionDamage);
                    }
                }
            }
        }
    }

    private static boolean holdMirrorOfTheDarkNight(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        ItemStack offHand = livingEntity.getOffhandItem();
        boolean holdMirrorOfTheDarkNight = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "mirror_of_the_dark_night")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "mirror_of_the_dark_night"));
        if (!(mainHand.isEmpty()) && (holdMirrorOfTheDarkNight)) {
            return true;
        }
        if (!(offHand.isEmpty()) && (holdMirrorOfTheDarkNight)) {
            return true;
        }
        return false;
    }
}
