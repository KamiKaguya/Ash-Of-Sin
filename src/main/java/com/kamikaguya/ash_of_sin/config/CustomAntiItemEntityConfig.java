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

public class CustomAntiItemEntityConfig {
    public static final ForgeConfigSpec ANTI_ITEM_ENTITY_CONFIG;
    public static ForgeConfigSpec.BooleanValue ANTI_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_ITEM_ENTITY;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_ITEM;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ON = builder.comment("Anti On")
                .comment("Enable Anti-Item Entity")
                .define("Anti On", true);
        ANTI_ITEM_ENTITY = builder.comment("Anti-Item Entity")
                .defineList("Anti-Item Entity",
                        () -> new ArrayList<>(Arrays.asList("ash_of_sin:kamikaguya",
                                "iceandfire:ice_dragon",
                                "iceandfire:fire_dragon",
                                "iceandfire:lightning_dragon"
                        )),
                        o -> o instanceof String);
        List<String> antiItems = List.of(
                "minecraft:bedrock"
        );
        ANTI_ITEM = builder.comment("Anti-Item ID")
                .defineList("Anti-Item ID",
                        antiItems,
                        o -> o instanceof String);
        ANTI_ITEM_ENTITY_CONFIG = builder.build();
    }

    public CustomAntiItemEntityConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_anti_item_entity.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default custom anti item entity config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ANTI_ITEM_ENTITY_CONFIG.setConfig(fileConfig);
    }
}