package com.kamikaguya.ash_of_sin.world.biome;

import net.minecraft.world.level.biome.*;

public class AshOfSinBiomes {

    private static Biome biome(Biome.Precipitation precipitation, float temperature, float downfall, int waterColor, int waterFogColor)
    {
        return biome(precipitation, temperature, downfall, 4159204, 329011);
    }

    public static Biome absoluteSpaceTimeRealm() {
        return biome(Biome.Precipitation.RAIN, 0.2F, 0.5F, 0xA89557, 0xC67F5B);
    }
}