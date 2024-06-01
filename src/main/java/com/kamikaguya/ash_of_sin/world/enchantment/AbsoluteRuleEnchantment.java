package com.kamikaguya.ash_of_sin.world.enchantment;


import com.google.common.collect.Multimap;
import com.kamikaguya.ash_of_sin.config.AshOfSinConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Map;

public class AbsoluteRuleEnchantment extends Enchantment {

    public AbsoluteRuleEnchantment(Enchantment.Rarity rarity, EnchantmentCategory category) {
        super(rarity, category, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 112247;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) * enchantmentLevel;
    }

    @Override
    public int getMaxLevel() {
        return 20;
    }

    @Override
    public Component getFullname(int level) {
        return ((MutableComponent) super.getFullname(level)).withStyle(ChatFormatting.DARK_PURPLE);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int enchantmentLevel) {
        if (enchantmentLevel > 20 || !(target instanceof LivingEntity livingTarget)) {
            return;
        }

        float armorValue = livingTarget.getArmorValue();
        float toughnessValue = (float) livingTarget.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        ItemStack mainHandWeapon = attacker.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHandWeapon = attacker.getItemBySlot(EquipmentSlot.OFFHAND);

        EquipmentSlot usedSlot = null;
        if (EnchantmentHelper.getItemEnchantmentLevel(this, mainHandWeapon) > 0) {
            usedSlot = EquipmentSlot.MAINHAND;
        } else if (EnchantmentHelper.getItemEnchantmentLevel(this, offHandWeapon) > 0) {
            usedSlot = EquipmentSlot.OFFHAND;
        }

        if (usedSlot == null) {
            return;
        }

        ItemStack weapon = attacker.getItemBySlot(usedSlot);

        float baseDamage = getAttackDamage(weapon, usedSlot);
        float extraDamage = calculateExtraDamageBasedOnArmor(livingTarget, enchantmentLevel, baseDamage);
        float totalDamage = 0;
        if (armorValue > 0 || toughnessValue > 0) {
            totalDamage = baseDamage + extraDamage;
        }

        float extraDamageFromArmor = 0;
        if (enchantmentLevel > 10) {
            int levelsAbove10 = enchantmentLevel - 10;
            extraDamageFromArmor = (livingTarget.getArmorValue() + (float) livingTarget.getAttributeValue(Attributes.ARMOR_TOUGHNESS)) * 0.1f * levelsAbove10;
        }

        float extraDamageFromHealth = 0;
        if (enchantmentLevel >= 12) {
            int countTwoLevels = enchantmentLevel / 2;
            extraDamageFromHealth = livingTarget.getHealth() * 0.1f * countTwoLevels;
        }

        totalDamage += extraDamageFromArmor + extraDamageFromHealth;

        float totalExtraDamage = 0;
        float damageAfterArmorProtection = damageAfterArmorProtection(livingTarget.getArmorSlots(), totalDamage);
        float extraDamageAfterArmorProtection = totalDamage - damageAfterArmorProtection;
        if (enchantmentLevel == 20) {
            totalExtraDamage += totalDamage + extraDamageAfterArmorProtection;
        }

        if (enchantmentLevel >= 10) {
            if (attacker instanceof Player playerAttacker) {
                livingTarget.hurt(DamageSource.playerAttack(playerAttacker), totalDamage);
            } else {
                livingTarget.hurt(DamageSource.mobAttack(attacker), totalDamage);
            }
            if (enchantmentLevel == 20) {
                if (attacker instanceof Player playerAttacker) {
                    livingTarget.hurt(DamageSource.playerAttack(playerAttacker).setMagic(), totalExtraDamage);
                } else {
                    livingTarget.hurt(DamageSource.mobAttack(attacker).setMagic(), totalExtraDamage);
                }
            }
        } else {
        if (attacker instanceof Player playerAttacker) {
            livingTarget.hurt(DamageSource.playerAttack(playerAttacker), totalDamage);
        } else {
            livingTarget.hurt(DamageSource.mobAttack(attacker), totalDamage);
        }
    }

        if (enchantmentLevel >= 2) {
            applySunderingEffectToTarget(livingTarget, enchantmentLevel);
        }
    }

    private float getAttackDamage(ItemStack weapon, EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> attributeModifiers = weapon.getAttributeModifiers(slot);
        Collection<AttributeModifier> attackDamageModifiers = attributeModifiers.get(Attributes.ATTACK_DAMAGE);
        float baseDamage = 0;
        for (AttributeModifier mod : attackDamageModifiers) {
            if (mod.getOperation() == AttributeModifier.Operation.ADDITION) {
                baseDamage += (float) mod.getAmount();
            }
        }
        return baseDamage;
    }

    public float calculateExtraDamageBasedOnArmor(LivingEntity target, int enchantmentLevel, float baseDamage) {
        float armorValue = target.getArmorValue();
        float toughnessValue = (float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
        float arpArmorValue = 0;
        float arpToughnessValue = 0;

        if (enchantmentLevel < 10) {
            arpArmorValue = armorValue * (enchantmentLevel * 0.1f);
            arpToughnessValue = toughnessValue * (enchantmentLevel * 0.1f);
        }

        if (enchantmentLevel > 10) {
            arpArmorValue = armorValue;
            arpToughnessValue = toughnessValue;
            if (enchantmentLevel >= 12) {
                int countTwoLevels = enchantmentLevel / 2;
                baseDamage += target.getHealth() * 0.1f * countTwoLevels;
            }
        }

        float damageAfterArmorReduction = damageAfterArmorReduction(arpArmorValue, arpToughnessValue, baseDamage);

        return baseDamage - damageAfterArmorReduction;
    }

    public float damageAfterArmorReduction(float armorValue, float toughnessValue, float baseDamage) {
        float damageAfterArmorReduction;
        if (baseDamage <= 1.6 * armorValue + 0.2 * toughnessValue) {
            damageAfterArmorReduction = (1 / (6.25f * toughnessValue + 50f)) * baseDamage * baseDamage
                    + (1f - armorValue / 25f) * baseDamage;
        } else {
            damageAfterArmorReduction = (1f - armorValue / 125f) * baseDamage;
        }
        return damageAfterArmorReduction;
    }

    public float damageAfterArmorProtection(Iterable<ItemStack> armorItems, float baseDamage) {
        float damageAfterArmorProtection = 0;
        for (ItemStack armorItem : armorItems) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armorItem);
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof ProtectionEnchantment) {
                    int protectLevel = entry.getValue();

                    damageAfterArmorProtection += baseDamage * (10 / (protectLevel + 10.0f));
                }
            }
        }
        return damageAfterArmorProtection;
    }

    private void applySunderingEffectToTarget(LivingEntity target, int level) {
        MobEffectInstance effectInstance = getSunderingEffectInstance(level);
        if (effectInstance != null) {
            target.addEffect(effectInstance);
        }
    }

    public static MobEffectInstance getSunderingEffectInstance(int enchantmentLevel) {
        if (enchantmentLevel < 2) {
            return null;
        }

        String sunderingEffectConfig = AshOfSinConfig.SUNDERING_EFFECT_STRING.get();
        String[] parts = sunderingEffectConfig.split(",");
        if (parts.length == 3) {
            try {
                String effectId = parts[0];
                int baseDuration = Integer.parseInt(parts[1]);
                int baseLevel = Integer.parseInt(parts[2]);

                int effectLevel = 4;
                int durationInSeconds = 42;


                if (enchantmentLevel <= 10) {
                    effectLevel = baseLevel + (enchantmentLevel / 2);
                    durationInSeconds = baseDuration + 7 * (enchantmentLevel / 2);
                }

                MobEffect mobEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectId));
                if (mobEffect != null) {
                    return new MobEffectInstance(mobEffect, durationInSeconds * 20, effectLevel);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Error Sundering Effect Configuration: " + sunderingEffectConfig);
            }
        }
        return null;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }
}