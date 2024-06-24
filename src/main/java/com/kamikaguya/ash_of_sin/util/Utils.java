package com.kamikaguya.ash_of_sin.util;

import com.kamikaguya.ash_of_sin.damage.DamageSources;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class Utils {
    public Utils() {
    }

    public static boolean doMeleeAttack(Mob attacker, Entity target, DamageSource damageSource) {
        float f = (float)attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (float)attacker.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(attacker.getMainHandItem(), ((LivingEntity)target).getMobType());
            f1 += (float)EnchantmentHelper.getKnockbackBonus(attacker);
        }

        int i = EnchantmentHelper.getFireAspect(attacker);
        if (i > 0) {
            target.setSecondsOnFire(i * 4);
        }

        boolean flag = DamageSources.applyDamage(target, f, damageSource);
        if (flag) {
            if (f1 > 0.0F && target instanceof LivingEntity livingTarget) {
                ((LivingEntity)target).knockback(f1 * 0.5F, Mth.sin(attacker.getYRot() * 0.017453292F), -Mth.cos(attacker.getYRot() * 0.017453292F));
                attacker.setDeltaMovement(attacker.getDeltaMovement().multiply(0.6, 1.0, 0.6));
                livingTarget.setLastHurtByMob(attacker);
            }

            if (target instanceof ServerPlayer player) {
                ItemStack aMobItemStack = attacker.getMainHandItem();
                ItemStack aPlayerItemStack = player.isUsingItem() ? player.getUseItem() : ItemStack.EMPTY;
                if (!aMobItemStack.isEmpty() && !aPlayerItemStack.isEmpty() && aMobItemStack.getItem() instanceof AxeItem && aPlayerItemStack.is(Items.SHIELD)) {
                    float f2 = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(attacker) * 0.05F;
                    if (attacker.getRandom().nextFloat() < f2) {
                        player.getCooldowns().addCooldown(Items.SHIELD, 100);
                        attacker.level.broadcastEntityEvent(player, (byte)30);
                    }
                }
            }

            attacker.doEnchantDamageEffects(attacker, target);
            attacker.setLastHurtMob(target);
        }

        return flag;
    }
}