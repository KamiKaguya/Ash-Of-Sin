package com.kamikaguya.ash_of_sin.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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

        CustomEntityItemConfig dreadKnightConfig = new CustomEntityItemConfig();
        dreadKnightConfig.setEntity(List.of("iceandfire:dread_knight"));
        CustomItemConfig dreadKnightItemConfig = new CustomItemConfig();
        dreadKnightItemConfig.setHelmet(List.of());
        dreadKnightItemConfig.setHelmetNBT(List.of("{}"));
        dreadKnightItemConfig.setChestplate(List.of());
        dreadKnightItemConfig.setChestplateNBT(List.of("{}"));
        dreadKnightItemConfig.setLeggings(List.of());
        dreadKnightItemConfig.setLeggingsNBT(List.of("{}"));
        dreadKnightItemConfig.setBoots(List.of());
        dreadKnightItemConfig.setBootsNBT(List.of("{}"));
        dreadKnightItemConfig.setMainhand(List.of("iceandfire:dread_knight_sword"));
        dreadKnightItemConfig.setMainhandNBT(List.of("{}"));
        dreadKnightItemConfig.setOffhand(List.of("upgradednetherite:corrupt_upgraded_netherite_shield"));
        dreadKnightItemConfig.setOffhandNBT(List.of("{}"));
        dreadKnightConfig.setItem(dreadKnightItemConfig);
        defaultConfigs.add(dreadKnightConfig);

        CustomEntityItemConfig dreadThrallConfig = new CustomEntityItemConfig();
        dreadThrallConfig.setEntity(List.of("iceandfire:dread_thrall"));
        CustomItemConfig dreadThrallItemConfig = new CustomItemConfig();
        dreadThrallItemConfig.setHelmet(List.of("amethysttoolsmod:netherite_helmet_amethyst"));
        dreadThrallItemConfig.setHelmetNBT(List.of("{}"));
        dreadThrallItemConfig.setChestplate(List.of("amethysttoolsmod:netherite_chestplate_amethyst"));
        dreadThrallItemConfig.setChestplateNBT(List.of("{}"));
        dreadThrallItemConfig.setLeggings(List.of("amethysttoolsmod:netherite_leggings_amethyst"));
        dreadThrallItemConfig.setLeggingsNBT(List.of("{}"));
        dreadThrallItemConfig.setBoots(List.of("amethysttoolsmod:netherite_boots_amethyst"));
        dreadThrallItemConfig.setBootsNBT(List.of("{}"));
        dreadThrallItemConfig.setMainhand(List.of("iceandfire:dread_sword"));
        dreadThrallItemConfig.setMainhandNBT(List.of("{}"));
        dreadThrallItemConfig.setOffhand(List.of("upgradednetherite:corrupt_upgraded_netherite_shield"));
        dreadThrallItemConfig.setOffhandNBT(List.of("{}"));
        dreadThrallConfig.setItem(dreadThrallItemConfig);
        defaultConfigs.add(dreadThrallConfig);

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