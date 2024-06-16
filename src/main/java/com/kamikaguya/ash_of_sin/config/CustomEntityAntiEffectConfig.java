package com.kamikaguya.ash_of_sin.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomEntityAntiEffectConfig {
    private static ForgeConfigSpec ENTITY_ANTI_EFFECT_CONFIG;
    public static ForgeConfigSpec.BooleanValue ANTI_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_EFFECT_ENTITY;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_EFFECT;
    private final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ON = builder.comment("Anti On")
                .comment("Enable Anti-Effect Entity")
                .define("Anti On", true);
        ANTI_EFFECT_ENTITY = builder.comment("Anti-Effect Entity")
                .defineList("Anti-Effect Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "ash_of_sin:kamikaguya",
                                "ash_of_sin:doppelganger",
                                "ash_of_sin:assassin",
                                "dummmmmmy:target_dummy",
                                "goblinsanddungeons:goblin_king",
                                "simple_mobs:corrupted_ogre",
                                "simple_mobs:knight_4",
                                "simple_mobs:nine_tails",
                                "simple_mobs:skeletonlord",
                                "simple_mobs:sentinel_knight",
                                "simple_mobs:fire_giant",
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "cataclysm:ender_guardian",
                                "simple_mobs:notch_boss",
                                "simple_mobs:elemental_deity",
                                "simple_mobs:herobrine",
                                "simple_mobs:dragon_lord",
                                "witherstormmod:wither_storm",
                                "witherstormmod:command_block",
                                "simple_mobs:mjolnir_thrown",
                                "bloodandmadness:father_gascoigne",
                                "bloodandmadness:gascoigne_beast",
                                "bloodandmadness:micolash",
                                "mowziesmobs:ferrous_wroughtnaut",
                                "epiceldenring:godfrey",
                                "epiceldenring:godfreyphasetwo",
                                "epiceldenring:radahn",
                                "epiceldenring:malenia",
                                "epiceldenring:maliketh",
                                "simple_mobs:ent",
                                "simple_mobs:samurai_4",
                                "simple_mobs:samurai_5",
                                "simple_mobs:martian",
                                "simple_mobs:gael",
                                "simple_mobs:morgott",
                                "mowziesmobs:frostmaw",
                                "mowziesmobs:umvuthi",
                                "mowziesmobs:naga",
                                "ba_bt:land_golem",
                                "ba_bt:ocean_golem",
                                "aquamirae:captain_cornelia",
                                "graveyard:lich",
                                "whisperwoods:hirschgeist",
                                "irons_spellbooks:dead_king",
                                "darkersouls:nameless_king",
                                "cataclysm:ignis",
                                "cataclysm:netherite_monstrosity",
                                "simple_mobs:twins_stone",
                                "simple_mobs:first_twin",
                                "simple_mobs:second_twin",
                                "alexsmobs:void_worm",
                                "alexsmobs:void_worm_part",
                                "bosses_of_mass_destruction:gauntlet",
                                "bosses_of_mass_destruction:lich",
                                "bosses_of_mass_destruction:obsidilith",
                                "bosses_of_mass_destruction:void_blossom",
                                "blue_skies:arachnarch",
                                "blue_skies:alchemist",
                                "blue_skies:summoner",
                                "blue_skies:starlit_crusher",
                                "illageandspillage:spiritcaller",
                                "illageandspillage:magispeller",
                                "conjurer_illager:conjurer",
                                "iceandfire:ice_dragon",
                                "iceandfire:fire_dragon",
                                "iceandfire:lightning_dragon",
                                "iceandfire:dread_knight",
                                "iceandfire:dread_lich",
                                "dwmg:hmag_zombie_girl",
                                "dwmg:hmag_husk_girl",
                                "dwmg:hmag_drowned_girl",
                                "dwmg:hmag_skeleton_girl",
                                "dwmg:hmag_wither_skeleton_girl",
                                "dwmg:hmag_stray_girl",
                                "dwmg:hmag_creeper_girl",
                                "dwmg:hmag_creeper_girl",
                                "hmag:ender_executor",
                                "dwmg:hmag_ender_executor",
                                "dwmg:hmag_kobold",
                                "dwmg:hmag_melty_monster",
                                "dwmg:hmag_cursed_doll",
                                "dwmg:hmag_jack_frost",
                                "dwmg:hmag_hornet",
                                "dwmg:hmag_dullahan",
                                "hmag:banshee",
                                "dwmg:hmag_banshee",
                                "dwmg:hmag_alraune",
                                "hmag:ghastly_seeker",
                                "dwmg:hmag_ghastly_seeker",
                                "dwmg:hmag_redcap",
                                "dwmg:hmag_slime_girl",
                                "hmag:crimson_slaughterer",
                                "dwmg:hmag_crimson_slaughterer",
                                "dwmg:hmag_snow_canine",
                                "dwmg:hmag_harpy",
                                "hmag:necrotic_reaper",
                                "dwmg:hmag_necrotic_reaper",
                                "hmag:dodomeki",
                                "dwmg:hmag_dodomeki",
                                "hmag:imp",
                                "dwmg:hmag_imp",
                                "dwmg:hmag_glaryad",
                                "wildbackport:warden"
                        )),
                        o -> o instanceof String);
        List<String> antiEffects = List.of(
                "magistuarmory:laceration", "aquamirae:health_decrease"
        );
        ANTI_EFFECT = builder.comment("Anti-Effect ID")
                .defineList("Anti-Effect ID",
                        antiEffects,
                        o -> o instanceof String);
        ENTITY_ANTI_EFFECT_CONFIG = builder.build();
    }

    public CustomEntityAntiEffectConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_entity_anti_effect.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default custom entity anti effect config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ENTITY_ANTI_EFFECT_CONFIG.setConfig(fileConfig);
    }
}
