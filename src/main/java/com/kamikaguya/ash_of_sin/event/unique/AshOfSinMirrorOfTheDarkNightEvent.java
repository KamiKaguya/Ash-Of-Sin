package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.register.sound.AshOfSinSoundEvent;
import com.kamikaguya.ash_of_sin.world.damagesource.AshOfSinDamageSources;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinMirrorOfTheDarkNightEvent {

    private static final String DEVOUR_DAMAGE = "devour_damage";

    @SubscribeEvent
    public static void skillShielderMindset(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        if (entity instanceof ServerPlayer player && holdMirrorOfTheDarkNight(player)) {
            float originalDamage = event.getAmount();
            event.setAmount(originalDamage * 0.9F);
        }
    }

    @SubscribeEvent
    public static void skillDevour(ShieldBlockEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        if (!holdMirrorOfTheDarkNight(serverPlayer)) return;

        Entity attacker = event.getDamageSource().getEntity();
        if (!(attacker instanceof LivingEntity target)) {
            if (attacker instanceof Arrow arrow) {
                arrow.kill();
            }
            return;
        }

        CompoundTag targetData = target.getPersistentData();
        if (targetData.contains(DEVOUR_DAMAGE)) {
            targetData.remove(DEVOUR_DAMAGE);
            return;
        }

        serverPlayer.getCooldowns().removeCooldown(Items.SHIELD);

        float maxHealth = target.getMaxHealth();

        target.setHealth(0.1f);

        targetData.putBoolean(DEVOUR_DAMAGE, true);
        try {
            target.hurt(AshOfSinDamageSources.devour(serverPlayer), Float.MAX_VALUE);
        } finally {
            targetData.remove(DEVOUR_DAMAGE);
        }

        serverPlayer.level().playSound(null, serverPlayer.getOnPos(),
                AshOfSinSoundEvent.SKILL_DEVOUR.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        serverPlayer.heal(maxHealth);
        serverPlayer.getFoodData().setFoodLevel(20);
    }

    public static boolean holdMirrorOfTheDarkNight(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        ItemStack offHand = livingEntity.getOffhandItem();
        ResourceLocation mirrorId = new ResourceLocation(AshOfSin.MODID, "mirror_of_the_dark_night");

        boolean mainHandValid = !mainHand.isEmpty() && ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(mirrorId);
        boolean offHandValid = !offHand.isEmpty() && ForgeRegistries.ITEMS.getKey(offHand.getItem()).equals(mirrorId);

        return mainHandValid || offHandValid;
    }
}