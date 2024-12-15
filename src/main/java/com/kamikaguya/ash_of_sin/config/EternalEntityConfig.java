package com.kamikaguya.ash_of_sin.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EternalEntityConfig {
    public static final ForgeConfigSpec ETERNAL_ENTITY_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ETERNAL_ENTITY;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ETERNAL_ENTITY = builder.comment("Eternal Entity ID")
                .defineList("Eternal Entity ID",
                        () -> new ArrayList<>(Arrays.asList(
                                "ash_of_sin:kamikaguya",
                                "ash_of_sin:another",
                                "iceandfire:ice_dragon",
                                "iceandfire:fire_dragon",
                                "iceandfire:lightning_dragon"
                        )),
                        obj -> obj instanceof String);
        ETERNAL_ENTITY_CONFIG = builder.build();
    }

    public EternalEntityConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/eternal_entity.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default eternal entity config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ETERNAL_ENTITY_CONFIG.setConfig(fileConfig);
    }
}