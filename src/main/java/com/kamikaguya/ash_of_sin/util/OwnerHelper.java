package com.kamikaguya.ash_of_sin.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class OwnerHelper {
    public OwnerHelper() {
    }

    public static LivingEntity getAndCacheOwner(Level level, LivingEntity cachedOwner, UUID ownerUUID) {
        if (cachedOwner != null && cachedOwner.isAlive()) {
            return cachedOwner;
        } else if (ownerUUID != null && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            Entity var5 = serverLevel.getEntity(ownerUUID);
            if (var5 instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)var5;
                cachedOwner = livingEntity;
            }

            return cachedOwner;
        } else {
            return null;
        }
    }

    public static void serializeOwner(CompoundTag compoundTag, UUID ownerUUID) {
        if (ownerUUID != null) {
            compoundTag.putUUID("Another", ownerUUID);
        }

    }

    public static UUID deserializeOwner(CompoundTag compoundTag) {
        return compoundTag.hasUUID("Another") ? compoundTag.getUUID("Another") : null;
    }
}
