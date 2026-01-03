package com.kamikaguya.ash_of_sin.event.enchantent;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.register.enchantment.AshOfSinEnchantments;
import com.kamikaguya.ash_of_sin.register.sound.AshOfSinSoundEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
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
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntity();
        Enchantment enchantment = AshOfSinEnchantments.CHALK_WALL.get();
        Iterable<ItemStack> armors = event.getEntity().getArmorSlots();
        for (ItemStack stack : armors) {
            if (!stack.isEmpty() && getSoloEnchantmentLevel(livingEntity, enchantment, stack) > 0) {
                return;
            }
        }

        CompoundTag livingEntityData = livingEntity.getPersistentData();
        boolean inChalkWallCD = livingEntityData.getBoolean(CHALK_WALL_CD);
        if (inChalkWallCD) {
            return;
        }

        float originalDamage = event.getAmount();
        float damageAfterArmorReduction = damageAfterArmor(livingEntity, originalDamage);
        float damageAfterArmorProtection = damageAfterArmorProtection(livingEntity.getArmorSlots(), damageAfterArmorReduction);
        boolean isTriggered = livingEntity.getHealth() <= livingEntity.getMaxHealth() * 0.05
                || damageAfterArmorProtection >= livingEntity.getHealth();
        if (!isTriggered) {
            return;
        }

        int enchantmentLevel = getEnchantmentLevel(livingEntity, AshOfSinEnchantments.CHALK_WALL.get());
        if (enchantmentLevel == 1) {
            consumeDurabilityBasedOnEnchantmentLevel(livingEntity, AshOfSinEnchantments.CHALK_WALL.get());
            float chalkWallDuration = 1 * 3 * 20;
            livingEntityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration);
            livingEntityData.putBoolean(CHALK_WALL, true);
            livingEntityData.putBoolean(CHALK_WALL_CD, true);
            livingEntity.removeAllEffects();
            livingEntity.setInvulnerable(true);
        }

        if (enchantmentLevel == 2) {
            consumeDurabilityBasedOnEnchantmentLevel(livingEntity, AshOfSinEnchantments.CHALK_WALL.get());
            float chalkWallDuration = 2 * 3 * 20;
            livingEntityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration);
            livingEntityData.putBoolean(CHALK_WALL, true);
            livingEntityData.putBoolean(CHALK_WALL_CD, true);
            livingEntity.removeAllEffects();
            livingEntity.setInvulnerable(true);
        }

        if (enchantmentLevel == 3) {
            consumeDurabilityBasedOnEnchantmentLevel(livingEntity, AshOfSinEnchantments.CHALK_WALL.get());
            float chalkWallDuration = 3 * 3 * 20;
            livingEntityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration);
            livingEntityData.putBoolean(CHALK_WALL, true);
            livingEntityData.putBoolean(CHALK_WALL_CD, true);
            livingEntity.removeAllEffects();
            livingEntity.setInvulnerable(true);
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
        float damageAfterToughnessReduction;
        if (toughnessValue == 0) {
            damageAfterArmorReduction = (baseDamage * Math.max(10 / (10 + armorValue), 0.2f));
        } else {
            if (baseDamage > (40 / (toughnessValue + 1))) {
                damageAfterToughnessReduction = baseDamage - ((40 / (toughnessValue + 1)) / 2);
                damageAfterArmorReduction = (damageAfterToughnessReduction * Math.max(10 / (10 + armorValue), 0.2f));
            } else {
                damageAfterToughnessReduction = baseDamage - (40 / (toughnessValue + 1));
                damageAfterArmorReduction = (damageAfterToughnessReduction * Math.max(10 / (10 + armorValue), 0.2f));
            }
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

                    damageAfterArmorProtection = damageAfterArmorReduction * (10 / (10.0f + protectLevel));
                }
            }
        }
        return damageAfterArmorProtection;
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntity();
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
                        player.level().playSound(player, player.getOnPos(), AshOfSinSoundEvent.TICK_TACK.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                    }
                }

                if (chalkWallDuration > 6 * 20 && chalkWallDuration < 9 * 20 && livingEntity instanceof ServerPlayer player) {
                    player.displayClientMessage(Component.translatable("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withBold(true)), true);
                }

                if (chalkWallDuration > 3 * 20 && chalkWallDuration < 6 * 20 && livingEntity instanceof ServerPlayer player) {
                    player.displayClientMessage(Component.translatable("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withBold(true)), true);
                }

                if (chalkWallDuration < 3 * 20 && livingEntity instanceof ServerPlayer player) {
                    player.displayClientMessage(Component.translatable("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.RED).withBold(true)), true);
                }

                entityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration - 1);
            }
        }

        boolean inChalkWallCD = entityData.getBoolean(CHALK_WALL_CD);
        float chalkWallDuration = entityData.getFloat(CHALK_WALL_DURATION);

        if (chalkWallDuration <= 0 && hasChalkWall) {
            livingEntity.removeAllEffects();
            livingEntity.setInvulnerable(false);
            float absorptionHealth = livingEntity.getMaxHealth() * 0.15F;
            livingEntity.setAbsorptionAmount(absorptionHealth);
            entityData.putBoolean(CHALK_WALL, false);
        }

        if (inChalkWallCD && chalkWallDuration <= 0) {
            entityData.putFloat(CHALK_WALL_DURATION, 0);
            if (livingEntity.getHealth() >= livingEntity.getMaxHealth() ) {
                if (livingEntity instanceof ServerPlayer player && player.getFoodData().getFoodLevel() >= 20) {
                    entityData.putBoolean(CHALK_WALL_CD, false);
                } else if (livingEntity.getAbsorptionAmount() == 0){
                    entityData.putBoolean(CHALK_WALL_CD, false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDied(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity entity = event.getEntity();
        CompoundTag entityData = entity.getPersistentData();

        int enchantmentLevel = getEnchantmentLevel(entity, AshOfSinEnchantments.CHALK_WALL.get());

        boolean inChalkWallCD = entityData.getBoolean(CHALK_WALL_CD);

        if ((enchantmentLevel == 1) && !(inChalkWallCD)) {
            consumeDurabilityBasedOnEnchantmentLevel(entity, AshOfSinEnchantments.CHALK_WALL.get());
            int chalkWallDuration = 1 * 3 * 20;
            entityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration);
            entityData.putBoolean(CHALK_WALL, true);
            entityData.putBoolean(CHALK_WALL_CD, true);
            entity.removeAllEffects();
            event.setCanceled(true);
        }

        if ((enchantmentLevel == 2) && !(inChalkWallCD)) {
            consumeDurabilityBasedOnEnchantmentLevel(entity, AshOfSinEnchantments.CHALK_WALL.get());
            int chalkWallDuration = 2 * 3 * 20;
            entityData.putFloat(CHALK_WALL_DURATION, chalkWallDuration);
            entityData.putBoolean(CHALK_WALL, true);
            entityData.putBoolean(CHALK_WALL_CD, true);
            entity.removeAllEffects();
            event.setCanceled(true);
        }

        if ((enchantmentLevel == 3) && !(inChalkWallCD)) {
            consumeDurabilityBasedOnEnchantmentLevel(entity, AshOfSinEnchantments.CHALK_WALL.get());
            int chalkWallDuration = 3 * 3 * 20;
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
        ServerPlayer player = (ServerPlayer) event.getEntity();
        CompoundTag entityData = player.getPersistentData();
        entityData.remove(CHALK_WALL);
        entityData.remove(CHALK_WALL_DURATION);
    }
}