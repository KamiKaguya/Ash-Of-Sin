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
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACK_LIST;
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
                .comment("List of entity IDs or tags (prefixed with '#') that are excluded from inactive mob behavior control (bosses and custom entities)." +
                        "Example: 'minecraft:ender_dragon' or '#minecraft:illager'.")
                .defineList("exclusionList",
                        Arrays.asList(
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "minecraft:warden"
                        ),
                        it -> it instanceof String);

        NEUTRAL_MONSTER_LIST = builder
                .comment("List of entity IDs or tags (prefixed with '#') that are considered neutral monsters.")
                .defineList("neutralMonsterList",
                        Arrays.asList(
                                "minecraft:enderman",
                                "minecraft:piglin",
                                "minecraft:piglin_brute",
                                "minecraft:zombified_piglin"
                        ),
                        obj -> obj instanceof String);

        FRIENDLY_LIST = builder
                .comment("List of entity IDs or tags (prefixed with '#') that are always friendly and will never attack players. " +
                        "These entities will be ignored by the entire system.")
                .defineList("friendlyList",
                        Arrays.asList(
                        ),
                        obj -> obj instanceof String);

        BLACK_LIST = builder
                .comment("List of entity IDs or tags (prefixed with '#') that are excluded from better AI entirely.")
                .defineList("blackList",
                        Arrays.asList(
                                "minecraft:ender_dragon"
                        ),
                        it -> it instanceof String);
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