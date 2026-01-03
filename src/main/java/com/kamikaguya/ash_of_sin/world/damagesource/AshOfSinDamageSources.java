package com.kamikaguya.ash_of_sin.world.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class AshOfSinDamageSources {
    private AshOfSinDamageSources() {}

    public static AshOfSinDamageSource devour(LivingEntity owner) {
        return new AshOfSinDamageSource(getDamageTypeHolder(owner, AshOfSinDamageTypes.DEVOUR), owner, owner, null);
    }

    private static Holder<DamageType> getDamageTypeHolder(Entity entity, ResourceKey<DamageType> damageTypeKey) {
        return entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey);
    }
}