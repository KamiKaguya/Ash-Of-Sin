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

public class SoulLikeBossBattleConfig {
    public static final ForgeConfigSpec SOUL_LIKE_BOSS_BATTLE_CONFIG;
    public static ForgeConfigSpec.BooleanValue SOUL_LIKE_BOSS_BATTLE_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SOUL_LIKE_BOSS_BATTLE_ENTITY;
    public static ForgeConfigSpec.ConfigValue<Integer> BOSS_BATTLE_DISTANCE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> PLAYER_GAMEMODE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> IRON_GOLEM;
    public static ForgeConfigSpec.ConfigValue<Boolean> ANTI_IRON_GOLEM;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        SOUL_LIKE_BOSS_BATTLE_ON = builder.comment("Soul Like Boss Battle On")
                .comment("Enable Soul Like Boss Battle")
                .define("Soul Like Boss Battle On", true);
        SOUL_LIKE_BOSS_BATTLE_ENTITY = builder.comment("Soul Like Boss Battle Entity")
                .comment("Soul Like Boss Battle Entity id.")
                .defineList("Soul Like Boss Battle Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "minecraft:warden",
                                "cataclysm:ender_guardian",
                                "cataclysm:ignis",
                                "cataclysm:netherite_monstrosity",
                                "cataclysm:the_harbinger",
                                "cataclysm:the_leviathan",
                                "cataclysm:ancient_ancient_remnant",
                                "cataclysm:ancient_remnant",
                                "cataclysm:maledictus",
                                "aquamirae:captain_cornelia",
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
                                "ba_bt:end_golem"
                        )),
                        obj -> obj instanceof String);
        BOSS_BATTLE_DISTANCE = builder.comment("Soul Like Boss Battle Distance")
                .comment("Radius of Soul Like Boss Battle. Minimum is 1, maximum is 128.(Default is 64 blocks)")
                .defineInRange("Soul Like Boss Battle Distance", 64,1,128);
        PLAYER_GAMEMODE = builder.comment("Soul Like Boss Battle Check Player Gamemode")
                .comment("Soul Like Boss Battle Valid Player's Gamemode: [survival, adventure, creative, spectator]")
                .defineList("Gamemode",
                        () -> new ArrayList<>(Arrays.asList(
                                "survival",
                                "adventure"
                        )),
                        obj -> obj instanceof String);
        IRON_GOLEM = builder.comment("Iron Golem Entity")
                .comment("Iron Golem entity id.")
                .defineList("Iron Golem Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "minecraft:iron_golem",
                                "create:contraption",
                                "create:stationary_contraption"
                        )),
                        obj -> obj instanceof String);
        ANTI_IRON_GOLEM = builder.comment("Anti-Iron Golem")
                .comment("Not allow Iron Golem hurt BOSS.")
                .define("Anti-Iron Golem",true);
        SOUL_LIKE_BOSS_BATTLE_CONFIG = builder.build();
    }

    public SoulLikeBossBattleConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/soul_like_boss_battle.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default soul like boss battle config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        SOUL_LIKE_BOSS_BATTLE_CONFIG.setConfig(fileConfig);
    }
}