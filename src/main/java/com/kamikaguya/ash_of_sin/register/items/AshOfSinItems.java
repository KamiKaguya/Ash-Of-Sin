package com.kamikaguya.ash_of_sin.register.items;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AshOfSinItems {
    private static final DeferredRegister<Item> registry;
    public static final RegistryObject<Item> MIRROR_OF_THE_DARK_NIGHT;
    public static final RegistryObject<Item> CRESCENT;
    public static final RegistryObject<Item> CRESCENT_DAGGER;
    public static final RegistryObject<Item> VENUZDONOA;
    public static final RegistryObject<Item> DARK_MOON_GREATSWORD;
    public static final RegistryObject<Item> CARIAN_KNIGHTS_SWORD;
    public static final RegistryObject<Item> EA;
    public static final RegistryObject<Item> CHAOS_MELEE_BLADE;
    public static final RegistryObject<Item> SUB_CRAVEN_BOW;
    public static final RegistryObject<Item> SHIKAMA_DOJI;
    public static final RegistryObject<Item> FLAME_KATANA_CARAVELLA;
    public static final RegistryObject<Item> SOUL_OF_THE_KING_FIRE;
    public static final RegistryObject<Item> SOUL_OF_THE_KING_LIGHTNING;
    public static final RegistryObject<Item> YAMATO;
    public static final RegistryObject<Item> YAMATO_KATANA;
    public static final RegistryObject<Item> SANGUINE_GAZE_UNION;
    public static final RegistryObject<Item> MURGLEIS;
    public static final RegistryObject<Item> FALLING_FLOWER;
    public static final RegistryObject<Item> MURASAME;
    public static final RegistryObject<Item> MURASAME_KATANA;
    public static final RegistryObject<Item> ABYSS_PREDATOR;

    public static final RegistryObject<Item> CRESCENT_SHEATH;
    public static final RegistryObject<Item> FLAME_KATANA_CARAVELLA_SHEATH;
    public static final RegistryObject<Item> YAMATO_SHEATH;
    public static final RegistryObject<Item> MURASAME_SHEATH;

    public static final RegistryObject<Item> ELUCIDATOR;
    public static final RegistryObject<Item> DARK_REPULSER;
    public static final RegistryObject<Item> LAMBENT_LIGHT;
    public static final RegistryObject<Item> FROSTMOURNE;
    public static final RegistryObject<Item> CRUCIBLE;
    public static final RegistryObject<Item> MELT_SWORD;
    public static final RegistryObject<Item> GOD_EATER;
    public static final RegistryObject<Item> CALAMITY_BLADE;
    public static final RegistryObject<Item> CALAMITY_BLADE_THIN;
    public static final RegistryObject<Item> CALAMITY_SCYTHE;
    public static final RegistryObject<Item> DESPAIR_SCYTHE;
    public static final RegistryObject<Item> SCULK_AXE;
    public static final RegistryObject<Item> SCULK_LONGSWORD;
    public static final RegistryObject<Item> SCULK_GREATSWORD;
    public static final RegistryObject<Item> SCULK_CLEAVER;
    public static final RegistryObject<Item> SCULK_SCYTHE;
    public static final RegistryObject<Item> SCULK_SWORD;

    public AshOfSinItems() {
    }

    public static void register(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ITEMS, AshOfSin.MODID);

        MIRROR_OF_THE_DARK_NIGHT = registry.register("mirror_of_the_dark_night", () ->
                new MirrorOfTheDarkNightItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );

        CRESCENT = registry.register("crescent", () ->
                new CrescentItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        CRESCENT_DAGGER = registry.register("crescent_dagger", () ->
                new CrescentDaggerItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        VENUZDONOA = registry.register("venuzdonoa", () ->
                new VenuzdonoaItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        DARK_MOON_GREATSWORD = registry.register("dark_moon_greatsword", () ->
                new DarkMoonGreatswordItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        CARIAN_KNIGHTS_SWORD = registry.register("carian_knights_sword", () ->
                new CarianKnightsSwordItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        EA = registry.register("ea", () ->
                new EaItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        CHAOS_MELEE_BLADE = registry.register("chaos_melee_blade", () ->
                new ChaosMeleeBladeItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        SUB_CRAVEN_BOW = registry.register("sub_craven_bow", () ->
                new SubCravenBowItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        SHIKAMA_DOJI = registry.register("shikama_doji", () ->
                new ShikamaDojiItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        FLAME_KATANA_CARAVELLA = registry.register("flame_katana_caravella", () ->
                new FlameKatanaCaravellaItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        SOUL_OF_THE_KING_FIRE = registry.register("soul_of_the_king_fire", () ->
                new SoulOfTheKingFireItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        SOUL_OF_THE_KING_LIGHTNING = registry.register("soul_of_the_king_lightning", () ->
                new SoulOfTheKingLightningItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        YAMATO = registry.register("yamato", () ->
                new YamatoItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant()))
        ;
        YAMATO_KATANA = registry.register("yamato_katana", () ->
                new YamatoKatanaItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        SANGUINE_GAZE_UNION = registry.register("sanguine_gaze_union", () ->
                new SanguineGazeUnionItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        MURGLEIS = registry.register("murgleis", () ->
                new MurgleisItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        FALLING_FLOWER = registry.register("falling_flower", () ->
                new FallingFlowerItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        MURASAME = registry.register("murasame", () ->
                new MurasameItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        MURASAME_KATANA = registry.register("murasame_katana", () ->
                new MurasameKatanaItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        ABYSS_PREDATOR = registry.register("abyss_predator", () ->
                new AbyssPredatorItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );

        CRESCENT_SHEATH = registry.register("crescent_sheath", () ->
                new Item(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        FLAME_KATANA_CARAVELLA_SHEATH = registry.register("flame_katana_caravella_sheath", () ->
                new Item(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        YAMATO_SHEATH = registry.register("yamato_sheath", () ->
                new Item(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        MURASAME_SHEATH = registry.register("murasame_sheath", () ->
                new Item(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );

        ELUCIDATOR = registry.register("elucidator", () ->
                new ElucidatorItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        DARK_REPULSER = registry.register("dark_repulser", () ->
                new DarkRepulserItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        LAMBENT_LIGHT = registry.register("lambent_light", () ->
                new LambentLightItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        FROSTMOURNE = registry.register("frostmourne", () ->
                new FrostmourneItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        CRUCIBLE = registry.register("crucible", () ->
                new CrucibleItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        MELT_SWORD = registry.register("melt_sword", () ->
                new MeltSwordItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        GOD_EATER = registry.register("god_eater", () ->
                new GodEaterItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        CALAMITY_BLADE = registry.register("calamity_blade", () ->
                new CalamityBladeItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        CALAMITY_BLADE_THIN = registry.register("calamity_blade_thin", () ->
                new CalamityBladeThinItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        CALAMITY_SCYTHE = registry.register("calamity_scythe", () ->
                new CalamityScytheItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        DESPAIR_SCYTHE = registry.register("despair_scythe", () ->
                new DespairScytheItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.EPIC).fireResistant())
        );
        SCULK_AXE = registry.register("sculk_axe", () ->
                new SculkAxeItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.RARE).fireResistant())
        );
        SCULK_LONGSWORD = registry.register("sculk_longsword", () ->
                new SculkLongSwordItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.RARE).fireResistant())
        );
        SCULK_GREATSWORD = registry.register("sculk_greatsword", () ->
                new SculkGreatswordItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.RARE).fireResistant())
        );
        SCULK_CLEAVER = registry.register("sculk_cleaver", () ->
                new SculkCleaverItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.RARE).fireResistant())
        );
        SCULK_SCYTHE = registry.register("sculk_scythe", () ->
                new SculkScytheItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.RARE).fireResistant())
        );
        SCULK_SWORD = registry.register("sculk_sword", () ->
                new SculkSwordItem(new Item.Properties().tab(AshOfSinCreativeModeTab.WEAPONS).rarity(Rarity.RARE).fireResistant())
        );
    }
}