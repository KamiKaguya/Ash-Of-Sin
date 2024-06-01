package com.kamikaguya.ash_of_sin.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AntiSameModifierConfig {
    private static ForgeConfigSpec ANTI_SAME_MODIFIER_CONFIG;
    public static ForgeConfigSpec.BooleanValue ANTI_ON;
    private final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ON = builder.comment("Anti On")
                .comment("Enable Anti Same Modifier")
                .define("Anti On", true);
        ANTI_SAME_MODIFIER_CONFIG = builder.build();
    }

    public AntiSameModifierConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/anti_same_modifier.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default anti same modifier config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ANTI_SAME_MODIFIER_CONFIG.setConfig(fileConfig);
    }
}
