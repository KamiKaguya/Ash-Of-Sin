package com.kamikaguya.ash_of_sin.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class BetterAIConfig {
    public static final ForgeConfigSpec BETTER_AI_CONFIG;
    public static ForgeConfigSpec.BooleanValue BETTER_AI_ON;
    public static ForgeConfigSpec.ConfigValue<Integer> BATTLE_LIMIT;
    public static ForgeConfigSpec.DoubleValue TRACKING_RANGE;
    public static ForgeConfigSpec.BooleanValue EXCLUSION_ENABLED;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> EXCLUSION_LIST;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> NEUTRAL_MONSTER_LIST;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> FRIENDLY_LIST;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Better AI Settings");
        BETTER_AI_ON = builder
                .comment("Enable better AI system (based on hate and attack limit).")
                .define("betterAIOn", true);

        BATTLE_LIMIT = builder
                .comment("Maximum number of mobs that can simultaneously attack a player.")
                .defineInRange("battleLimit", 3, 1, 39);

        TRACKING_RANGE = builder
                .comment("Distance (in blocks) within which mobs consider players as candidates.")
                .defineInRange("trackingRange", 32.0, 8.0, 128.0);

        EXCLUSION_ENABLED = builder
                .comment("Enable exclusion list for inactive mob behavior (bosses and custom entities).")
                .define("exclusionEnabled", true);

        EXCLUSION_LIST = builder
                .comment("List of entity IDs (e.g., 'minecraft:ender_dragon') that are excluded from inactive mob behavior control. Default includes common bosses.")
                .defineList("exclusionList",
                        Arrays.asList(
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "minecraft:warden",
                                "cataclysm:ancient_remnant",
                                "cataclysm:the_leviathan",
                                "cataclysm:the_harbinger",
                                "cataclysm:netherite_monstrosity",
                                "cataclysm:ignis",
                                "cataclysm:ender_guardian",
                                "cataclysm:maledictus",
                                "cataclysm:scylla",
                                "mowziesmobs:ferrous_wroughtnaut",
                                "mowziesmobs:frostmaw",
                                "mowziesmobs:umvuthi",
                                "mowziesmobs:naga",
                                "aquamirae:captain_cornelia",
                                "irons_spellbooks:dead_king",
                                "irons_spellbooks:fire_boss",
                                "alexsmobs:void_worm",
                                "alexsmobs:void_worm_part",
                                "bosses_of_mass_destruction:gauntlet",
                                "bosses_of_mass_destruction:lich",
                                "bosses_of_mass_destruction:obsidilith",
                                "bosses_of_mass_destruction:void_blossom",
                                "fromtheshadows:nehemoth",
                                "nightfall_invade:arterius",
                                "dodosmobs:bone_chimera",
                                "fdbosses:malkuth",
                                "fdbosses:chesed",
                                "fdbosses:geburah",
                                "ba_bt:land_golem",
                                "ba_bt:ocean_golem",
                                "ba_bt:core_golem",
                                "ba_bt:nether_golem",
                                "ba_bt:sky_golem",
                                "ba_bt:end_golem",
                                "tcr_bosses:scylla_humanoid",
                                "tcr_bosses:ancient_remnant_humanoid",
                                "tcr_bosses:harbinger_humanoid",
                                "tcr_bosses:maledictus_humanoid",
                                "tcr_bosses:netherite_humanoid",
                                "tcr_bosses:ender_guardian_humanoid",
                                "tcr_bosses:leviathan_humanoid",
                                "tcr_bosses:ignis_humanoid"
                        ),
                        it -> it instanceof String);

        NEUTRAL_MONSTER_LIST = builder
                .comment("List of entity IDs that are considered neutral monsters (e.g., 'minecraft:enderman'). These entities will only become hostile when provoked.")
                .defineList("neutralMonsterList",
                        Arrays.asList(
                                "minecraft:enderman",
                                "minecraft:piglin",
                                "minecraft:piglin_brute",
                                "minecraft:zombified_piglin"
                        ),
                        obj -> obj instanceof String);

        FRIENDLY_LIST = builder
                .comment("List of entity IDs that are always friendly and will never attack players (e.g., 'minecraft:villager'). These entities will be ignored by the entire system.")
                .defineList("friendlyList",
                        Arrays.asList(
                        ),
                        obj -> obj instanceof String);
        BETTER_AI_CONFIG = builder.build();
    }

    public BetterAIConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/better_ai.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default better ai config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        BETTER_AI_CONFIG.setConfig(fileConfig);
    }
}