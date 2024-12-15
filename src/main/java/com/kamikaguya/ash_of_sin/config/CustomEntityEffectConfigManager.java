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
                    new CustomEntityEffectConfig("minecraft:wither", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("minecraft:ender_dragon", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("minecraft:warden", Arrays.asList(
                            new CustomEffectConfig("epicfight:stun_immunity", "infinite", 0),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 2),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0)
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
                    new CustomEntityEffectConfig("hmag:ender_executor", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:banshee", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:crimson_slaughterer", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:necrotic_reaper", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:imp", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:ghastly_seeker", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("hmag:dodomeki", Arrays.asList(
                            new CustomEffectConfig("minecraft:fire_resistance", "infinite", 0),
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_knight", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
                            new CustomEffectConfig("alexsmobs:soulsteal", "infinite", 6),
                            new CustomEffectConfig("alexsmobs:knockback_resistance", "infinite", 0),
                            new CustomEffectConfig("alexsmobs:poison_resistance", "infinite", 0)
                    )),
                    new CustomEntityEffectConfig("iceandfire:dread_lich", Arrays.asList(
                            new CustomEffectConfig("minecraft:haste", "infinite", 4),
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3),
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
                    new CustomEntityEffectConfig("aquamirae:captain_cornelia", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("graveyard:lich", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("irons_spellbooks:dead_king", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ender_guardian", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ender_golem", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ignis", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:netherite_monstrosity", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:the_harbinger", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:the_leviathan", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ancient_ancient_remnant", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:ancient_remnant", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("cataclysm:maledictus", List.of(
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
                    new CustomEntityEffectConfig("soulslikeuniverse:beastclergyman", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossabysswatcher", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossartorias", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossdragonslayerarmour", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossgael", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossgundyr", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossgwyndolin", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bosslookingglassknight", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossmaliketh", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossnamelessking", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bossornstein", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:bosssoulofcinder", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:cloneabysswatcher", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:eldenbeast", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:fieldboss_countrobert", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:fieldboss_crucibleknight", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:fieldboss_elemer", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:fieldboss_mohg", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:godfrey", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:godskinapostle", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:godskinnoble", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:malenia", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:monstersmough", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:morgott", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:radagon", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("soulslikeuniverse:radahn", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("gom:ancient_guardian", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("gom:fallen_lord", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("gom:notch", List.of(
                            new CustomEffectConfig("minecraft:resistance", "infinite", 3)
                    )),
                    new CustomEntityEffectConfig("gom:minecraftlord", List.of(
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