package com.kamikaguya.ash_of_sin.register.items;

import com.kamikaguya.ash_of_sin.item.*;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AshOfSinItemRegistry {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AshOfSin.MODID);
    
    public static final RegistryObject<Item> MIRROR_OF_THE_DARK_NIGHT = ITEMS.register("mirror_of_the_dark_night", () ->
            new MirrorOfTheDarkNightItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> CRESCENT = ITEMS.register("crescent", () ->
            new CrescentItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> CRESCENT_DAGGER = ITEMS.register("crescent_dagger", () ->
            new CrescentDaggerItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> VENUZDONOA = ITEMS.register("venuzdonoa", () ->
            new VenuzdonoaItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> DARK_MOON_GREATSWORD = ITEMS.register("dark_moon_greatsword", () ->
            new DarkMoonGreatswordItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> CARIAN_KNIGHTS_SWORD = ITEMS.register("carian_knights_sword", () ->
            new CarianKnightsSwordItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> EA = ITEMS.register("ea", () ->
            new EaItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> CHAOS_MELEE_BLADE = ITEMS.register("chaos_melee_blade", () ->
            new ChaosMeleeBladeItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> SUB_CRAVEN_BOW = ITEMS.register("sub_craven_bow", () ->
            new SubCravenBowItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> SHIKAMA_DOJI = ITEMS.register("shikama_doji", () ->
            new ShikamaDojiItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> FLAME_KATANA_CARAVELLA = ITEMS.register("flame_katana_caravella", () ->
            new FlameKatanaCaravellaItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> SOUL_OF_THE_KING_FIRE = ITEMS.register("soul_of_the_king_fire", () ->
            new SoulOfTheKingFireItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> SOUL_OF_THE_KING_LIGHTNING = ITEMS.register("soul_of_the_king_lightning", () ->
            new SoulOfTheKingLightningItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> YAMATO = ITEMS.register("yamato", () ->
            new YamatoItem(new Item.Properties().fireResistant()))
            ;
    public static final RegistryObject<Item> YAMATO_KATANA = ITEMS.register("yamato_katana", () ->
            new YamatoKatanaItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> SANGUINE_GAZE_UNION = ITEMS.register("sanguine_gaze_union", () ->
            new SanguineGazeUnionItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> MURGLEIS = ITEMS.register("murgleis", () ->
            new MurgleisItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> FALLING_FLOWER = ITEMS.register("falling_flower", () ->
            new FallingFlowerItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> MURASAME = ITEMS.register("murasame", () ->
            new MurasameItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> MURASAME_KATANA = ITEMS.register("murasame_katana", () ->
            new MurasameKatanaItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> ABYSS_PREDATOR = ITEMS.register("abyss_predator", () ->
            new AbyssPredatorItem(new Item.Properties().fireResistant())
    );

    public static final RegistryObject<Item> CRESCENT_SHEATH = ITEMS.register("crescent_sheath", () ->
            new Item(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> FLAME_KATANA_CARAVELLA_SHEATH = ITEMS.register("flame_katana_caravella_sheath", () ->
            new Item(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> YAMATO_SHEATH = ITEMS.register("yamato_sheath", () ->
            new Item(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> MURASAME_SHEATH = ITEMS.register("murasame_sheath", () ->
            new Item(new Item.Properties().fireResistant())
    );

    public static final RegistryObject<Item> ELUCIDATOR = ITEMS.register("elucidator", () ->
            new ElucidatorItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> DARK_REPULSER = ITEMS.register("dark_repulser", () ->
            new DarkRepulserItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> LAMBENT_LIGHT = ITEMS.register("lambent_light", () ->
            new LambentLightItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> FROSTMOURNE = ITEMS.register("frostmourne", () ->
            new FrostmourneItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> CRUCIBLE = ITEMS.register("crucible", () ->
            new CrucibleItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> MELT_SWORD = ITEMS.register("melt_sword", () ->
            new MeltSwordItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> GOD_EATER = ITEMS.register("god_eater", () ->
            new GodEaterItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> CALAMITY_BLADE = ITEMS.register("calamity_blade", () ->
            new CalamityBladeItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> CALAMITY_BLADE_THIN = ITEMS.register("calamity_blade_thin", () ->
            new CalamityBladeThinItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> CALAMITY_SCYTHE = ITEMS.register("calamity_scythe", () ->
            new CalamityScytheItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> DESPAIR_SCYTHE = ITEMS.register("despair_scythe", () ->
            new DespairScytheItem(new Item.Properties().fireResistant())
    );

    public static final RegistryObject<Item> SCULK_CLEAVER = ITEMS.register("sculk_cleaver", () ->
            new SculkCleaverItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> SCULK_SCYTHE = ITEMS.register("sculk_scythe", () ->
            new SculkScytheItem(new Item.Properties().fireResistant())
    );
    public static final RegistryObject<Item> SCULK_SWORD = ITEMS.register("sculk_sword", () ->
            new SculkSwordItem(new Item.Properties().fireResistant())
    );

    public static final DeferredRegister<CreativeModeTab> ASH_OF_SIN_WEAPONS_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AshOfSin.MODID);
    public static final RegistryObject<CreativeModeTab> DEFAULT_TAB = ASH_OF_SIN_WEAPONS_TAB.register("ash_of_sin_weapons",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ash_of_sin.weapons"))
                    .icon(() -> new ItemStack(MIRROR_OF_THE_DARK_NIGHT.get()))
                    .displayItems((parameters, tabData) -> {
                        tabData.accept(MIRROR_OF_THE_DARK_NIGHT.get());
                        tabData.accept(CRESCENT.get());
                        tabData.accept(CRESCENT_DAGGER.get());
                        tabData.accept(VENUZDONOA.get());
                        tabData.accept(DARK_MOON_GREATSWORD.get());
                        tabData.accept(CARIAN_KNIGHTS_SWORD.get());
                        tabData.accept(EA.get());
                        tabData.accept(CHAOS_MELEE_BLADE.get());
                        tabData.accept(SUB_CRAVEN_BOW.get());
                        tabData.accept(SHIKAMA_DOJI.get());
                        tabData.accept(FLAME_KATANA_CARAVELLA.get());
                        tabData.accept(SOUL_OF_THE_KING_FIRE.get());
                        tabData.accept(SOUL_OF_THE_KING_LIGHTNING.get());
                        tabData.accept(YAMATO.get());
                        tabData.accept(YAMATO_KATANA.get());
                        tabData.accept(SANGUINE_GAZE_UNION.get());
                        tabData.accept(MURGLEIS.get());
                        tabData.accept(FALLING_FLOWER.get());
                        tabData.accept(MURASAME.get());
                        tabData.accept(MURASAME_KATANA.get());
                        tabData.accept(ABYSS_PREDATOR.get());
                        tabData.accept(CRESCENT_SHEATH.get());
                        tabData.accept(FLAME_KATANA_CARAVELLA_SHEATH.get());
                        tabData.accept(YAMATO_SHEATH.get());
                        tabData.accept(MURASAME_SHEATH.get());
                        tabData.accept(ELUCIDATOR.get());
                        tabData.accept(DARK_REPULSER.get());
                        tabData.accept(LAMBENT_LIGHT.get());
                        tabData.accept(FROSTMOURNE.get());
                        tabData.accept(CRUCIBLE.get());
                        tabData.accept(MELT_SWORD.get());
                        tabData.accept(GOD_EATER.get());
                        tabData.accept(CALAMITY_BLADE.get());
                        tabData.accept(CALAMITY_BLADE_THIN.get());
                        tabData.accept(CALAMITY_SCYTHE.get());
                        tabData.accept(DESPAIR_SCYTHE.get());
                        tabData.accept(SCULK_CLEAVER.get());
                        tabData.accept(SCULK_SCYTHE.get());
                        tabData.accept(SCULK_SWORD.get());
                    }).build());
}