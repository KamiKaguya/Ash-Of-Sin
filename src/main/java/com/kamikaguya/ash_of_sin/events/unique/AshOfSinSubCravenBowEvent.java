package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinSubCravenBowEvent {
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Entity entity = event.getEntity();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof LivingEntity livingEntity) {
            if (holdSubCravenBow(livingEntity)) {
                if (livingEntity instanceof ServerPlayer player && AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(player)) {
                    return;
                }
                float originalDamage = event.getAmount();
                float bonusDamage = originalDamage * 2.22F;
                event.setAmount(bonusDamage);
                if (RANDOM.nextFloat() <= 0.08F) {
                    float baseSubCravenBowDamage = bonusDamage * 1.22F;
                    event.setAmount(baseSubCravenBowDamage);
                }
            }
        }
    }

    private static boolean holdSubCravenBow(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        if (!(mainHand.isEmpty()) && (mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "sub_craven_bow")))) {
            return true;
        }
        return false;
    }
}
