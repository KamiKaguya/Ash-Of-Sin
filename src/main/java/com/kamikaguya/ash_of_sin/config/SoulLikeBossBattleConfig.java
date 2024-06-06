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
    private static ForgeConfigSpec SOUL_LIKE_BOSS_BATTLE_CONFIG;
    public static ForgeConfigSpec.BooleanValue SOUL_LIKE_BOSS_BATTLE_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SOUL_LIKE_BOSS_BATTLE_ENTITY;
    public static ForgeConfigSpec.ConfigValue<Integer> BOSS_BATTLE_DISTANCE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> IRON_GOLEM;
    public static ForgeConfigSpec.ConfigValue<Boolean> ANTI_IRON_GOLEM;
    private final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        SOUL_LIKE_BOSS_BATTLE_ON = builder.comment("Soul Like Boss Battle On")
                .comment("Enable Soul Like Boss Battle")
                .define("Soul Like Boss Battle On", true);
        SOUL_LIKE_BOSS_BATTLE_ENTITY = builder.comment("Soul Like Boss Battle Entity")
                .comment("Soul Like Boss Battle entity id.")
                .defineList("Soul Like Boss Battle Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "goblinsanddungeons:goblin_king",
                                "simple_mobs:corrupted_ogre",
                                "simple_mobs:knight_4",
                                "simple_mobs:nine_tails",
                                "simple_mobs:skeletonlord",
                                "simple_mobs:sentinel_knight",
                                "simple_mobs:fire_giant",
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "cataclysm:ender_guardian",
                                "simple_mobs:notch_boss",
                                "simple_mobs:elemental_deity",
                                "simple_mobs:herobrine",
                                "simple_mobs:dragon_lord",
                                "witherstormmod:wither_storm",
                                "bloodandmadness:father_gascoigne",
                                "bloodandmadness:gascoigne_beast",
                                "bloodandmadness:micolash",
                                "mowziesmobs:ferrous_wroughtnaut",
                                "epiceldenring:godfrey",
                                "epiceldenring:godfreyphasetwo",
                                "epiceldenring:radahn",
                                "epiceldenring:malenia",
                                "epiceldenring:maliketh",
                                "simple_mobs:ent",
                                "simple_mobs:samurai_4",
                                "simple_mobs:samurai_5",
                                "simple_mobs:martian",
                                "simple_mobs:gael",
                                "simple_mobs:morgott",
                                "mowziesmobs:frostmaw",
                                "mowziesmobs:umvuthi",
                                "mowziesmobs:naga",
                                "ba_bt:land_golem",
                                "ba_bt:ocean_golem",
                                "aquamirae:captain_cornelia",
                                "graveyard:lich",
                                "whisperwoods:hirschgeist",
                                "irons_spellbooks:dead_king",
                                "darkersouls:nameless_king",
                                "cataclysm:ignis",
                                "cataclysm:netherite_monstrosity",
                                "simple_mobs:twins_stone",
                                "simple_mobs:first_twin",
                                "simple_mobs:second_twin",
                                "alexsmobs:void_worm",
                                "alexsmobs:void_worm_part",
                                "bosses_of_mass_destruction:gauntlet",
                                "bosses_of_mass_destruction:lich",
                                "bosses_of_mass_destruction:obsidilith",
                                "bosses_of_mass_destruction:void_blossom",
                                "blue_skies:arachnarch",
                                "blue_skies:alchemist",
                                "blue_skies:summoner",
                                "blue_skies:starlit_crusher",
                                "illageandspillage:spiritcaller",
                                "illageandspillage:magispeller",
                                "conjurer_illager:conjurer",
                                "wildbackport:warden"
                        )),
                        obj -> obj instanceof String);
        BOSS_BATTLE_DISTANCE = builder.comment("Soul Like Boss Battle Distance")
                .comment("Radius of Soul Like Boss Battle. Minimum is 1, maximum is 128.(Default is 64 block)")
                .defineInRange("Soul Like Boss Battle Distance", 64,1,128);
        IRON_GOLEM = builder.comment("Iron Golem Entity")
                .comment("Iron Golem entity id.")
                .defineList("Iron Golem Entity",
                        () -> new ArrayList<>(Arrays.asList(
                        "minecraft:iron_golem"
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
