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


public class CustomAntiTrapCageEntityConfig {
    public static final ForgeConfigSpec ANTI_TRAP_CAGE_ENTITY_CONFIG;
    public static ForgeConfigSpec.BooleanValue ANTI_ON;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ANTI_TRAP_CAGE_ENTITY;
    public static ForgeConfigSpec.ConfigValue<Integer> CHECK_DISTANCE;
    public final Path configPath;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ANTI_ON = builder.comment("Anti On")
                .comment("Enable Anti-Trap_Cage Entity")
                .define("Anti On", true);
        ANTI_TRAP_CAGE_ENTITY = builder.comment("Anti-Trap_Cage Entity")
                .defineList("Anti-Trap_Cage Entity",
                        () -> new ArrayList<>(Arrays.asList(
                                "ash_of_sin:kamikaguya",
                                "ash_of_sin:another",
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
                                "hmag:imp",
                                "simple_mobs:blackmarketvillager",
                                "simple_mobs:multi_part_leg",
                                "simple_mobs:multipartbody",
                                "simple_mobs:multipartbody_2",
                                "simple_mobs:drag_multi_part_2",
                                "simple_mobs:drag_multi_part_3",
                                "simple_mobs:drag_multi_part_4",
                                "simple_mobs:drag_multi_part_5",
                                "simple_mobs:drag_multi_part_6",
                                "simple_mobs:ice_drag_multi_part_3",
                                "simple_mobs:ice_drag_multi_part_4",
                                "simple_mobs:ice_drag_multi_part_5",
                                "simple_mobs:ice_drag_multi_part_6",
                                "simple_mobs:ice_drag_multi_part_2",
                                "simple_mobs:fire_drag_multi_part",
                                "simple_mobs:ice_drag_multi_part",
                                "simple_mobs:fire_drag_multi_part_1",
                                "simple_mobs:ice_drag_multi_part_1",
                                "simple_mobs:staff_interact",
                                "simple_mobs:giant",
                                "simple_mobs:notch_punch",
                                "simple_mobs:notch_grab",
                                "simple_mobs:dl_multi_part_2",
                                "simple_mobs:dl_multi_part_3",
                                "simple_mobs:dl_multi_part_4",
                                "simple_mobs:dl_multi_part_5",
                                "simple_mobs:dl_multi_part_6",
                                "simple_mobs:dl_multi_part_7",
                                "simple_mobs:giant_skele",
                                "simple_mobs:fire_dragon",
                                "simple_mobs:ice_dragon",
                                "simple_mobs:lightning_dragon",
                                "simple_mobs:protector",
                                "simple_mobs:shield_element",
                                "simple_mobs:sword_auto_2",
                                "simple_mobs:sword_auto",
                                "simple_mobs:barrel_man",
                                "simple_mobs:cave_dweller",
                                "simple_mobs:moss_golem",
                                "irons_spellbooks:archevoker",
                                "irons_spellbooks:citadel_keeper",
                                "irons_spellbooks:pyromancer",
                                "irons_spellbooks:cyromancer",
                                "irons_spellbooks:necromancer",
                                "irons_spellbooks:dead_king_corpse",
                                "dawncraft:quest_player",
                                "humancompanions:knight",
                                "humancompanions:archer",
                                "humancompanions:arbalist",
                                "humancompanions:axeguard",
                                "biomemakeover:adjudicator",
                                "biomemakeover:stone_golem",
                                "minecraft:iron_golem",
                                "minecraft:ravager",
                                "hunterillager:hunterillager",
                                "minecraft:blaze",
                                "cataclysm:nameless_sorcerer",
                                "cataclysm:ignited_revenant",
                                "mutantmonsters:mutant_creeper",
                                "mutantmonsters:mutant_enderman",
                                "mutantmonsters:mutant_skeleton",
                                "mutantmonsters:mutant_snow_golem",
                                "mutantmonsters:mutant_zombie",
                                "mutantmonsters:spider_pig",
                                "mutantmonsters:body_part",
                                "undead_revamp2:thewolf",
                                "undead_revamp2:thebeartamer",
                                "monsterplus:abyssologer",
                                "alexsmobs:warped_mosco",
                                "guardvillagers:guard",
                                "minecraft:villager",
                                "quest_giver:quest_villager",
                                "quest_giver:quest_guard_villager",
                                "aquamirae:maze_mother"
                        )),
                        o -> o instanceof String);
        CHECK_DISTANCE = builder.comment("Check Distance")
                .comment("Radius of Anti-Trap_cage entity. Minimum is 1, maximum is 64.(Default is 8 block)")
                .defineInRange("Check Distance", 8,1,64);
        ANTI_TRAP_CAGE_ENTITY_CONFIG = builder.build();
    }

    public CustomAntiTrapCageEntityConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_anti_trap_cage_entity.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default custom anti trap cage entity config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        ANTI_TRAP_CAGE_ENTITY_CONFIG.setConfig(fileConfig);
    }
}