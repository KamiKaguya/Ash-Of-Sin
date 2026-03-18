package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCarianEvent {

    public static final Random RANDOM = new Random();
    private static final Enchantment ENHANCE_ENCHANTMENT = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("minecraft", "sweeping"));

    @SubscribeEvent
    public static void carian(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        Entity target = event.getEntity();
        Entity attackerEntity = event.getSource().getEntity();
        String carianExtraDamage = "carian_extra_damage";
        CompoundTag persistentData = target.getPersistentData();

        if (persistentData.contains(carianExtraDamage)) {
            persistentData.remove(carianExtraDamage);
            return;
        }

        if (attackerEntity instanceof LivingEntity attacker) {
            int freezingLevel = getEnchantmentLevel(attacker, ENHANCE_ENCHANTMENT);
            float freezeMultiplier = 1.0f + 0.2f * freezingLevel;

            if (holdDarkMoonGreatsword(attacker)) {
                float originalDamage = event.getAmount();
                float magicDamage = originalDamage * freezeMultiplier;
                float freezeDamage = originalDamage * 0.7F * freezeMultiplier;

                if (attacker instanceof ServerPlayer) {
                    persistentData.putBoolean(carianExtraDamage, true);
                    try {
                        target.hurt(attacker.damageSources().magic(), magicDamage);
                        if (RANDOM.nextFloat() <= 0.7F) {
                            target.hurt(attacker.damageSources().freeze(), freezeDamage);
                        }
                    } finally {
                        persistentData.remove(carianExtraDamage);
                    }
                }
            }

            if (holdCarianKnightsSword(attacker)) {
                float originalDamage = event.getAmount();
                float finalDamage = originalDamage * freezeMultiplier;

                if (attacker instanceof ServerPlayer) {
                    persistentData.putBoolean(carianExtraDamage, true);
                    try {
                        target.hurt(attacker.damageSources().magic(), finalDamage);
                    } finally {
                        persistentData.remove(carianExtraDamage);
                    }
                }
            }
        }
    }

    public static boolean holdDarkMoonGreatsword(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdDarkMoonGreatsword = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "dark_moon_greatsword"));
        return !(mainHand.isEmpty()) && (holdDarkMoonGreatsword);
    }

    public static boolean holdCarianKnightsSword(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdCarianKnightsSword = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "carian_knights_sword"));
        return !(mainHand.isEmpty()) && (holdCarianKnightsSword);
    }

    @SubscribeEvent
    public static void magicReduction(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntity();
        DamageSource damageSource = event.getSource();
        if (holdDarkMoonGreatsword(livingEntity)) {
            if (damageSource.is(DamageTypes.MAGIC)) {
                float originalDamage = event.getAmount();

                float reductionDamage = originalDamage * 0.2F;
                if (livingEntity instanceof ServerPlayer) {
                    event.setAmount(reductionDamage);
                }
            }
        }

        if (holdCarianKnightsSword(livingEntity)) {
            if (damageSource.is(DamageTypes.MAGIC)) {
                float originalDamage = event.getAmount();

                float reductionDamage = originalDamage * 0.5F;
                if (livingEntity instanceof ServerPlayer) {
                    event.setAmount(reductionDamage);
                }
            }
        }
    }

    public static int getEnchantmentLevel(LivingEntity entity, Enchantment enchantment) {
        if (enchantment == null) {
            return 0;
        }
        ItemStack mainHand = entity.getMainHandItem();
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, mainHand);
    }
}