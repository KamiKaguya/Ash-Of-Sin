package com.kamikaguya.ash_of_sin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class CustomEntityEffectConfigManager {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Type CONFIG_TYPE = new TypeToken<List<CustomEntityEffectConfig>>() {}.getType();
    public List<CustomEntityEffectConfig> customEntityEffectConfigManager;
    public final Path configPath;

    public CustomEntityEffectConfigManager() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_entity_effect.json");
        initializeDefaultConfig();
    }

    public void initializeDefaultConfig() {
        if (!Files.exists(configPath)) {
            List<CustomEntityEffectConfig> defaultCustomEntityEffectConfig = Arrays.asList(
                    new CustomEntityEffectConfig("simple_mobs:corrupted_ogre", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:knight_4", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 1)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:nine_tails", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 2)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:skeletonlord", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:sentinel_knight", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:fire_giant", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("minecraft:wither", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("minecraft:ender_dragon", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ender_guardian", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:notch_boss", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:elemental_deity", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:herobrine", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:dragon_lord", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4)
                    )),
                    new CustomEntityEffectConfig("ash_of_sin:kamikaguya", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6)
                    )),
                    new CustomEntityEffectConfig("iceandfire:ice_dragon", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 6),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("iceandfire:fire_dragon", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 6),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("iceandfire:lightning_dragon", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 6),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("witherstormmod:wither_storm", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 5),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("witherstormmod:withered_symbiont", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 5)
                    )),
                    new CustomEntityEffectConfig("hmag:ender_executor", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:banshee", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:crimson_slaughterer", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:necrotic_reaper", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:imp", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:ghastly_seeker", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:dodomeki", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("dwmg:hmag_ender_executor", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("dwmg:hmag_banshee", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("dwmg:hmag_crimson_slaughterer", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("dwmg:hmag_necrotic_reaper", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("dwmg:hmag_imp", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("dwmg:hmag_ghastly_seeker", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("dwmg:hmag_dodomeki", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("mowziesmobs:ferrous_wroughtnaut", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("minecraft:strength", "infinite", 19),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_knight", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_lich", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 4),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_scuttler", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 2),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_beast", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_ghoul", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_thrall", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("wildbackport:warden", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("epiceldenring:godfrey", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("epiceldenring:godfreyphasetwo", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("epiceldenring:radahn", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("epiceldenring:malenia", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("epiceldenring:maliketh", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("occ:vergil", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("occ:vergil_2", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("bloodandmadness:father_gascoigne", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bloodandmadness:gascoigne_beast", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bloodandmadness:micolash", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:ent", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:samurai_4", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:samurai_5", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:martian", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("mowziesmobs:frostmaw", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("mowziesmobs:umvuthi", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("mowziesmobs:naga", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("ba_bt:land_golem", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("ba_bt:ocean_golem", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("aquamirae:captain_cornelia", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("graveyard:lich", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("whisperwoods:hirschgeist", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("irons_spellbooks:dead_king", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("darkersouls:nameless_king", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ignis", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:netherite_monstrosity", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ender_golem", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:twins_stone", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:first_twin", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("simple_mobs:second_twin", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("alexsmobs:void_worm", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("alexsmobs:void_worm_part", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bosses_of_mass_destruction:gauntlet", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bosses_of_mass_destruction:lich", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bosses_of_mass_destruction:obsidilith", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bosses_of_mass_destruction:void_blossom", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("blue_skies:arachnarch", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("blue_skies:alchemist", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("blue_skies:summoner", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("blue_skies:starlit_crusher", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("illageandspillage:spiritcaller", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("illageandspillage:magispeller", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("conjurer_illager:conjurer", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("meetyourfight:bellringer", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("meetyourfight:dame_fortuna", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("meetyourfight:swampjaw", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    ))
            );
            String defaultConfigJson = GSON.toJson(defaultCustomEntityEffectConfig, CONFIG_TYPE);

            try {
                Files.createDirectories(configPath.getParent());
                Files.write(configPath, defaultConfigJson.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadConfig() {
        try (FileReader reader = new FileReader(this.configPath.toFile())) {
            this.customEntityEffectConfigManager = GSON.fromJson(reader, CONFIG_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CustomEntityEffectConfig> getCustomEntityEffectConfigManager() {
        return customEntityEffectConfigManager;
    }
}