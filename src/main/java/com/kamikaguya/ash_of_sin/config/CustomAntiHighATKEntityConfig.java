package com.kamikaguya.ash_of_sin.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAntiHighATKEntityConfig {
    public static final ForgeConfigSpec ANTI_HIGH_ATK_ENTITY_CONFIG;
    public static ForgeConfigSpec.BooleanValue ANTI_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_HIGH_ATK_ENTITY;
    public static ForgeConfigSpec.ConfigValue<Double> MAX_ATK;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ON = builder.comment("Anti On")
                .comment("Enable Anti-High ATK Entity")
                .define("Anti On", true);
        ANTI_HIGH_ATK_ENTITY = builder.comment("Anti-High ATK Entity")
                .comment("Anti-High ATK entity id.")
                .defineList("Anti-High ATK Entity",
                        () -> new ArrayList<>(Arrays.asList("dwmg:hmag_zombie_girl", "dwmg:hmag_husk_girl", "dwmg:hmag_drowned_girl", "dwmg:hmag_skeleton_girl", "dwmg:hmag_wither_skeleton_girl", "dwmg:hmag_stray_girl", "dwmg:hmag_creeper_girl", "dwmg:hmag_creeper_girl", "dwmg:hmag_ender_executor", "dwmg:hmag_kobold", "dwmg:hmag_melty_monster", "dwmg:hmag_cursed_doll", "dwmg:hmag_jack_frost", "dwmg:hmag_hornet", "dwmg:hmag_dullahan", "dwmg:hmag_banshee", "dwmg:hmag_alraune", "dwmg:hmag_ghastly_seeker", "dwmg:hmag_redcap", "dwmg:hmag_slime_girl", "dwmg:hmag_crimson_slaughterer", "dwmg:hmag_snow_canine", "dwmg:hmag_harpy", "dwmg:hmag_necrotic_reaper", "dwmg:hmag_dodomeki", "dwmg:hmag_imp", "dwmg:hmag_glaryad")),
                        o -> o instanceof String);
        MAX_ATK = builder.comment("Max ATK")
                .comment("Limit of allowed ATK.")
                .define("Max ATK", 10000.0);
        ANTI_HIGH_ATK_ENTITY_CONFIG = builder.build();
    }

    public CustomAntiHighATKEntityConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_anti_high_atk_entity.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default custom anti high atk entity config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ANTI_HIGH_ATK_ENTITY_CONFIG.setConfig(fileConfig);
    }
}