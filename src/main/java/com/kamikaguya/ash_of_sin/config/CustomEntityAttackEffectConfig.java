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

public class CustomEntityAttackEffectConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Type CONFIG_TYPE = new TypeToken<List<CustomAttackEntityConfig>>() {}.getType();
    public List<CustomAttackEntityConfig> customEntityAttackEffectConfig;
    public final Path configPath;

    public CustomEntityAttackEffectConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_entity_attack_effect.json");
        initializeDefaultConfig();
    }

    public void initializeDefaultConfig() {
        if (!Files.exists(configPath)) {
            List<CustomAttackEntityConfig> defaultCustomAttackEntityConfig = Arrays.asList(
                    new CustomAttackEntityConfig("iceandfire:ice_dragon", List.of(
                            new CustomAttackEffectConfig("apotheosis:sundering", 60, 2)
                    )),
                    new CustomAttackEntityConfig("iceandfire:fire_dragon", List.of(
                            new CustomAttackEffectConfig("apotheosis:sundering", 60, 2)
                    )),
                    new CustomAttackEntityConfig("iceandfire:lightning_dragon", List.of(
                            new CustomAttackEffectConfig("apotheosis:sundering", 60, 2)
                    )),
                    new CustomAttackEntityConfig("hmag:ender_executor", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("hmag:banshee", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("hmag:crimson_slaughterer", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("hmag:necrotic_reaper", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("hmag:imp", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("hmag:ghastly_seeker", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("hmag:dodomeki", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("dwmg:hmag_ender_executor", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("dwmg:hmag_banshee", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("dwmg:hmag_crimson_slaughterer", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("dwmg:hmag_necrotic_reaper", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("dwmg:hmag_imp", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("dwmg:hmag_ghastly_seeker", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    )),
                    new CustomAttackEntityConfig("dwmg:hmag_dodomeki", Arrays.asList(
                            new CustomAttackEffectConfig("apotheosis:sundering", 15, 1)
                    ))
            );
            String defaultConfigJson = GSON.toJson(defaultCustomAttackEntityConfig, CONFIG_TYPE);

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
            this.customEntityAttackEffectConfig = GSON.fromJson(reader, CONFIG_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CustomAttackEntityConfig> getCustomEntityAttackEffectConfig () {
        return customEntityAttackEffectConfig;
    }
}