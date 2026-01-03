package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCrescentEvent {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide() || event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (holdCrescent(serverPlayer)) {
                skillHydraDevour(serverPlayer);
            }
        }
    }

    public static boolean holdCrescent(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        ItemStack offHand = livingEntity.getOffhandItem();
        boolean holdCrescent = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "crescent")) ||
                ForgeRegistries.ITEMS.getKey(offHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "crescent"));
        return (!(mainHand.isEmpty()) || !(offHand.isEmpty())) && (holdCrescent);
    }

    @SubscribeEvent
    public static void skillHydraDevour(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (holdCrescent(serverPlayer)) {
                MobEffect paralysis = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("minecraft", "slowness"));
                MobEffect poison = MobEffects.POISON;
                serverPlayer.removeEffect(paralysis);
                serverPlayer.removeEffect(poison);
            }
        }
    }

    public static void skillHydraDevour(ServerPlayer attacker) {
        MobEffect paralysis = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("minecraft", "slowness"));
        MobEffect poison = MobEffects.POISON;
        double attackerX = attacker.getX();
        double attackerY = attacker.getY();
        double attackerZ = attacker.getZ();
        List<LivingEntity> nearbyEntities = attacker.level().getEntitiesOfClass(LivingEntity.class, new AABB(
                attackerX - 7, attackerY - 7, attackerZ - 7,
                attackerX + 7, attackerY + 7, attackerZ + 7
        ));
        for (LivingEntity nearbyEntity : nearbyEntities) {
            nearbyEntity.addEffect(new MobEffectInstance(paralysis, 30 * 20, 4));
            nearbyEntity.addEffect(new MobEffectInstance(poison, 30 * 20, 4));
        }
    }
}