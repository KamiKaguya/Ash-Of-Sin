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

public class AntiHighLevelEnchantmentConfig {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Type CONFIG_TYPE = new TypeToken<List<AntiHighLevelEnchantmentLevelConfig>>() {}.getType();
    public List<AntiHighLevelEnchantmentLevelConfig> antiHighLevelEnchantmentConfig;
    public final Path configPath;

    public AntiHighLevelEnchantmentConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/anti_high_level_enchantment.json");
        initializeDefaultConfig();
    }

    public void initializeDefaultConfig() {
        if (!Files.exists(configPath)) {
            List<AntiHighLevelEnchantmentLevelConfig> defaultAntiHighLevelEnchantmentConfig = Arrays.asList(
                    new AntiHighLevelEnchantmentLevelConfig("ash_of_sin:absolute_rule", 21),
                    new AntiHighLevelEnchantmentLevelConfig("ash_of_sin:another", 2),
                    new AntiHighLevelEnchantmentLevelConfig("ash_of_sin:chalk_wall", 4),
                    new AntiHighLevelEnchantmentLevelConfig("difficultraids:critical_burst", 5),
                    new AntiHighLevelEnchantmentLevelConfig("difficultraids:critical_strike", 9),
                    new AntiHighLevelEnchantmentLevelConfig("modification_of_critical_hit:critchance", 5),
                    new AntiHighLevelEnchantmentLevelConfig("modification_of_critical_hit:criteffect", 3),
                    new AntiHighLevelEnchantmentLevelConfig("minecraft:blast_protection", 4),
                    new AntiHighLevelEnchantmentLevelConfig("apotheosis:berserkers_fury", 4)
            );
            String defaultConfigJson = GSON.toJson(defaultAntiHighLevelEnchantmentConfig, CONFIG_TYPE);
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
            this.antiHighLevelEnchantmentConfig = GSON.fromJson(reader, CONFIG_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<AntiHighLevelEnchantmentLevelConfig> getAntiHighLevelEnchantmentConfig() {
        return antiHighLevelEnchantmentConfig;
    }
}