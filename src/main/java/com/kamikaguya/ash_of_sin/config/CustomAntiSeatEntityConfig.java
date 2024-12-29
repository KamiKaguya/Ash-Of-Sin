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

public class CustomAntiSeatEntityConfig {
    public static final ForgeConfigSpec ANTI_SEAT_ENTITY_CONFIG;
    public static ForgeConfigSpec.BooleanValue ANTI_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_SEAT_ENTITY;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ON = builder.comment("Anti On")
                .comment("Enable Anti-Seat Entity")
                .define("Anti On", true);
        ANTI_SEAT_ENTITY = builder.comment("Anti-Seat Entity")
                .defineList("Anti-Seat Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "ash_of_sin:kamikaguya",
                                "ash_of_sin:another",
                                "ash_of_sin:doppelganger",
                                "ash_of_sin:assassin",
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "minecraft:warden",
                                "tensura:folgen",
                                "tensura:hinata_sakaguchi",
                                "tensura:kirara_mizutani",
                                "tensura:kyoya_tachibana",
                                "tensura:mai_furuki",
                                "tensura:mark_lauren",
                                "tensura:shinji_tanimura",
                                "tensura:shin_ryusei",
                                "tensura:shizu",
                                "tensura:shogo_taguchi",
                                "tensura:falmuth_knight",
                                "tensura:charybdis",
                                "tensura:akash",
                                "tensura:ifrit",
                                "tensura:ifrit_clone",
                                "tensura:sylphide",
                                "tensura:undine",
                                "tensura:war_gnome",
                                "tensura:orc_lord",
                                "tensura:orc_disaster",
                                "tensura:supermassive_slime",
                                "tensura:elemental_colossus",
                                "cataclysm:ender_guardian",
                                "cataclysm:ignis",
                                "cataclysm:netherite_monstrosity",
                                "cataclysm:the_harbinger",
                                "cataclysm:the_leviathan",
                                "cataclysm:ancient_ancient_remnant",
                                "cataclysm:ancient_remnant",
                                "cataclysm:maledictus",
                                "bosses_of_mass_destruction:lich",
                                "bosses_of_mass_destruction:obsidilith",
                                "bosses_of_mass_destruction:gauntlet",
                                "bosses_of_mass_destruction:void_blossom",
                                "aquamirae:captain_cornelia",
                                "irons_spellbooks:dead_king",
                                "graveyard:lich",
                                "iceandfire:ice_dragon",
                                "iceandfire:fire_dragon",
                                "iceandfire:lightning_dragon",
                                "iceandfire:dread_lich"
                        )),
                        o -> o instanceof String);
        ANTI_SEAT_ENTITY_CONFIG = builder.build();
    }

    public CustomAntiSeatEntityConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_anti_seat_entity.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default custom anti seat entity config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ANTI_SEAT_ENTITY_CONFIG.setConfig(fileConfig);
    }
}