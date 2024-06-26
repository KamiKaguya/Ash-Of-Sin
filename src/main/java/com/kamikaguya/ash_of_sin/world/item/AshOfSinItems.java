package com.kamikaguya.ash_of_sin.world.item;

import com.google.common.base.Supplier;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AshOfSinItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AshOfSin.MODID);

    public static final RegistryObject<Item> ASH_OF_SIN = register("ash_of_sin",
            () -> new Item(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).fireResistant()));

    public static final RegistryObject<Item> MIRROR_OF_THE_DARK_NIGHT = ITEMS.register("mirror_of_the_dark_night", () -> new MirrorOfTheDarkNightItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CRESCENT = ITEMS.register("crescent", () -> new CrescentItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CRESCENT_SHEATH = ITEMS.register("crescent_sheath", () -> new Item(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> VENUZDONOA = ITEMS.register("venuzdonoa", () -> new VenuzdonoaItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> DARK_MOON_GREATSWORD = ITEMS.register("dark_moon_greatsword", () -> new DarkMoonGreatswordItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CARIAN_KNIGHTS_SWORD = ITEMS.register("carian_knights_sword", () -> new CarianKnightsSwordItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> EA = ITEMS.register("ea", () -> new EaItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CHAOS_MELEE_BLADE = ITEMS.register("chaos_melee_blade", () -> new ChaosMeleeBladeItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> SUB_CRAVEN_BOW = ITEMS.register("sub_craven_bow", () -> new SubCravenBowItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> SHIKAMA_DOJI = ITEMS.register("shikama_doji", () -> new ShikamaDojiItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> FLAME_KATANA_CARAVELLA = ITEMS.register("flame_katana_caravella", () -> new FlameKatanaCaravellaItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> FLAME_KATANA_CARAVELLA_SHEATH = ITEMS.register("flame_katana_caravella_sheath", () -> new Item(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> SOUL_OF_THE_KING_FIRE = ITEMS.register("soul_of_the_king_fire", () -> new SoulOfTheKingFireItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> SOUL_OF_THE_KING_LIGHTNING = ITEMS.register("soul_of_the_king_lightning", () -> new SoulOfTheKingLightningItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> ELUCIDATOR = ITEMS.register("elucidator", () -> new ElucidatorItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> DARK_REPULSER = ITEMS.register("dark_repulser", () -> new DarkRepulserItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> LAMBENT_LIGHT = ITEMS.register("lambent_light", () -> new LambentLightItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> FROSTMOURNE = ITEMS.register("frostmourne", () -> new FrostmourneItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CRUCIBLE = ITEMS.register("crucible", () -> new CrucibleItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> MELT_SWORD = ITEMS.register("melt_sword", () -> new MeltSwordItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> GOD_EATER = ITEMS.register("god_eater", () -> new GodEaterItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CALAMITY_BLADE = ITEMS.register("calamity_blade", () -> new CalamityBladeItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CALAMITY_BLADE_THIN = ITEMS.register("calamity_blade_thin", () -> new CalamityBladeThinItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> CALAMITY_SCYTHE = ITEMS.register("calamity_scythe", () -> new CalamityScytheItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> DESPAIR_SCYTHE = ITEMS.register("despair_scythe", () -> new DespairScytheItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.EPIC).fireResistant()));

    public static final RegistryObject<Item> SCULK_AXE = ITEMS.register("sculk_axe", () -> new SculkAxeItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.RARE).fireResistant()));

    public static final RegistryObject<Item> SCULK_SWORD = ITEMS.register("sculk_sword", () -> new SculkSwordItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.RARE).fireResistant()));

    public static final RegistryObject<Item> SCULK_GREATSWORD = ITEMS.register("sculk_greatsword", () -> new SculkGreatswordItem(new Item.Properties().tab(AshOfSinCreativeModeTabs.ITEMS).rarity(Rarity.RARE).fireResistant()));

    public static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}