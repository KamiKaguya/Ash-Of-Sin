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
                        () -> new ArrayList<>(Arrays.asList("ash_of_sin:kamikaguya", "iceandfire:ice_dragon", "iceandfire:fire_dragon", "iceandfire:lightning_dragon", "dwmg:hmag_zombie_girl", "dwmg:hmag_husk_girl", "dwmg:hmag_drowned_girl", "dwmg:hmag_skeleton_girl", "dwmg:hmag_wither_skeleton_girl", "dwmg:hmag_stray_girl", "dwmg:hmag_creeper_girl", "dwmg:hmag_creeper_girl", "hmag:ender_executor", "dwmg:hmag_ender_executor", "dwmg:hmag_kobold", "dwmg:hmag_melty_monster", "dwmg:hmag_cursed_doll", "dwmg:hmag_jack_frost", "dwmg:hmag_hornet", "dwmg:hmag_dullahan", "hmag:banshee", "dwmg:hmag_banshee", "dwmg:hmag_alraune", "hmag:ghastly_seeker", "dwmg:hmag_ghastly_seeker", "dwmg:hmag_redcap", "dwmg:hmag_slime_girl", "hmag:crimson_slaughterer", "dwmg:hmag_crimson_slaughterer", "dwmg:hmag_snow_canine", "dwmg:hmag_harpy", "hmag:necrotic_reaper", "dwmg:hmag_necrotic_reaper", "hmag:dodomeki", "dwmg:hmag_dodomeki", "hmag:imp", "dwmg:hmag_imp", "dwmg:hmag_glaryad")),
                        o -> o instanceof String);
        List<String> antiItems = List.of(
                "dawncraft:slayers_blade"
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