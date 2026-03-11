package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Entity target = event.getEntity();
        Entity attackerEntity = event.getSource().getEntity();
        String subCravenBowExtraDamage = "sub_craven_bow_extra_damage";
        CompoundTag persistentData = target.getPersistentData();

        if (persistentData.contains(subCravenBowExtraDamage)) {
            persistentData.remove(subCravenBowExtraDamage);
            return;
        }

        if (attackerEntity instanceof LivingEntity attacker) {
            if (holdSubCravenBow(attacker)) {
                float originalDamage = event.getAmount();
                float bonusDamage = originalDamage * 2.22F;
                float baseSubCravenBowDamage = bonusDamage * 1.22F;
                if (attacker instanceof ServerPlayer) {
                    persistentData.putBoolean(subCravenBowExtraDamage, true);
                    try {
                        target.hurt(attacker.damageSources().mobAttack(attacker), bonusDamage);
                        if (RANDOM.nextFloat() <= 0.08F) {
                            target.hurt(attacker.damageSources().mobAttack(attacker), baseSubCravenBowDamage);
                        }
                    } finally {
                        persistentData.remove(subCravenBowExtraDamage);
                    }
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