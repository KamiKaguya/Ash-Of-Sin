package com.kamikaguya.ash_of_sin.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SubTankEnchantment extends Enchantment {

    public SubTankEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 10;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) * enchantmentLevel;
    }

    @Override
    public int getMaxLevel() {
        return super.getMaxLevel();
    }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        if (other instanceof MainTankEnchantment) {
            return false;
        }
        return super.checkCompatibility(other);
    }

    @Override
    public Component getFullname(int level) {
        return ((MutableComponent) super.getFullname(level)).withStyle(ChatFormatting.BLUE);
    }
}