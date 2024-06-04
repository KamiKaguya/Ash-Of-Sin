package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinMirrorOfTheDarkNightEvent {

    public static final List<Attribute> Armor_and_ArmorToughness = new ArrayList<>();

    static {
        Armor_and_ArmorToughness.add(Attributes.ARMOR);
        Armor_and_ArmorToughness.add(Attributes.ARMOR_TOUGHNESS);
    }

    public static final String ABSOLUTE_DEFENSE = "AbsoluteDefense";

    @SubscribeEvent
    public static void skillAbsoluteDefense(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = (LivingEntity) event.getEntity();
        CompoundTag livingentityData = livingEntity.getPersistentData();
        boolean hasAbsoluteDefense = livingentityData.getBoolean(ABSOLUTE_DEFENSE);
        for (Attribute attribute : Armor_and_ArmorToughness) {
            AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);
            AttributeModifier modifierAbsoluteDefense = new AttributeModifier("Absolute Defense", 2, AttributeModifier.Operation.MULTIPLY_BASE);
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                if (holdMirrorOfTheDarkNight(serverPlayer)) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        if ((attributeInstance != null) && !(hasAbsoluteDefense)) {
                            attributeInstance.addPermanentModifier(modifierAbsoluteDefense);
                            livingentityData.putBoolean(ABSOLUTE_DEFENSE, true);
                        }
                    }
                }
            }
            if (livingEntity instanceof Another another) {
                if (holdMirrorOfTheDarkNight(another)) {
                    if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                        if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                            if ((attributeInstance != null) && !(hasAbsoluteDefense)) {
                                attributeInstance.addPermanentModifier(modifierAbsoluteDefense);
                                livingentityData.putBoolean(ABSOLUTE_DEFENSE, true);
                            }
                        }
                    }
                } else {
                    if (attributeInstance != null && hasAbsoluteDefense) {
                        attributeInstance.removeModifier(modifierAbsoluteDefense);
                        livingentityData.putBoolean(ABSOLUTE_DEFENSE, false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void skillShielderMindset(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        DamageSource damageSource = event.getSource();
        LivingEntity livingEntity = (LivingEntity) event.getEntity();
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
    public static void skillVenenosaurusDevour(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = (LivingEntity) event.getEntity();
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (holdMirrorOfTheDarkNight(serverPlayer)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                    MobEffect poison = MobEffects.POISON;
                    serverPlayer.removeEffect(poison);
                }
            }
        }
        if (livingEntity instanceof Another another) {
            if (holdMirrorOfTheDarkNight(another)) {
                if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        MobEffect poison = MobEffects.POISON;
                        serverPlayer.removeEffect(poison);
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
        LivingEntity livingEntity = (LivingEntity) event.getEntity();
        if (targetEntity instanceof LivingEntity target) {
            if (livingEntity instanceof ServerPlayer serverPlayer) {
                if (holdMirrorOfTheDarkNight(serverPlayer)) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        serverPlayer.getCooldowns().removeCooldown(Items.SHIELD);
                        float devourOriginalDamage = target.getMaxHealth();
                        if (hasProtectionEnchantmentAromor(target, Enchantments.ALL_DAMAGE_PROTECTION)) {
                            float devourCorrectionDamage = damageAfterTargetArmorProtection(target.getArmorSlots(), devourOriginalDamage);
                            target.hurt(new EntityDamageSource("Devour", serverPlayer).setMagic(), devourCorrectionDamage);
                        } else {
                            target.hurt(new EntityDamageSource("Devour", serverPlayer).setMagic(), devourOriginalDamage);
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
                                target.hurt(new EntityDamageSource("Devour", serverPlayer).setMagic(), devourCorrectionDamage);
                            } else {
                                target.hurt(new EntityDamageSource("Devour", serverPlayer).setMagic(), devourOriginalDamage);
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

    private static boolean holdMirrorOfTheDarkNight(LivingEntity livingEntity) {
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

    private static boolean hasProtectionEnchantmentAromor(LivingEntity livingEntity, Enchantment enchantment) {
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
