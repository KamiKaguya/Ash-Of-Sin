package com.kamikaguya.ash_of_sin.events.special;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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
public class AshOfSinSculkEvent {
    public static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof LivingEntity livingEntity) {
            if (RANDOM.nextFloat() < 0.15F && holdSculkWeapon(livingEntity)) {
                float originalDamage = event.getAmount();
                if (RANDOM.nextFloat() > 0.75F) {
                    float sculkDamage = originalDamage * 1.25F;
                    diffuseDarkness(target, livingEntity, sculkDamage);
                    event.setAmount(sculkDamage);
                } else {
                    float sculkDamage = originalDamage * 2.0F;
                    diffuseDarkness(target, livingEntity, sculkDamage);
                    event.setAmount(sculkDamage);
                }
                SoundEvent darknessSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("wildbackport:block.sculk_shrieker.shriek"));
                if (darknessSound != null) {
                    target.level.playSound(null, target.getOnPos(), darknessSound, SoundSource.NEUTRAL, 1.0f, 1.0f);
                }
            }
        }
    }

    public static boolean holdSculkWeapon(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean hasSculkWeapon = (mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "sculk_axe"))) || (mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "sculk_sword"))) || (mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "sculk_greatsword")));
        return !(mainHand.isEmpty()) && (hasSculkWeapon);
    }

    @SubscribeEvent
    public static void immuneDarkness(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntityLiving();
        MobEffect darkness = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("wildbackport", "darkness"));
        if (holdSculkWeapon(livingEntity)) {
            if (darkness != null) {
                livingEntity.removeEffect(darkness);
            }
        }
    }

    public static void diffuseDarkness(LivingEntity target, LivingEntity attacker, float sculkDamege) {
        MobEffect darknessEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("wildbackport", "darkness"));
        if (darknessEffect != null) {
            boolean alreadyPlungedIntoDarkness = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(darknessEffect) && existingEffect.getAmplifier() >= 0);
            boolean alreadyPlungedIntoDeepestDarkness = target.getActiveEffects().stream()
                    .anyMatch(existingEffect -> existingEffect.getEffect().equals(darknessEffect) && existingEffect.getAmplifier() == 2);
            if (!alreadyPlungedIntoDarkness) {
                MobEffectInstance darkness = new MobEffectInstance(darknessEffect, 33 * 20, 0);
                target.addEffect(darkness);
            } else {
                int amplifier = target.getEffect(darknessEffect).getAmplifier();
                if (!alreadyPlungedIntoDeepestDarkness) {
                    MobEffectInstance darkness = new MobEffectInstance(darknessEffect, 33 * 20, amplifier + 1);
                    target.addEffect(darkness);
                } else {
                    EntityType<?> livingEntityType = target.getType();
                    double targetX = target.getX();
                    double targetY = target.getY();
                    double targetZ = target.getZ();
                    List<LivingEntity> nearbyEntities = target.level.getEntitiesOfClass(LivingEntity.class, new AABB(
                            targetX - 7, targetY - 7, targetZ - 7,
                            targetX + 7, targetY + 7, targetZ + 7
                    ));
                    for (LivingEntity nearbyEntity : nearbyEntities) {
                        EntityType<?> dummy = ForgeRegistries.ENTITIES.getValue(new ResourceLocation("dummmmmmy:target_dummy"));
                        if (livingEntityType.equals(dummy)) {
                            return;
                        }
                        if (livingEntityType.equals(nearbyEntity.getType())) {
                            if (!holdSculkWeapon(nearbyEntity)) {
                                if (RANDOM.nextFloat() <= 0.25F) {
                                    float sonicBoomDamage = sculkDamege * 1.25F;
                                    nearbyEntity.hurt(DamageSource.mobAttack(attacker).setMagic(), sonicBoomDamage);
                                } else {
                                    float sonicBoomDamage = sculkDamege * 2.0F;
                                    nearbyEntity.hurt(DamageSource.mobAttack(attacker).setMagic(), sonicBoomDamage);
                                }
                                MobEffectInstance darkness = new MobEffectInstance(darknessEffect, 33 * 20, 2);
                                nearbyEntity.addEffect(darkness);
                                SoundEvent sonicBoomSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("wildbackport:entity.warden.sonic_boom"));
                                if (sonicBoomSound != null) {
                                    nearbyEntity.level.playSound(null, nearbyEntity.getOnPos(), sonicBoomSound, SoundSource.NEUTRAL, 1.0f, 1.0f);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}