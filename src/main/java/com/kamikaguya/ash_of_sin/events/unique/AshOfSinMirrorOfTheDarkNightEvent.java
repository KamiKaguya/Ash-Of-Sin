package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.gameasset.AshOfSinSounds;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinMirrorOfTheDarkNightEvent {
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
        if (targetEntity instanceof LivingEntity target) {
            MobEffect sunderingEffect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("apotheosis","sundering"));
            MobEffectInstance sundering = new MobEffectInstance(sunderingEffect, 7 * 20, 98);
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                if (holdMirrorOfTheDarkNight(serverPlayer)) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        serverPlayer.getCooldowns().removeCooldown(Items.SHIELD);
                        float devourOriginalDamage = target.getMaxHealth();
                        if (hasProtectionEnchantmentAromor(target, Enchantments.ALL_DAMAGE_PROTECTION)) {
                            float devourCorrectionDamage = damageAfterTargetArmorProtection(target.getArmorSlots(), devourOriginalDamage);
                            target.addEffect(sundering);
                            target.hurt(new EntityDamageSource("devour", serverPlayer).bypassArmor().bypassMagic().bypassInvul(), devourCorrectionDamage);
                            serverPlayer.level.playSound(null, serverPlayer.getOnPos(), AshOfSinSounds.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                            serverPlayer.heal(devourCorrectionDamage);
                            serverPlayer.getFoodData().setFoodLevel(20);
                        } else {
                            target.addEffect(sundering);
                            target.hurt(new EntityDamageSource("devour", serverPlayer).bypassArmor().bypassMagic().bypassInvul(), devourOriginalDamage);
                            serverPlayer.level.playSound(null, serverPlayer.getOnPos(), AshOfSinSounds.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                            serverPlayer.heal(devourOriginalDamage);
                            serverPlayer.getFoodData().setFoodLevel(20);
                        }
                    }
                }
            }

            if (livingEntity instanceof Another another) {
                if (holdMirrorOfTheDarkNight(another)) {
                    if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                        if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                            float devourOriginalDamage = target.getMaxHealth();
                            if (hasProtectionEnchantmentAromor(target, Enchantments.ALL_DAMAGE_PROTECTION)) {
                                float devourCorrectionDamage = damageAfterTargetArmorProtection(target.getArmorSlots(), devourOriginalDamage);
                                target.addEffect(sundering);
                                target.hurt(new EntityDamageSource("devour", serverPlayer).bypassArmor().bypassMagic().bypassInvul(), devourCorrectionDamage);
                                another.level.playSound(null, serverPlayer.getOnPos(), AshOfSinSounds.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                                another.heal(devourCorrectionDamage);
                                serverPlayer.heal(devourCorrectionDamage);
                                serverPlayer.getFoodData().setFoodLevel(20);
                            } else {
                                target.addEffect(sundering);
                                target.hurt(new EntityDamageSource("devour", serverPlayer).bypassArmor().bypassMagic().bypassInvul(), devourOriginalDamage);
                                another.level.playSound(null, serverPlayer.getOnPos(), AshOfSinSounds.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                                another.heal(devourOriginalDamage);
                                serverPlayer.heal(devourOriginalDamage);
                                serverPlayer.getFoodData().setFoodLevel(20);
                            }
                        }
                    }
                }
            }
        }

        if (targetEntity instanceof Arrow arrow) {
            arrow.kill();
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
        return !(offHand.isEmpty()) && (holdMirrorOfTheDarkNight);
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