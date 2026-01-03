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


public class CustomAntiTrapCageEntityConfig {
    public static final ForgeConfigSpec ANTI_TRAP_CAGE_ENTITY_CONFIG;
    public static ForgeConfigSpec.BooleanValue ANTI_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_TRAP_CAGE_ENTITY;
    public static ForgeConfigSpec.ConfigValue<Integer> CHECK_DISTANCE;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ON = builder.comment("Anti On")
                .comment("Enable Anti-Trap_Cage Entity")
                .define("Anti On", false);
        ANTI_TRAP_CAGE_ENTITY = builder.comment("Anti-Trap_Cage Entity")
                .defineList("Anti-Trap_Cage Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "minecraft:warden"
                        )),
                        o -> o instanceof String);
        CHECK_DISTANCE = builder.comment("Check Distance")
                .comment("Radius of Anti-Trap_cage entity. Minimum is 1, maximum is 64.(Default is 8 block)")
                .defineInRange("Check Distance", 8,1,64);
        ANTI_TRAP_CAGE_ENTITY_CONFIG = builder.build();
    }

    public CustomAntiTrapCageEntityConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_anti_trap_cage_entity.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default custom anti trap cage entity config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ANTI_TRAP_CAGE_ENTITY_CONFIG.setConfig(fileConfig);
    }
}