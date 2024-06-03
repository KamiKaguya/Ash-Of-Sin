package com.kamikaguya.ash_of_sin.events.enchantent;

import com.kamikaguya.ash_of_sin.gameasset.AshOfSinSounds;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinChalkWallEvent {

    public static final String CHALK_WALL_DURATION_KEY = "ChalkWallD";
    public static final String CHALK_WALL_CD_KEY = "ChalkWallCD";
    public static final String CHALK_WALL_KEY = "ChalkWall";

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity)) {
            return;
        }

        LivingEntity entity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        if (damageSource.getDirectEntity() instanceof Another another && (another.getCustomName() != null) && another.getCustomName().equals(entity)) {
            return;
        }

        if (damageSource.getEntity() instanceof Another another && (another.getCustomName() != null) && another.getCustomName().equals(entity)) {
            return;
        }

        CompoundTag entityData = entity.getPersistentData();
        boolean inChalkWallCD = entityData.getBoolean(CHALK_WALL_CD_KEY);

        if (inChalkWallCD) {
            return;
        }

        float originalDamage = event.getAmount();
        float damageAfterArmorReduction = damageAfterArmor(entity, originalDamage);
        float damageAfterArmorProtection = damageAfterArmorProtection(entity.getArmorSlots(),damageAfterArmorReduction);
        boolean isTriggered = entity.getHealth() <= entity.getMaxHealth() * 0.05
                || damageAfterArmorProtection >= entity.getHealth();
        if (!isTriggered) {
            return;
        }

        int enchantmentLevel = getEnchantmentLevel(entity, AshOfSin.CHALK_WALL.get());
        if (enchantmentLevel > 3) {
            return;
        }

        if (enchantmentLevel > 0) {
            consumeDurabilityBasedOnEnchantmentLevel(entity, AshOfSin.CHALK_WALL.get());
            int chalkWallDuration = enchantmentLevel * 3 * 20;
            entityData.putInt(CHALK_WALL_DURATION_KEY, chalkWallDuration);
            entityData.putBoolean(CHALK_WALL_KEY, true);
            entityData.putBoolean(CHALK_WALL_CD_KEY, true);
            entity.removeAllEffects();
        }

        boolean hasChalkWall = entityData.getBoolean(CHALK_WALL_KEY);
        if (hasChalkWall) {
            event.setCanceled(true);
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

                    damageAfterArmorProtection = damageAfterArmorReduction * (10 / (protectLevel + 10.0f));
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

        LivingEntity entity = event.getEntityLiving();
        CompoundTag entityData = entity.getPersistentData();
        boolean hasChalkWall = entityData.getBoolean(CHALK_WALL_KEY);

        if (hasChalkWall) {
            int chalkWallDuration = entityData.getInt(CHALK_WALL_DURATION_KEY);
            if (chalkWallDuration > 0) {
                entity.setHealth(entity.getMaxHealth() * 0.25F);
                entity.setInvulnerable(true);
                if (entity instanceof ServerPlayer player) {
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

                if (chalkWallDuration > 6 * 20 && chalkWallDuration < 9 * 20 && entity instanceof ServerPlayer player) {
                    player.displayClientMessage(new TranslatableComponent("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withBold(true)), true);
                }

                if (chalkWallDuration > 3 * 20 && chalkWallDuration < 6 * 20 && entity instanceof ServerPlayer player) {
                    player.displayClientMessage(new TranslatableComponent("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withBold(true)), true);
                }

                if (chalkWallDuration < 3 * 20 && entity instanceof ServerPlayer player) {
                    player.displayClientMessage(new TranslatableComponent("message.ash_of_sin.chalk_wall").setStyle(Style.EMPTY.withColor(ChatFormatting.RED).withBold(true)), true);
                }

                entityData.putInt(CHALK_WALL_DURATION_KEY, chalkWallDuration - 1);
            }
        }

        boolean inChalkWallCD = entityData.getBoolean(CHALK_WALL_CD_KEY);
        int chalkWallDuration = entityData.getInt(CHALK_WALL_DURATION_KEY);

        if (chalkWallDuration <= 0 && hasChalkWall) {
            float absorptionHealth = entity.getMaxHealth() * 0.15F;
            entity.setAbsorptionAmount(absorptionHealth);
            entityData.putBoolean(CHALK_WALL_KEY, false);
            entity.setInvulnerable(false);
        }

        if (inChalkWallCD && chalkWallDuration <= 0) {
            entityData.putInt(CHALK_WALL_DURATION_KEY, 0);
            if (entity.getHealth() >= entity.getMaxHealth()) {
                entityData.putBoolean(CHALK_WALL_CD_KEY, false);
            }
        }

        if (chalkWallDuration <= 0 && entity.isInvulnerable()) {
            entity.setInvulnerable(false);
        }
    }

    @SubscribeEvent
    public static void onDied(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity entity = event.getEntityLiving();
        CompoundTag entityData = entity.getPersistentData();

        if (!(event.getEntityLiving().level.isClientSide())) {

            int enchantmentLevel = getEnchantmentLevel(entity, AshOfSin.CHALK_WALL.get());
            if (enchantmentLevel > 3) {
                return;
            }

            boolean inChalkWallCD = entityData.getBoolean(CHALK_WALL_CD_KEY);

            if ((enchantmentLevel > 0) && !(inChalkWallCD)) {
                consumeDurabilityBasedOnEnchantmentLevel(entity, AshOfSin.CHALK_WALL.get());
                int chalkWallDuration = enchantmentLevel * 3 * 20;
                entityData.putInt(CHALK_WALL_DURATION_KEY, chalkWallDuration);
                entityData.putBoolean(CHALK_WALL_KEY, true);
                entityData.putBoolean(CHALK_WALL_CD_KEY, true);
                entity.removeAllEffects();
                event.setCanceled(true);
            }

            if (inChalkWallCD) {
                entityData.putBoolean(CHALK_WALL_CD_KEY, false);
            }
        }

        boolean hasChalkWall = entityData.getBoolean(CHALK_WALL_KEY);
        if (hasChalkWall) {
            event.setCanceled(true);
        }
    }

    private static int getEnchantmentLevel(LivingEntity entity, Enchantment enchantment) {
        Iterable<ItemStack> armors = entity.getArmorSlots();
        int level = 0;
        for (ItemStack stack : armors) {
            level += EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
        }
        return level;
    }

    private static void consumeDurabilityBasedOnEnchantmentLevel(LivingEntity entity, Enchantment enchantment) {
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

    private static int getSoloEnchantmentLevel(LivingEntity entity, Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        CompoundTag entityData = player.getPersistentData();
        entityData.remove(CHALK_WALL_KEY);
        entityData.remove(CHALK_WALL_DURATION_KEY);
    }
}