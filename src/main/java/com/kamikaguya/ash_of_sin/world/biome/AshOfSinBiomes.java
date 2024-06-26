package com.kamikaguya.ash_of_sin.world.biome;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.*;

public class AshOfSinBiomes {

    public static Biome createBiome() {
        BiomeGenerationSettings plainsGenSettings = BuiltinRegistries.BIOME.get(Biomes.PLAINS).getGenerationSettings();

        BiomeSpecialEffects plainsEffects = BuiltinRegistries.BIOME.get(Biomes.PLAINS).getSpecialEffects();

        MobSpawnSettings.Builder mobSpawnSettingsBuilder = new MobSpawnSettings.Builder();

        return (new Biome.BiomeBuilder())
                .temperature(BuiltinRegistries.BIOME.get(Biomes.PLAINS).getBaseTemperature())
                .downfall(BuiltinRegistries.BIOME.get(Biomes.PLAINS).getDownfall())
                .precipitation(BuiltinRegistries.BIOME.get(Biomes.PLAINS).getPrecipitation())
                .biomeCategory(Biome.BiomeCategory.PLAINS)
                .specialEffects(plainsEffects)
                .mobSpawnSettings(mobSpawnSettingsBuilder.build())
                .generationSettings(plainsGenSettings)
                .build();
    }
}