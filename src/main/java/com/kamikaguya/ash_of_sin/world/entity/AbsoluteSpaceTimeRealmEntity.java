package com.kamikaguya.ash_of_sin.world.entity;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public interface AbsoluteSpaceTimeRealmEntity {
    LivingEntity getOwner();

    default boolean isAlliedHelper(Entity entity) {
        if (this.getOwner() == null) {
            return false;
        } else {
            boolean var10000;
            boolean isFellowSummon;
            label28: {
                isFellowSummon = entity == this.getOwner() || entity.isAlliedTo(this.getOwner());
                if (entity instanceof OwnableEntity) {
                    OwnableEntity ownableEntity = (OwnableEntity)entity;
                    if (ownableEntity.getOwner() == this.getOwner()) {
                        var10000 = true;
                        break label28;
                    }
                }

                var10000 = false;
            }

            boolean hasCommonOwner = var10000;
            return isFellowSummon || hasCommonOwner;
        }
    }

    default void onDeathHelper() {
        if (this instanceof LivingEntity entity) {
            Level level = entity.level;
            Component deathMessage = entity.getCombatTracker().getDeathMessage();
            if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
                LivingEntity var5 = this.getOwner();
                if (var5 instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer)var5;
                    player.sendMessage(deathMessage, Util.NIL_UUID);
                }
            }
        }
    }
}