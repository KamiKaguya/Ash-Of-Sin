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
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Type CONFIG_TYPE = new TypeToken<List<CustomEntityItemConfig>>() {}.getType();
    private List<CustomEntityItemConfig> customEntityItemConfig;
    private final Path configPath;

    public CustomEntityItemConfigManager() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/custom_entity_item.json");
        loadConfig();
    }

    private void initializeDefaultConfig() {
        List<CustomEntityItemConfig> defaultConfigs = new ArrayList<>();

        CustomEntityItemConfig dreadKnightConfig = new CustomEntityItemConfig();
        dreadKnightConfig.setEntity(List.of("iceandfire:dread_knight"));
        CustomItemConfig CustomItemsConfig01 = new CustomItemConfig();
        CustomItemsConfig01.setHelmet(List.of());
        CustomItemsConfig01.setHelmetNBT(List.of("{}"));
        CustomItemsConfig01.setChestplate(List.of());
        CustomItemsConfig01.setChestplateNBT(List.of("{}"));
        CustomItemsConfig01.setLeggings(List.of());
        CustomItemsConfig01.setLeggingsNBT(List.of("{}"));
        CustomItemsConfig01.setBoots(List.of());
        CustomItemsConfig01.setBootsNBT(List.of("{}"));
        CustomItemsConfig01.setMainhand(List.of("iceandfire:dread_knight_sword"));
        CustomItemsConfig01.setMainhandNBT(List.of("{}"));
        CustomItemsConfig01.setOffhand(List.of("upgradednetherite:corrupt_upgraded_netherite_shield"));
        CustomItemsConfig01.setOffhandNBT(List.of("{}"));
        dreadKnightConfig.setItem(CustomItemsConfig01);
        defaultConfigs.add(dreadKnightConfig);

        CustomEntityItemConfig dreadThrallConfig = new CustomEntityItemConfig();
        dreadThrallConfig.setEntity(List.of("iceandfire:dread_thrall"));
        CustomItemConfig CustomItemsConfig02 = new CustomItemConfig();
        CustomItemsConfig02.setHelmet(List.of("amethysttoolsmod:netherite_helmet_amethyst"));
        CustomItemsConfig02.setHelmetNBT(List.of("{}"));
        CustomItemsConfig02.setChestplate(List.of("amethysttoolsmod:netherite_chestplate_amethyst"));
        CustomItemsConfig02.setChestplateNBT(List.of("{}"));
        CustomItemsConfig02.setLeggings(List.of("amethysttoolsmod:netherite_leggings_amethyst"));
        CustomItemsConfig02.setLeggingsNBT(List.of("{}"));
        CustomItemsConfig02.setBoots(List.of("amethysttoolsmod:netherite_boots_amethyst"));
        CustomItemsConfig02.setBootsNBT(List.of("{}"));
        CustomItemsConfig02.setMainhand(List.of("iceandfire:dread_sword"));
        CustomItemsConfig02.setMainhandNBT(List.of("{}"));
        CustomItemsConfig02.setOffhand(List.of("upgradednetherite:corrupt_upgraded_netherite_shield"));
        CustomItemsConfig02.setOffhandNBT(List.of("{}"));
        dreadThrallConfig.setItem(CustomItemsConfig02);
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