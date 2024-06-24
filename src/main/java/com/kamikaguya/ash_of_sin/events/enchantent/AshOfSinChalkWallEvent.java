package com.kamikaguya.ash_of_sin.events.enchantent;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.events.unique.AshOfSinMirrorOfTheDarkNightEvent;
import com.kamikaguya.ash_of_sin.gameasset.AshOfSinSounds;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinChalkWallEvent {

    public static final String CHALK_WALL_DURATION = "ChalkWallD";
    public static final String CHALK_WALL_CD = "ChalkWallCD";
    public static final String CHALK_WALL = "ChalkWall";

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        LivingEntity attacker = (LivingEntity) damageSource.getEntity();
        if (damageSource.getDirectEntity() instanceof Another another && (another.getCustomName() != null) && another.getCustomName().equals(livingEntity)) {
            return;
        }

        if (attacker instanceof Another another && (another.getCustomName() != null) && another.getCustomName().equals(livingEntity)) {
            return;
        }

        CompoundTag livingEntityData = livingEntity.getPersistentData();
        boolean inChalkWallCD = livingEntityData.getBoolean(CHALK_WALL_CD);

        if (inChalkWallCD) {
            return;
        }

        float originalDamage = event.getAmount();
        float damageAfterArmorReduction = damageAfterArmor(livingEntity, originalDamage);
        float damageAfterArmorProtection = damageAfterArmorProtection(livingEntity.getArmorSlots(),damageAfterArmorReduction);
        boolean isTriggered = livingEntity.getHealth() <= livingEntity.getMaxHealth() * 0.05
                || damageAfterArmorProtection >= livingEntity.getHealth();
        if (!isTriggered) {
            return;
        }

        int enchantmentLevel = getEnchantmentLevel(livingEntity, AshOfSin.CHALK_WALL.get());
        if (enchantmentLevel > 3) {
            return;
        }

        if (attacker != null && AshOfSinMirrorOfTheDarkNightEvent.holdMirrorOfTheDarkNight(attacker) && attacker instanceof ServerPlayer serverPlayer && !(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
            return;
        }

        if (attacker != null && AshOfSinMirrorOfTheDarkNightEvent.holdMirrorOfTheDarkNight(attacker) && attacker instanceof Another another && another.getOwner() instanceof ServerPlayer serverPlayer && !(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
            return;
        }

        if (enchantmentLevel > 0) {
            consumeDurabilityBasedOnEnchantmentLevel(livingEntity, AshOfSin.CHALK_WALL.get());
            float chalkWallDuration = enchantmentLevel * 3 * 20;
            livingEntityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration);
            livingEntityData.putBoolean(CHALK_WALL, true);
            livingEntityData.putBoolean(CHALK_WALL_CD, true);
            livingEntity.removeAllEffects();
        }

        boolean hasChalkWall = livingEntityData.getBoolean(CHALK_WALL);
        if (hasChalkWall) {
            event.setAmount(0);
        }
    }

    public static float damageAfterArmor(LivingEntity entity, float baseDamage) {
        float armorValue = entity.getArmorValue();
        float toughnessValue = (float) entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        return damageAfterArmorReduction(armorValue, toughnessValue, baseDamage);
    }

    public static float damageAfterArmorReduction(float armorValue, float toughnessValue, float baseDamage) {
        float damageAfterArmorReduction;
        if (baseDamage <= 1.6 * armorValue + 0.2 * toughnessValue) {
            damageAfterArmorReduction = (1 / (6.25f * toughnessValue + 50f)) * baseDamage * baseDamage
                    + (1f - armorValue / 25f) * baseDamage;
        } else {
            damageAfterArmorReduction = (1f - armorValue / 125f) * baseDamage;
        }
        return damageAfterArmorReduction;
    }

    public static float damageAfterArmorProtection(Iterable<ItemStack> armorItems, float damageAfterArmorReduction) {
        float damageAfterArmorProtection = damageAfterArmorReduction;
        for (ItemStack armorItem : armorItems) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armorItem);
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof ProtectionEnchantment) {
                    int protectLevel = entry.getValue();

                    damageAfterArmorProtection = damageAfterArmorReduction * (10 / ((protectLevel + 10.0f) / 2));
                }
            }
        }
        return damageAfterArmorProtection;
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntityLiving();
        CompoundTag entityData = livingEntity.getPersistentData();
        boolean hasChalkWall = entityData.getBoolean(CHALK_WALL);

        if (hasChalkWall) {
            float chalkWallDuration = entityData.getFloat(CHALK_WALL_DURATION);
            if (chalkWallDuration > 0) {
                livingEntity.setHealth(livingEntity.getMaxHealth() * 0.25F);
                if (livingEntity instanceof ServerPlayer player) {
                    player.getFoodData().setFoodLevel(16);
                    if (chalkWallDuration == 9 * 20 ||
                            chalkWallDuration == 8 * 20 ||
                            chalkWallDuration == 7 * 20 ||
                            chalkWallDuration == 6 * 20 ||
                            chalkWallDuration == 5 * 20 ||
                            chalkWallDuration == 4 * 20 ||
                            chalkWallDuration == 3 * 20 ||
                            chalkWallDuration == 2 * 20 ||
                            chalkWallDuration == 20) {
                        player.level.playSound(player, player.getOnPos(), AshOfSinSounds.TICK_TACK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                    }
                }

                if (chalkWallDuration > 6 * 20 && chalkWallDuration < 9 * 20 && livingEntity instanceof ServerPlayer player) {
                    player.displayClientMessage(new TranslatableComponent("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withBold(true)), true);
                }

                if (chalkWallDuration > 3 * 20 && chalkWallDuration < 6 * 20 && livingEntity instanceof ServerPlayer player) {
                    player.displayClientMessage(new TranslatableComponent("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withBold(true)), true);
                }

                if (chalkWallDuration < 3 * 20 && livingEntity instanceof ServerPlayer player) {
                    player.displayClientMessage(new TranslatableComponent("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.RED).withBold(true)), true);
                }

                entityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration - 1);
            }
        }

        boolean inChalkWallCD = entityData.getBoolean(CHALK_WALL_CD);
        float chalkWallDuration = entityData.getFloat(CHALK_WALL_DURATION);

        if (chalkWallDuration <= 0 && hasChalkWall) {
            livingEntity.removeAllEffects();
            float absorptionHealth = livingEntity.getMaxHealth() * 0.15F;
            livingEntity.setAbsorptionAmount(absorptionHealth);
            entityData.putBoolean(CHALK_WALL, false);
        }

        if (inChalkWallCD && chalkWallDuration <= 0) {
            entityData.putFloat(CHALK_WALL_DURATION, 0);
            if (livingEntity.getHealth() >= livingEntity.getMaxHealth()) {
                entityData.putBoolean(CHALK_WALL_CD, false);
            }
        }
    }

    @SubscribeEvent
    public static void onDied(LivingDeathEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity entity = event.getEntityLiving();
        CompoundTag entityData = entity.getPersistentData();
        DamageSource damageSource = event.getSource();
        LivingEntity attacker = (LivingEntity) damageSource.getEntity();

        if (attacker != null && AshOfSinMirrorOfTheDarkNightEvent.holdMirrorOfTheDarkNight(attacker) && attacker instanceof ServerPlayer serverPlayer && !(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
            return;
        }

        if (attacker != null && AshOfSinMirrorOfTheDarkNightEvent.holdMirrorOfTheDarkNight(attacker) && attacker instanceof Another another && another.getOwner() instanceof ServerPlayer serverPlayer && !(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
            return;
        }

        int enchantmentLevel = getEnchantmentLevel(entity, AshOfSin.CHALK_WALL.get());
        if (enchantmentLevel > 3) {
            return;
        }

        boolean inChalkWallCD = entityData.getBoolean(CHALK_WALL_CD);

        if ((enchantmentLevel > 0) && !(inChalkWallCD)) {
            consumeDurabilityBasedOnEnchantmentLevel(entity, AshOfSin.CHALK_WALL.get());
            int chalkWallDuration = enchantmentLevel * 3 * 20;
            entityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration);
            entityData.putBoolean(CHALK_WALL, true);
            entityData.putBoolean(CHALK_WALL_CD, true);
            entity.removeAllEffects();
            event.setCanceled(true);
        }

        if (inChalkWallCD) {
            entityData.putBoolean(CHALK_WALL_CD, false);
        }


        boolean hasChalkWall = entityData.getBoolean(CHALK_WALL);
        if (hasChalkWall) {
            event.setCanceled(true);
        }
    }

    public static int getEnchantmentLevel(LivingEntity entity, Enchantment enchantment) {
        Iterable<ItemStack> armors = entity.getArmorSlots();
        int level = 0;
        for (ItemStack stack : armors) {
            level += EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
        }
        return level;
    }

    public static void consumeDurabilityBasedOnEnchantmentLevel(LivingEntity entity, Enchantment enchantment) {
        Iterable<ItemStack> armors = entity.getArmorSlots();
        for (ItemStack stack : armors) {
            if (!stack.isEmpty() && getSoloEnchantmentLevel(entity, enchantment, stack) > 0) {
                int maxDamage = stack.getMaxDamage();
                int enchantmentLevel = getSoloEnchantmentLevel(entity, enchantment, stack);
                int damageDealt = (int) (maxDamage * 0.25 * enchantmentLevel);
                int itemDamage = stack.getDamageValue();
                int realityDamageDealt = itemDamage - damageDealt;
                stack.setDamageValue(realityDamageDealt);
            }
        }
    }

    public static int getSoloEnchantmentLevel(LivingEntity entity, Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        CompoundTag entityData = player.getPersistentData();
        entityData.remove(CHALK_WALL);
        entityData.remove(CHALK_WALL_DURATION);
    }
}