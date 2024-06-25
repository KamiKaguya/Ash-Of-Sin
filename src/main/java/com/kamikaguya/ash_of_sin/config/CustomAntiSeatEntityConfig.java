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
                                "simple_mobs:notch_sit",
                                "simple_mobs:notch_boss",
                                "simple_mobs:elemental_deity",
                                "simple_mobs:herobrine",
                                "simple_mobs:dragon_lord",
                                "witherstormmod:withered_symbiont",
                                "witherstormmod:wither_storm",
                                "witherstormmod:command_block",
                                "simple_mobs:mjolnir_thrown",
                                "bloodandmadness:father_gascoigne",
                                "bloodandmadness:gascoigne_beast",
                                "bloodandmadness:micolash",
                                "mowziesmobs:ferrous_wroughtnaut",
                                "wildbackport:warden",
                                "occ:vergil",
                                "occ:vergil_2",
                                "epicdmcbossdante:dante",
                                "epicsisterfriede:friede",
                                "epicsisterfriede:blackflamefriede",
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
                                "meetyourfight:bellringer",
                                "meetyourfight:dame_fortuna",
                                "meetyourfight:swampjaw",
                                "iceandfire:ice_dragon",
                                "iceandfire:fire_dragon",
                                "iceandfire:lightning_dragon",
                                "iceandfire:dread_knight",
                                "iceandfire:dread_lich",
                                "hmag:ender_executor",
                                "hmag:banshee",
                                "hmag:ghastly_seeker",
                                "hmag:crimson_slaughterer",
                                "hmag:necrotic_reaper",
                                "hmag:dodomeki",
                                "hmag:imp"
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