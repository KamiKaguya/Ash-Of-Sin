package com.kamikaguya.ash_of_sin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CustomEntityItemConfigManager {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final Type CONFIG_TYPE = new TypeToken<List<CustomEntityItemConfig>>() {}.getType();
    public List<CustomEntityItemConfig> customEntityItemConfig;
    public final Path configPath;

    public CustomEntityItemConfigManager() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_entity_item.json");
        loadConfig();
    }

    public void initializeDefaultConfig() {
        List<CustomEntityItemConfig> defaultConfigs = new ArrayList<>();

        CustomEntityItemConfig forgottenConfig = new CustomEntityItemConfig();
        forgottenConfig.setEntity(List.of("quark:forgotten"));
        CustomItemConfig forgottenItemConfig = new CustomItemConfig();
        forgottenItemConfig.setHelmet(List.of(""));
        forgottenItemConfig.setHelmetNBT(List.of("{}"));
        forgottenItemConfig.setChestplate(List.of("epicfight:stray_robe"));
        forgottenItemConfig.setChestplateNBT(List.of("{}"));
        forgottenItemConfig.setLeggings(List.of("epicfight:stray_pants"));
        forgottenItemConfig.setLeggingsNBT(List.of("{}"));
        forgottenItemConfig.setBoots(List.of(""));
        forgottenItemConfig.setBootsNBT(List.of("{}"));
        forgottenItemConfig.setMainhand(List.of(""));
        forgottenItemConfig.setMainhandNBT(List.of("{}"));
        forgottenItemConfig.setOffhand(List.of(""));
        forgottenItemConfig.setOffhandNBT(List.of("{}"));
        forgottenConfig.setItem(forgottenItemConfig);
        defaultConfigs.add(forgottenConfig);

        CustomEntityItemConfig strayConfig = new CustomEntityItemConfig();
        strayConfig.setEntity(List.of("minecraft:stray"));
        CustomItemConfig strayItemConfig = new CustomItemConfig();
        strayItemConfig.setHelmet(List.of(""));
        strayItemConfig.setHelmetNBT(List.of("{}"));
        strayItemConfig.setChestplate(List.of("epicfight:stray_robe"));
        strayItemConfig.setChestplateNBT(List.of("{}"));
        strayItemConfig.setLeggings(List.of("epicfight:stray_pants"));
        strayItemConfig.setLeggingsNBT(List.of("{}"));
        strayItemConfig.setBoots(List.of(""));
        strayItemConfig.setBootsNBT(List.of("{}"));
        strayItemConfig.setMainhand(List.of(""));
        strayItemConfig.setMainhandNBT(List.of("{}"));
        strayItemConfig.setOffhand(List.of(""));
        strayItemConfig.setOffhandNBT(List.of("{}"));
        strayConfig.setItem(strayItemConfig);
        defaultConfigs.add(strayConfig);

        String defaultConfigJson = GSON.toJson(defaultConfigs, CONFIG_TYPE);
        try {
            Files.createDirectories(configPath.getParent());
            Files.write(configPath, defaultConfigJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            initializeDefaultConfig();
        } else {
            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                customEntityItemConfig = GSON.fromJson(reader, CONFIG_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<CustomEntityItemConfig> getCustomEntityItemConfig() {
        return customEntityItemConfig;
    }
}