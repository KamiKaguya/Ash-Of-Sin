package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCarianEvent {
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
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity attacker) {
            if (holdDarkMoonGreatsword(attacker)) {
                float originalDamage = event.getAmount();
                float correctionDamage = damageAfterArmor(target, originalDamage);
                float magicDamage = (originalDamage * 1.2F) + correctionDamage;
                float freezeDamage = originalDamage * 0.7F;
                if (attacker instanceof ServerPlayer) {
                    target.hurt(attacker.damageSources().magic(), magicDamage);
                    if (RANDOM.nextFloat() <= 0.7F) {
                        target.hurt(attacker.damageSources().freeze(), freezeDamage);
                    }
                }
            }

            if (holdCarianKnightsSword(attacker)) {
                float originalDamage = event.getAmount();
                float correctionDamage = damageAfterArmor(target, originalDamage);
                float finalDamage = originalDamage + correctionDamage;
                if (attacker instanceof ServerPlayer) {
                    target.hurt(attacker.damageSources().magic(), finalDamage);
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
                if (hasProtectionEnchantmentAromor(livingEntity, Enchantments.ALL_DAMAGE_PROTECTION)) {
                    originalDamage = damageAftertArmorProtection(livingEntity.getArmorSlots(), originalDamage);
                }

                float reductionDamage = originalDamage * 0.2F;
                if (livingEntity instanceof ServerPlayer) {
                    event.setAmount(reductionDamage);
                }
            }
        }

        if (holdCarianKnightsSword(livingEntity)) {
            if (damageSource.is(DamageTypes.MAGIC)) {
                float originalDamage = event.getAmount();
                if (hasProtectionEnchantmentAromor(livingEntity, Enchantments.ALL_DAMAGE_PROTECTION)) {
                    originalDamage = damageAftertArmorProtection(livingEntity.getArmorSlots(), originalDamage);
                }

                float reductionDamage = originalDamage * 0.5F;
                if (livingEntity instanceof ServerPlayer) {
                    event.setAmount(reductionDamage);
                }
            }
        }
    }

    public static float damageAfterArmor(LivingEntity entity, float baseDamage) {
        float armorValue = entity.getArmorValue();
        float toughnessValue = (float) entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        return damageAfterArmorReduction(armorValue, toughnessValue, baseDamage);
    }

    public static float damageAfterArmorReduction(float armorValue, float toughnessValue, float baseDamage) {
        float damageAfterArmorReduction;
        float damageAfterToughnessReduction;
        if (toughnessValue == 0) {
            damageAfterArmorReduction = (baseDamage * Math.max(10 / (10 + armorValue), 0.2f));
        } else {
            if (baseDamage > (40 / (toughnessValue + 1))) {
                damageAfterToughnessReduction = baseDamage - ((40 / (toughnessValue + 1)) / 2);
                damageAfterArmorReduction = (damageAfterToughnessReduction * Math.max(10 / (10 + armorValue), 0.2f));
            } else {
                damageAfterToughnessReduction = baseDamage - (40 / (toughnessValue + 1));
                damageAfterArmorReduction = (damageAfterToughnessReduction * Math.max(10 / (10 + armorValue), 0.2f));
            }
        }
        return damageAfterArmorReduction;
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

    public static float damageAftertArmorProtection(Iterable<ItemStack> armorItems, float originalDamage) {
        float damageAfterArmorProtection = 0;
        for (ItemStack armorItem : armorItems) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armorItem);
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof ProtectionEnchantment) {
                    int protectLevel = entry.getValue();

                    damageAfterArmorProtection += originalDamage * (10 / (10.0f + protectLevel));
                }
            }
        }
        return damageAfterArmorProtection;
    }
}