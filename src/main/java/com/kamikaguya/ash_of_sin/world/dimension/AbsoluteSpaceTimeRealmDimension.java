package com.kamikaguya.ash_of_sin.world.dimension;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;

public class AbsoluteSpaceTimeRealmDimension {
        public static final ResourceKey<Level> ABSOLUTE_SPACE_TIME_REALM = ResourceKey.create(Registry.DIMENSION_REGISTRY,
                new ResourceLocation(AshOfSin.MODID, "absolute_space_time_realm"));
        public static final ResourceKey<DimensionType> ABSOLUTE_SPACE_TIME_REALM_TYPE =
            ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, ABSOLUTE_SPACE_TIME_REALM.getRegistryName());

        public static void register() {
                System.out.println("Registering Absolute Space Time Realm for " + AshOfSin.MODID);
        }
}