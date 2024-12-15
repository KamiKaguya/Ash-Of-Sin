package com.kamikaguya.ash_of_sin.damage;

import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.scores.Team;

public class DamageSources {
    public DamageSources() {
    }

    public static boolean applyDamage(Entity target, float baseAmount, DamageSource damageSource) {
        if (target instanceof LivingEntity livingTarget) {
            float adjustedDamage = baseAmount;
            boolean fromAbsoluteSpaceTimeRealm = false;
            Entity var8 = damageSource.getDirectEntity();
            if (var8 instanceof Another another) {
                fromAbsoluteSpaceTimeRealm = true;
                if (another.getOwner() != null) {
                    adjustedDamage = (float)((double)adjustedDamage * another.getOwner().getAttributeValue(Attributes.LUCK));
                }
            }

            var8 = damageSource.getEntity();
            if (var8 instanceof LivingEntity livingAttacker) {
                if (isFriendlyFireBetween(livingAttacker, livingTarget)) {
                    return false;
                }

                livingAttacker.setLastHurtMob(target);
            }

            boolean flag = livingTarget.hurt(damageSource, adjustedDamage);
            if (fromAbsoluteSpaceTimeRealm) {
                livingTarget.setLastHurtByMob((LivingEntity)damageSource.getDirectEntity());
            }

            return flag;
        } else {
            return target.hurt(damageSource, baseAmount);
        }
    }

    public static boolean isFriendlyFireBetween(Entity attacker, Entity target) {
        if (attacker != null && target != null) {
            Team team = attacker.getTeam();
            if (team == null) {
                return false;
            } else {
                return team.isAlliedTo(target.getTeam()) && !team.isAllowFriendlyFire();
            }
        } else {
            return false;
        }
    }
}