package com.kamikaguya.ash_of_sin.world.item;

import java.util.function.Supplier;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("deprecation")
public enum AshOfSinItemTier implements Tier {
    CRESCENT(4, 10000, 1.0F, 14.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_BLOCK);
    }), VENUZDONOA(4, 100000000, 39.0F, 38.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_BLOCK);
    }), DARK_MOON_GREATSWORD(4, 10000, 39.0F, 38.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_BLOCK);
    }), CARIAN_KNIGHTS_SWORD(4, 10000, 39.0F, 17.0F, 39, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    }), EA(4, 100000000, 39.0F, 16.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_BLOCK);
    }), CHAOS_MELEE_BLADE(4, 10000, 39.0F, 17.0F, 39, () -> {
        return Ingredient.of(Items.AMETHYST_SHARD);
    }), SHIKAMA_DOJI(4, 10000, 39.0F, 16.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), FLAME_KATANA_CARAVELLA(4, 10000, 39.0F, 13.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), ELUCIDATOR(4, 10000, 20.0F, 17.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), DARK_REPULSER(4, 10000, 20.0F, 16.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), LAMBENT_LIGHT(4, 10000, 20.0F, 16.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), FROSTMOURNE(4, 10000, 20.0F, 24.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), CRUCIBLE(4, 10000, 20.0F, 17.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), MELT_SWORD(4, 10000, 20.0F, 17.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), GOD_EATER(4, 10000, 20.0F, 17.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), ENDER_CHASER(4, 10000, 20.0F, 17.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), CALAMITY_BLADE(4, 10000, 20.0F, 16.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), CALAMITY_BLADE_THIN(4, 10000, 20.0F, 15.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), CALAMITY_SCYTHE(4, 10000, 20.0F, 16.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), DESPAIR_SCYTHE(4, 10000, 20.0F, 16.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), SCULK_AXE(4, 3000, 20.0F, 10.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), SCULK_SWORD(4, 3000, 20.0F, 9.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }), SCULK_GREATSWORD(4, 3000, 20.0F, 17.0F, 39, () -> {
        return Ingredient.of(Items.NETHERITE_INGOT);
    });
    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    private AshOfSinItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterialIn);
    }

    public int getUses() {
        return this.maxUses;
    }

    public float getSpeed() {
        return this.efficiency;
    }

    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    public int getLevel() {
        return this.harvestLevel;
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
