package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinSubCravenBowEvent {
    public static final Random RANDOM = new Random();

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity livingEntity) {
            if (holdSubCravenBow(livingEntity)) {
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

    public static boolean holdSubCravenBow(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdSubCravenBow = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "sub_craven_bow"));
        return !(mainHand.isEmpty()) && (holdSubCravenBow);
    }
}