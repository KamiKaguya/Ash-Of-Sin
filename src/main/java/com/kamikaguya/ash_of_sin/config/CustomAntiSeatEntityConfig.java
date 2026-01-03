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
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "minecraft:warden",
                                "minecraft:iron_golem",
                                "cataclysm:ender_guardian",
                                "cataclysm:ignis",
                                "cataclysm:netherite_monstrosity",
                                "cataclysm:the_harbinger",
                                "cataclysm:the_leviathan",
                                "cataclysm:ancient_ancient_remnant",
                                "cataclysm:ancient_remnant",
                                "cataclysm:maledictus",
                                "aquamirae:captain_cornelia",
                                "fromtheshadows:nehemoth",
                                "block_factorys_bosses:infernal_dragon",
                                "block_factorys_bosses:sandworm",
                                "block_factorys_bosses:yeti",
                                "block_factorys_bosses:underworld_knight",
                                "fdbosses:malkuth",
                                "fdbosses:chesed",
                                "nightfall_invade:arterius",
                                "alexsmobs:void_worm",
                                "mowziesmobs:ferrous_wroughtnaut",
                                "mowziesmobs:frostmaw",
                                "mowziesmobs:umvuthi",
                                "bosses_of_mass_destruction:gauntlet",
                                "bosses_of_mass_destruction:lich",
                                "bosses_of_mass_destruction:obsidilith",
                                "bosses_of_mass_destruction:void_blossom",
                                "ba_bt:land_golem",
                                "ba_bt:ocean_golem",
                                "ba_bt:core_golem",
                                "ba_bt:nether_golem",
                                "ba_bt:sky_golem",
                                "ba_bt:end_golem",
                                "minecraft:zombie",
                                "minecraft:husk",
                                "minecraft:drowned",
                                "minecraft:skeleton",
                                "minecraft:wither_skeleton",
                                "minecraft:stray",
                                "minecraft:pillager",
                                "minecraft:vindicator",
                                "mowziesmobs:umvuthana",
                                "mowziesmobs:umvuthana_raptor",
                                "mowziesmobs:umvuthana_crane",
                                "quark:forgotten"
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