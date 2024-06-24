package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.KamiKaguya;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCarianEvent {
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
        Entity entity = damageSource.getEntity();
        if (entity instanceof LivingEntity attacker) {
            if (holdDarkMoonGreatsword(attacker)) {
                float originalDamage = event.getAmount();
                float correctionDamage = damageAfterTargetArmorProtection(target.getArmorSlots(), originalDamage);
                float magicDamage = (originalDamage * 1.2F) + correctionDamage;
                float freezeDamage = originalDamage * 0.7F;
                if (attacker instanceof ServerPlayer serverPlayer) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        target.hurt(DamageSource.MAGIC, magicDamage);
                        if (RANDOM.nextFloat() <= 0.7F) {
                            target.hurt(DamageSource.FREEZE, freezeDamage);
                        }
                    }
                }

                if (attacker instanceof KamiKaguya) {
                    target.hurt(DamageSource.MAGIC, magicDamage);
                    if (RANDOM.nextFloat() <= 0.7F) {
                        target.hurt(DamageSource.FREEZE, freezeDamage);
                    }
                }
            }

            if (holdCarianKnightsSword(attacker)) {
                float originalDamage = event.getAmount();
                float correctionDamage = damageAfterTargetArmorProtection(target.getArmorSlots(), originalDamage);
                float finalDamage = originalDamage + correctionDamage;
                if (attacker instanceof ServerPlayer serverPlayer) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        target.hurt(DamageSource.MAGIC, finalDamage);
                    }
                }
            }
        }
    }

    public static boolean holdDarkMoonGreatsword(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdDarkMoonGreatsword = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "dark_moon_greatsword"));
        return !(mainHand.isEmpty()) && (holdDarkMoonGreatsword);
    }

    public static boolean holdCarianKnightsSword(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdCarianKnightsSword = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "carian_knights_sword"));
        return !(mainHand.isEmpty()) && (holdCarianKnightsSword);
    }

    @SubscribeEvent
    public static void magicReduction(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        if (holdDarkMoonGreatsword(livingEntity)) {
            if (damageSource.isMagic()) {
                float originalDamage = event.getAmount();
                if (hasProtectionEnchantmentAromor(livingEntity, Enchantments.ALL_DAMAGE_PROTECTION)) {
                    originalDamage = damageAftertArmorProtection(livingEntity.getArmorSlots(), originalDamage);
                }

                float reductionDamage = originalDamage * 0.2F;
                if (livingEntity instanceof ServerPlayer player) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(player))) {
                        event.setAmount(reductionDamage);
                    }
                }

                if (livingEntity instanceof KamiKaguya) {
                    event.setAmount(reductionDamage);
                }
            }
        }

        if (holdCarianKnightsSword(livingEntity)) {
            if (damageSource.isMagic()) {
                float originalDamage = event.getAmount();
                if (hasProtectionEnchantmentAromor(livingEntity, Enchantments.ALL_DAMAGE_PROTECTION)) {
                    originalDamage = damageAftertArmorProtection(livingEntity.getArmorSlots(), originalDamage);
                }

                float reductionDamage = originalDamage * 0.5F;
                if (livingEntity instanceof ServerPlayer player) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(player))) {
                        event.setAmount(reductionDamage);
                    }
                }

                if (livingEntity instanceof KamiKaguya) {
                    event.setAmount(reductionDamage);
                }
            }
        }
    }

    public static boolean hasProtectionEnchantmentAromor(LivingEntity livingEntity, Enchantment enchantment) {
        Iterable<ItemStack> armors = livingEntity.getArmorSlots();
        for (ItemStack stack : armors) {
            if (EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack) > 0) {
                return true;
            }
        }
        return false;
    }

    public static float damageAfterTargetArmorProtection(Iterable<ItemStack> armorItems, float originalDamage) {
        float damageAfterArmorProtection = 0;
        float correctionDamage = 0;
        for (ItemStack armorItem : armorItems) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armorItem);
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof ProtectionEnchantment) {
                    int protectLevel = entry.getValue();

                    damageAfterArmorProtection += originalDamage * (10 / ((protectLevel + 10.0f) / 2));
                    correctionDamage += originalDamage - damageAfterArmorProtection;
                }
            }
        }
        return correctionDamage;
    }

    public static float damageAftertArmorProtection(Iterable<ItemStack> armorItems, float originalDamage) {
        float damageAfterArmorProtection = 0;
        for (ItemStack armorItem : armorItems) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armorItem);
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof ProtectionEnchantment) {
                    int protectLevel = entry.getValue();

                    damageAfterArmorProtection += originalDamage * (10 / ((protectLevel + 10.0f) / 2));
                }
            }
        }
        return damageAfterArmorProtection;
    }
}