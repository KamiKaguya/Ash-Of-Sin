package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCrescentEvent {
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = (LivingEntity) event.getEntity();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof LivingEntity livingEntity) {
            if (holdCrescent(livingEntity)) {
                if (livingEntity instanceof ServerPlayer player && !(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(player))) {
                    float originalDamage = event.getAmount();
                    crescent(target, livingEntity, originalDamage);
                    if (RANDOM.nextFloat() < 0.15F) {
                        target.hurt(DamageSource.playerAttack(player).setMagic(), originalDamage);
                        Shock(target);
                    }
                    if (RANDOM.nextFloat() < 0.25F) {
                        float bonusDamage = originalDamage * 2.0F;
                        target.hurt(DamageSource.playerAttack(player).setMagic(), bonusDamage);
                        Shock(target);
                    }
                }
            }
        }
    }

    private static boolean holdCrescent(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdCrescent = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "crescent"));
        if (!(mainHand.isEmpty()) && (holdCrescent)) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void immuneShock(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntityLiving();
        MobEffect shock = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("simple_mobs", "shock"));
        if (holdCrescent(livingEntity)) {
            if (shock != null) {
                if (livingEntity instanceof ServerPlayer player && !(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(player))) {
                    player.removeEffect(shock);
                }
            }
        }
    }

    private static void Shock(LivingEntity target) {
        MobEffect shock = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("simple_mobs", "shock"));
        if (shock != null) {
            MobEffectInstance shockEffect = new MobEffectInstance(shock, 5 * 20, 0);
            target.addEffect(shockEffect);
        }
    }

    private static void crescent(LivingEntity target, LivingEntity attacker, float damage) {
        MobEffect glowing = MobEffects.GLOWING;
        boolean alreadyCrescent = target.getActiveEffects().stream()
                .anyMatch(existingEffect -> existingEffect.getEffect().equals(glowing) && existingEffect.getAmplifier() >= 0);
        if (!alreadyCrescent) {
            target.addEffect(new MobEffectInstance(glowing, 7 * 20, 0));
        } else {
            EntityType<?> livingEntityType = target.getType();
            double targetX = target.getX();
            double targetY = target.getY();
            double targetZ = target.getZ();
            List<LivingEntity> nearbyEntities = target.level.getEntitiesOfClass(LivingEntity.class, new AABB(
                    targetX - 3, targetY - 3, targetZ - 3,
                    targetX + 3, targetY + 3, targetZ + 3
            ));
            for (LivingEntity nearbyEntity : nearbyEntities) {
                if (livingEntityType.equals(nearbyEntity.getType())) {
                    if (RANDOM.nextFloat() > 0.295F) {
                        nearbyEntity.addEffect(new MobEffectInstance(glowing, 7 * 20, 0));
                        nearbyEntity.hurt(DamageSource.mobAttack(attacker), damage);
                    }
                }
            }
        }
    }
}
