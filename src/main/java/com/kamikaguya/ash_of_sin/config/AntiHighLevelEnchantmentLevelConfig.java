package com.kamikaguya.ash_of_sin.config;

public class AntiHighLevelEnchantmentLevelConfig {
    public final String enchantment;
    public final int level;
    public AntiHighLevelEnchantmentLevelConfig(String enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public String getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }
}