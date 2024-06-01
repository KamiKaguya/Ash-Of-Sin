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

public class AdventureDimensionConfig {
    private static ForgeConfigSpec ADVENTURE_DIMENSION_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ADVENTURE_DIMENSION_ID;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ADVENTURE_DIMENSION_ALLOW_PLAYER_ID;
    private final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ADVENTURE_DIMENSION_ID = builder.comment("Adventure Dimension ID")
                .defineList("Adventure Dimension ID",
                        () -> new ArrayList<>(Arrays.asList("witherstormmod:bowels","simple_mobs:fervent_dream")),
                        o -> o instanceof String
                );
        ADVENTURE_DIMENSION_ALLOW_PLAYER_ID = builder.comment("Exception Player ID")
                .defineList("Exception Player ID",
                        () -> new ArrayList<>(Arrays.asList("KamiKaguya")),
                        o -> o instanceof String
                );
        ADVENTURE_DIMENSION_CONFIG = builder.build();
    }

    public AdventureDimensionConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/adventure_dimension.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default adventure dimension config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ADVENTURE_DIMENSION_CONFIG.setConfig(fileConfig);
    }
}
