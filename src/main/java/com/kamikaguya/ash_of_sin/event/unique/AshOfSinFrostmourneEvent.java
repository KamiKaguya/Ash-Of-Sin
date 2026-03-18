package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.damagesource.AshOfSinDamageSources;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinFrostmourneEvent {
    private static final String FROSTMOURNE_ATTACK_TAG = "frostmourne_frozen";
    private static final String FROSTMOURNE_FREEZE_TIME = "frostmourne_freeze_time";
    private static final Enchantment ENHANCE_ENCHANTMENT = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation("minecraft", "sweeping"));

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.getPersistentData().getBoolean(FROSTMOURNE_ATTACK_TAG)) {
                return;
            }

            LivingEntity target = event.getEntity();
            float originalDamage = event.getAmount();
            int freezingLevel = getEnchantmentLevel(serverPlayer, ENHANCE_ENCHANTMENT);
            if (holdFrostmourne(serverPlayer)) {
                event.setCanceled(true);
                serverPlayer.getPersistentData().putBoolean(FROSTMOURNE_ATTACK_TAG, true);
                target.hurt(AshOfSinDamageSources.frozen(serverPlayer), originalDamage);
                target.getPersistentData().putInt(FROSTMOURNE_FREEZE_TIME, 3 * 20);
                if (freezingLevel > 0) {
                    target.getPersistentData().putInt(FROSTMOURNE_FREEZE_TIME, (3 + 3 * freezingLevel) * 20);
                }

                serverPlayer.getPersistentData().remove(FROSTMOURNE_ATTACK_TAG);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        var data = entity.getPersistentData();
        if (!data.contains(FROSTMOURNE_FREEZE_TIME)) return;

        int timeLeft = data.getInt(FROSTMOURNE_FREEZE_TIME);
        if (timeLeft <= 0) {
            data.remove(FROSTMOURNE_FREEZE_TIME);
            return;
        }

        int current = entity.getTicksFrozen();
        entity.setTicksFrozen(current + 2);

        entity.makeStuckInBlock(Blocks.POWDER_SNOW.defaultBlockState(), new Vec3(0.9, 1.5, 0.9));

        if (entity instanceof Player player && player.level().isClientSide) {
            RandomSource random = player.level().random;
            if (random.nextBoolean()) {
                double x = player.getX();
                double y = player.getY() + 1.0;
                double z = player.getZ();
                player.level().addParticle(ParticleTypes.SNOWFLAKE, x, y, z,
                        Mth.nextFloat(random, -1.0F, 1.0F) * 0.083333336F,
                        0.05F,
                        Mth.nextFloat(random, -1.0F, 1.0F) * 0.083333336F);
            }
        }

        data.putInt(FROSTMOURNE_FREEZE_TIME, timeLeft - 1);
    }

    public static boolean holdFrostmourne(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdFrostmourne = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "frostmourne"));
        return !(mainHand.isEmpty()) && (holdFrostmourne);
    }

    public static int getEnchantmentLevel(LivingEntity entity, Enchantment enchantment) {
        if (enchantment == null) {
            return 0;
        }
        ItemStack mainHand = entity.getMainHandItem();
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, mainHand);
    }
}