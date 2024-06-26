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
        ETERNAL_ENTITY = builder.comment("Eternal Entity")
                .defineList("Eternal Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "ash_of_sin:kamikaguya",
                                "ash_of_sin:another",
                                "iceandfire:ice_dragon",
                                "iceandfire:fire_dragon",
                                "iceandfire:lightning_dragon",
                                "witherstormmod:wither_storm",
                                "dwmg:hmag_zombie_girl",
                                "dwmg:hmag_husk_girl",
                                "dwmg:hmag_drowned_girl",
                                "dwmg:hmag_skeleton_girl",
                                "dwmg:hmag_wither_skeleton_girl",
                                "dwmg:hmag_stray_girl",
                                "dwmg:hmag_creeper_girl",
                                "dwmg:hmag_creeper_girl",
                                "dwmg:hmag_ender_executor",
                                "dwmg:hmag_kobold",
                                "dwmg:hmag_melty_monster",
                                "dwmg:hmag_cursed_doll",
                                "dwmg:hmag_jack_frost",
                                "dwmg:hmag_hornet",
                                "dwmg:hmag_dullahan",
                                "dwmg:hmag_banshee",
                                "dwmg:hmag_alraune",
                                "dwmg:hmag_ghastly_seeker",
                                "dwmg:hmag_redcap",
                                "dwmg:hmag_slime_girl",
                                "dwmg:hmag_crimson_slaughterer",
                                "dwmg:hmag_snow_canine",
                                "dwmg:hmag_harpy",
                                "dwmg:hmag_necrotic_reaper",
                                "dwmg:hmag_dodomeki",
                                "dwmg:hmag_imp",
                                "dwmg:hmag_glaryad"
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

    public static boolean isEternalEntity(Entity entity) {
        if (ETERNAL_ENTITY.get().contains(entity.getType().getRegistryName().toString())) {
            return true;
        }
        if (entity.getCustomName() != null && !entity.getCustomName().getString().isEmpty()) {
            String customName = entity.getCustomName().getString();
            return ETERNAL_ENTITY.get().stream().anyMatch(customName::equalsIgnoreCase);
        }
        return false;
    }
}