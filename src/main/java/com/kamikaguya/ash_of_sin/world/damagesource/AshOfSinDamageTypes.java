package com.kamikaguya.ash_of_sin.world.damagesource;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public final class AshOfSinDamageTypes {
    private AshOfSinDamageTypes() {}

    public static final ResourceKey<DamageType> DEVOUR = ResourceKey.create(Registries.DAMAGE_TYPE, AshOfSin.identifier("devour"));
}