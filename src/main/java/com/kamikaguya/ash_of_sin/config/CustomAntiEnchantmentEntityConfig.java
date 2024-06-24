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

public class CustomAntiEnchantmentEntityConfig {
    public static final ForgeConfigSpec ANTI_ENCHANTMENT_ENTITY_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_ENCHANTMENT_ENTITY;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_ENCHANTMENT;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ENCHANTMENT_ENTITY = builder.comment("Anti-Enchantment Entity")
                .defineList("Anti-Enchantment Entity",
                        () -> new ArrayList<>(List.of("ash_of_sin:kamikaguya")),
                        o -> o instanceof String);
        List<String> antiEffects = List.of(
                "ash_of_sin:absolute_rule"
        );
        ANTI_ENCHANTMENT = builder.comment("Enchantment ID")
                .defineList("Anti-Enchantment ID",
                        antiEffects,
                        o -> o instanceof String);
        ANTI_ENCHANTMENT_ENTITY_CONFIG = builder.build();
    }

    public CustomAntiEnchantmentEntityConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_anti_enchantment_entity.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default custom anti enchantment entity config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ANTI_ENCHANTMENT_ENTITY_CONFIG.setConfig(fileConfig);
    }
}