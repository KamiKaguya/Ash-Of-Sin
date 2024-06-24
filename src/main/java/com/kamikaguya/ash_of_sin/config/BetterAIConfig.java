package com.kamikaguya.ash_of_sin.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BetterAIConfig {
    public static final ForgeConfigSpec BETTER_AI_CONFIG;
    public static ForgeConfigSpec.BooleanValue BETTER_AI_ON;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        BETTER_AI_ON = builder.comment("Better AI On")
                .comment("Allow lower beings to possess wisdom.(Enable Better AI)")
                .define("Better AI On", true);
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