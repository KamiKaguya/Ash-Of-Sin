package com.kamikaguya.ash_of_sin.tracker;

import net.minecraft.world.entity.LivingEntity;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AshOfSinPlayerAttackTargetTracker {
    public static final Map<UUID, SoftReference<LivingEntity>> ownerTargets = new HashMap<>();

    public static LivingEntity getOwnerTarget(UUID ownerUUID) {
        SoftReference<LivingEntity> targetReference = ownerTargets.get(ownerUUID);
        return targetReference != null ? targetReference.get() : null;
    }

    public static void updateOwnerTarget(UUID playerUUID, LivingEntity newTarget) {
        if (newTarget != null) {
            ownerTargets.put(playerUUID, new SoftReference<>(newTarget));
        } else {
            ownerTargets.remove(playerUUID);
        }
    }

    public static void removeOwnerTarget(UUID playerUUID) {
        ownerTargets.remove(playerUUID);
    }
}