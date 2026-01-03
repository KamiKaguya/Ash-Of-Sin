package com.kamikaguya.ash_of_sin.world.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class AshOfSinDamageSource extends DamageSource {
    private final Set<TagKey<DamageType>> runtimeTags = new HashSet<>();
    private Vec3 initialPosition;

    private float baseArmorNegation;
    private float baseImpact;
    private boolean basicAttack;

    public AshOfSinDamageSource(DamageSource damageSource) {
        this(damageSource.typeHolder(), damageSource.getDirectEntity(), damageSource.getEntity(), damageSource.getSourcePosition());
    }
    public AshOfSinDamageSource(Holder<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 initialPosition) {
        super(damageType, directEntity, causingEntity, initialPosition);
        this.initialPosition = initialPosition;
    }

    public AshOfSinDamageSource setBaseArmorNegation(float f) {
        this.baseArmorNegation = f;
        return this;
    }

    public float getBaseArmorNegation() {
        return this.baseArmorNegation;
    }

    public AshOfSinDamageSource setBaseImpact(float f) {
        this.baseImpact = f;
        return this;
    }

    public float getBaseImpact() {
        return this.baseImpact;
    }

    public AshOfSinDamageSource setInitialPosition(Vec3 initialPosition) {
        this.initialPosition = initialPosition;
        return this;
    }

    public Vec3 getInitialPosition() {
        return initialPosition;
    }

    public AshOfSinDamageSource setBasicAttack(boolean basicAttack) {
        this.basicAttack = basicAttack;
        return this;
    }

    public boolean isBasicAttack() {
        return basicAttack;
    }

    @Override
    public boolean is(TagKey<DamageType> type) {
        return this.runtimeTags.contains(type) || super.is(type);
    }

    public AshOfSinDamageSource addRuntimeTag(TagKey<DamageType> type) {
        this.runtimeTags.add(type);
        return this;
    }

    public AshOfSinDamageSource setExecute() {
        this.runtimeTags.add(AshOfSinDamageTypeTags.EXECUTION);
        this.runtimeTags.add(DamageTypeTags.BYPASSES_ARMOR);
        this.runtimeTags.add(DamageTypeTags.BYPASSES_ENCHANTMENTS);

        return this;
    }
}