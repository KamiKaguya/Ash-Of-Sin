package com.kamikaguya.ash_of_sin.world.enchantment;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

import java.util.Collection;
import java.util.Map;

public class VenuzdonoaEnchantent extends Enchantment {

    public VenuzdonoaEnchantent(Enchantment.Rarity rarity, EnchantmentCategory category) {
        super(rarity, category, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 0;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }

    @Override
    public Component getFullname(int level) {
        return ((MutableComponent) super.getFullname(level)).withStyle(ChatFormatting.DARK_RED);
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int enchantmentLevel) {
        if (enchantmentLevel > 1 || !(target instanceof LivingEntity livingTarget) || !(attacker instanceof ServerPlayer PlayerAttacker)) {
            return;
        }

        ItemStack mainHandWeapon = PlayerAttacker.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack offHandWeapon = PlayerAttacker.getItemBySlot(EquipmentSlot.OFFHAND);

        EquipmentSlot usedSlot = null;
        if (EnchantmentHelper.getItemEnchantmentLevel(this, mainHandWeapon) > 0) {
            usedSlot = EquipmentSlot.MAINHAND;
        } else if (EnchantmentHelper.getItemEnchantmentLevel(this, offHandWeapon) > 0) {
            usedSlot = EquipmentSlot.OFFHAND;
        }

        if (usedSlot == null) {
            return;
        }

        ItemStack weapon = PlayerAttacker.getItemBySlot(usedSlot);

        float baseDamage = getAttackDamage(weapon, usedSlot);

        float armorValue = livingTarget.getArmorValue();
        float toughnessValue = (float) livingTarget.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
        float extraDamageFromArmor = armorValue + toughnessValue;
        float extraDamageFromHealth = livingTarget.getMaxHealth();

        baseDamage += extraDamageFromArmor + extraDamageFromHealth;

        float totalDamage = 0;
        float damageAfterArmorProtection = damageAfterArmorProtection(livingTarget.getArmorSlots(), totalDamage);
        float extraDamageAfterArmorProtection = baseDamage - damageAfterArmorProtection;
        totalDamage += baseDamage + extraDamageAfterArmorProtection;
        float venuzdonoaDamage = totalDamage * 7.0F;

        livingTarget.hurt(DamageSource.playerAttack(PlayerAttacker).setMagic(), venuzdonoaDamage);
    }

    public float getAttackDamage(ItemStack weapon, EquipmentSlot slot) {
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

    public float damageAfterArmorProtection(Iterable<ItemStack> armorItems, float baseDamage) {
        float damageAfterArmorProtection = 0;
        for (ItemStack armorItem : armorItems) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armorItem);
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof ProtectionEnchantment) {
                    int protectLevel = entry.getValue();

                    damageAfterArmorProtection += baseDamage * (10 / ((protectLevel + 10.0f) / 2));
                }
            }
        }
        return damageAfterArmorProtection;
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