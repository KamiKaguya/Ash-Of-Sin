package com.kamikaguya.ash_of_sin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
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
                    new CustomEntityEffectConfig("ash_of_sin:kamikaguya", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6)
                    )),
                    new CustomEntityEffectConfig("minecraft:wither", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("minecraft:ender_dragon", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("minecraft:warden", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:ice_dragon", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:fire_dragon", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:lightning_dragon", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_knight", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_lich", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_scuttler", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_beast", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_ghoul", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_thrall", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("aquamirae:captain_cornelia", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("graveyard:lich", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("irons_spellbooks:dead_king", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ender_guardian", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ender_golem", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ignis", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:netherite_monstrosity", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:the_harbinger", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:the_leviathan", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ancient_ancient_remnant", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ancient_remnant", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:maledictus", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("alexsmobs:void_worm", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("alexsmobs:void_worm_part", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bosses_of_mass_destruction:gauntlet", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bosses_of_mass_destruction:lich", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bosses_of_mass_destruction:obsidilith", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("bosses_of_mass_destruction:void_blossom", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("eeeabsmobs:nameless_guardian", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("eeeabsmobs:corpse_warlock", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("eeeabsmobs:guling_sentinel_heavy", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("mowziesmobs:ferrous_wroughtnaut", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("mowziesmobs:frostmaw", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("mowziesmobs:umvuthi", Arrays.asList(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("mowziesmobs:naga", Arrays.asList(
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