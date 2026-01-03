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
    public static final ForgeConfigSpec ENTITY_ANTI_EFFECT_CONFIG;
    public static ForgeConfigSpec.BooleanValue ANTI_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_EFFECT_ENTITY;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_EFFECT;
    public static ForgeConfigSpec.BooleanValue HEAL;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ON = builder.comment("Anti On")
                .comment("Enable Anti-Effect Entity")
                .define("Anti On", true);
        ANTI_EFFECT_ENTITY = builder.comment("Anti-Effect Entity")
                .defineList("Anti-Effect Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "minecraft:warden",
                                "cataclysm:ender_guardian",
                                "cataclysm:ignis",
                                "cataclysm:netherite_monstrosity",
                                "cataclysm:the_harbinger",
                                "cataclysm:the_leviathan",
                                "cataclysm:ancient_ancient_remnant",
                                "cataclysm:ancient_remnant",
                                "cataclysm:maledictus",
                                "aquamirae:captain_cornelia",
                                "block_factorys_bosses:infernal_dragon",
                                "block_factorys_bosses:sandworm",
                                "block_factorys_bosses:yeti",
                                "block_factorys_bosses:underworld_knight",
                                "fdbosses:malkuth",
                                "fdbosses:chesed",
                                "nightfall_invade:arterius",
                                "alexsmobs:void_worm",
                                "mowziesmobs:ferrous_wroughtnaut",
                                "mowziesmobs:frostmaw",
                                "mowziesmobs:umvuthi",
                                "bosses_of_mass_destruction:gauntlet",
                                "bosses_of_mass_destruction:lich",
                                "bosses_of_mass_destruction:obsidilith",
                                "bosses_of_mass_destruction:void_blossom",
                                "ba_bt:land_golem",
                                "ba_bt:ocean_golem",
                                "ba_bt:core_golem",
                                "ba_bt:nether_golem",
                                "ba_bt:sky_golem",
                                "ba_bt:end_golem",
                                "dummmmmmy:target_dummy"
                        )),
                        o -> o instanceof String);
        List<String> antiEffects = List.of(
                "aquamirae:health_decrease"
        );
        ANTI_EFFECT = builder.comment("Anti-Effect ID")
                .defineList("Anti-Effect ID",
                        antiEffects,
                        o -> o instanceof String);
        HEAL = builder.comment("Heal On")
                .comment("Enable Restored to the maximum health.")
                .define("Heal On", true);
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