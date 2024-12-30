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
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> IRON_GOLEM;
    public static ForgeConfigSpec.ConfigValue<Boolean> ANTI_IRON_GOLEM;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        SOUL_LIKE_BOSS_BATTLE_ON = builder.comment("Soul Like Boss Battle On")
                .comment("Enable Soul Like Boss Battle")
                .define("Soul Like Boss Battle On", true);
        SOUL_LIKE_BOSS_BATTLE_ENTITY = builder.comment("Soul Like Boss Battle Entity")
                .comment("Soul Like Boss Battle entity id.")
                .defineList("Soul Like Boss Battle Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "minecraft:wither",
                                "minecraft:ender_dragon",
                                "minecraft:warden",
                                "tensura:hinata_sakaguchi",
                                "tensura:charybdis",
                                "tensura:akash",
                                "tensura:ifrit",
                                "tensura:ifrit_clone",
                                "tensura:sylphide",
                                "tensura:undine",
                                "tensura:war_gnome",
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
                                "graveyard:nameless_guardian"
                        )),
                        obj -> obj instanceof String);
        BOSS_BATTLE_DISTANCE = builder.comment("Soul Like Boss Battle Distance")
                .comment("Radius of Soul Like Boss Battle. Minimum is 1, maximum is 128.(Default is 64 block)")
                .defineInRange("Soul Like Boss Battle Distance", 64,1,128);
        IRON_GOLEM = builder.comment("Iron Golem Entity")
                .comment("Iron Golem entity id.")
                .defineList("Iron Golem Entity",
                        () -> new ArrayList<>(List.of(
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