package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.gameasset.AshOfSinSounds;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinMirrorOfTheDarkNightEvent {

    public static final String DEVOUR_CD = "DevourCD";
    public static final String DEVOUR_CHANCE = "DevourChance";

    @SubscribeEvent
    public static void skillShielderMindset(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        DamageSource damageSource = event.getSource();
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (holdMirrorOfTheDarkNight(serverPlayer)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                    float originalDamage = event.getAmount();
                    float afterReductionDamage = originalDamage * 0.9F;
                    event.setAmount(afterReductionDamage);
                }
            }
        }
        if (livingEntity instanceof Another another) {
            if (holdMirrorOfTheDarkNight(another)) {
                if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        float originalDamage = event.getAmount();
                        float afterReductionDamage = originalDamage * 0.9F;
                        event.setAmount(afterReductionDamage);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void resetSkillDevourCD(TickEvent.WorldTickEvent worldTickEvent, LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        if (worldTickEvent.phase == TickEvent.Phase.END) {
            LivingEntity livingEntity = livingUpdateEvent.getEntityLiving();
            CompoundTag livingEntityData = livingEntity.getPersistentData();
            boolean hasSkillDevourCD = livingEntityData.getBoolean(DEVOUR_CD);
            float skillDevourChance = livingEntityData.getFloat(DEVOUR_CHANCE);
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                if (holdMirrorOfTheDarkNight(serverPlayer)) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        if (hasSkillDevourCD && skillDevourChance == 0) {
                            livingEntityData.putBoolean(DEVOUR_CD, false);
                            livingEntityData.putFloat(DEVOUR_CHANCE, 10);
                        }
                    }
                }
            }
            if (livingEntity instanceof Another another) {
                if (holdMirrorOfTheDarkNight(another)) {
                    if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                        if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                            if (hasSkillDevourCD && skillDevourChance == 0) {
                                livingEntityData.putBoolean(DEVOUR_CD, false);
                                livingEntityData.putFloat(DEVOUR_CHANCE, 10);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void skillDevourChanceUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntityLiving();
        CompoundTag livingEntityData = livingEntity.getPersistentData();
        if (!livingEntityData.contains(DEVOUR_CHANCE, Tag.TAG_FLOAT)) {
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                if (holdMirrorOfTheDarkNight(serverPlayer)) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        livingEntityData.putFloat(DEVOUR_CHANCE, 10);
                    }
                }
            }
            if (livingEntity instanceof Another another) {
                if (holdMirrorOfTheDarkNight(another)) {
                    if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                        if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                            livingEntityData.putFloat(DEVOUR_CHANCE, 10);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void skillDevour(ShieldBlockEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Entity targetEntity = event.getDamageSource().getEntity();
        LivingEntity livingEntity = event.getEntityLiving();
        CompoundTag livingEntityData = livingEntity.getPersistentData();
        boolean skillDevourCD = livingEntityData.getBoolean(DEVOUR_CD);
        float skillDevourChance = livingEntityData.getFloat(DEVOUR_CHANCE);
        if (targetEntity instanceof LivingEntity target) {
            MobEffect sunderingEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","sundering"));
            MobEffectInstance sundering = new MobEffectInstance(sunderingEffect, 7 * 20, 98);
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                if (holdMirrorOfTheDarkNight(serverPlayer)) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        if (!skillDevourCD && skillDevourChance > 0) {
                            serverPlayer.getCooldowns().removeCooldown(Items.SHIELD);
                            float devourOriginalDamage = target.getMaxHealth();
                            if (hasProtectionEnchantmentAromor(target, Enchantments.ALL_DAMAGE_PROTECTION)) {
                                float devourCorrectionDamage = damageAfterTargetArmorProtection(target.getArmorSlots(), devourOriginalDamage);
                                target.addEffect(sundering);
                                target.hurt(new EntityDamageSource("Devour", serverPlayer).setMagic(), devourCorrectionDamage);
                                serverPlayer.level.playSound(null, serverPlayer.getOnPos(), AshOfSinSounds.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                                serverPlayer.heal(devourCorrectionDamage);
                                livingEntityData.putFloat(DEVOUR_CHANCE, skillDevourChance - 1);
                            } else {
                                target.addEffect(sundering);
                                target.hurt(new EntityDamageSource("Devour", serverPlayer).setMagic(), devourOriginalDamage);
                                serverPlayer.level.playSound(null, serverPlayer.getOnPos(), AshOfSinSounds.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                                serverPlayer.heal(devourOriginalDamage);
                                livingEntityData.putFloat(DEVOUR_CHANCE, skillDevourChance - 1);
                            }
                        } else {
                            livingEntityData.putBoolean(DEVOUR_CD, true);
                        }
                    }
                }
            }

            if (livingEntity instanceof Another another) {
                if (holdMirrorOfTheDarkNight(another)) {
                    if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                        if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                            if (!skillDevourCD && skillDevourChance > 0) {
                                float devourOriginalDamage = target.getMaxHealth();
                                if (hasProtectionEnchantmentAromor(target, Enchantments.ALL_DAMAGE_PROTECTION)) {
                                    float devourCorrectionDamage = damageAfterTargetArmorProtection(target.getArmorSlots(), devourOriginalDamage);
                                    target.addEffect(sundering);
                                    target.hurt(new EntityDamageSource("Devour", serverPlayer).setMagic(), devourCorrectionDamage);
                                    another.level.playSound(null, serverPlayer.getOnPos(), AshOfSinSounds.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                                    another.heal(devourCorrectionDamage);
                                    serverPlayer.heal(devourCorrectionDamage);
                                    livingEntityData.putFloat(DEVOUR_CHANCE, skillDevourChance - 1);
                                } else {
                                    target.addEffect(sundering);
                                    target.hurt(new EntityDamageSource("Devour", serverPlayer).setMagic(), devourOriginalDamage);
                                    another.level.playSound(null, serverPlayer.getOnPos(), AshOfSinSounds.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                                    another.heal(devourOriginalDamage);
                                    serverPlayer.heal(devourOriginalDamage);
                                    livingEntityData.putFloat(DEVOUR_CHANCE, skillDevourChance - 1);
                                }
                            } else {
                                livingEntityData.putBoolean(DEVOUR_CD, true);
                            }
                        }
                    }
                }
            }
        }

        if (targetEntity instanceof Arrow arrow) {
            arrow.kill();
        }

        if (targetEntity != null && targetEntity.isInvulnerable()) {
            targetEntity.kill();
        }
    }

    public static boolean holdMirrorOfTheDarkNight(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        ItemStack offHand = livingEntity.getOffhandItem();
        boolean holdMirrorOfTheDarkNight = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "mirror_of_the_dark_night")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "mirror_of_the_dark_night"));
        if (!(mainHand.isEmpty()) && (holdMirrorOfTheDarkNight)) {
            return true;
        }
        if (!(offHand.isEmpty()) && (holdMirrorOfTheDarkNight)) {
            return true;
        }
        return false;
    }

    public static boolean hasProtectionEnchantmentAromor(LivingEntity livingEntity, Enchantment enchantment) {
        Iterable<ItemStack> armors = livingEntity.getArmorSlots();
        for (ItemStack stack : armors) {
            if (EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack) > 0) {
                return true;
            }
        }
        return false;
    }

    public static float damageAfterTargetArmorProtection(Iterable<ItemStack> armorItems, float originalDamage) {
        float damageAfterArmorProtection = 0;
        float correctionDamage = 0;
        for (ItemStack armorItem : armorItems) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armorItem);
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof ProtectionEnchantment) {
                    int protectLevel = entry.getValue();

                    damageAfterArmorProtection += originalDamage * (10 / (protectLevel + 10.0f));
                    correctionDamage += originalDamage - damageAfterArmorProtection;
                }
            }
        }
        return correctionDamage;
    }
}
