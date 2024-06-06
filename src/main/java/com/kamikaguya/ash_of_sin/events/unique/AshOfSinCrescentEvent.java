package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
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
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (holdCrescent(serverPlayer)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                    skillHydraDevour(serverPlayer);
                }
            }
        }
    }

    private static boolean holdCrescent(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdCrescent = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "crescent"));
        if (!(mainHand.isEmpty()) && (holdCrescent)) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void skillHydraDevour(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            if (holdCrescent(serverPlayer)) {
                if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                    MobEffect shock = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("simple_mobs", "shock"));
                    serverPlayer.removeEffect(MobEffects.POISON);
                    serverPlayer.removeEffect(shock);
                }
            }
        }
        if (livingEntity instanceof Another another) {
            if (holdCrescent(another)) {
                if (another.getOwner() instanceof ServerPlayer serverPlayer) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        MobEffect shock = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("simple_mobs", "shock"));
                        another.removeEffect(MobEffects.POISON);
                        another.removeEffect(shock);
                    }
                }
            }
        }
    }

    public static void skillHydraDevour(ServerPlayer attacker) {
        MobEffect shock = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("simple_mobs", "shock"));
        MobEffect poison = MobEffects.POISON;
        double attackerX = attacker.getX();
        double attackerY = attacker.getY();
        double attackerZ = attacker.getZ();
        List<LivingEntity> nearbyEntities = attacker.level.getEntitiesOfClass(LivingEntity.class, new AABB(
                attackerX - 7, attackerY - 7, attackerZ - 7,
                attackerX + 7, attackerY + 7, attackerZ + 7
        ));
        for (LivingEntity nearbyEntity : nearbyEntities) {
            if (nearbyEntity == attacker || nearbyEntity instanceof Another another && another.getOwner() == attacker) {
                    return;
            }
            nearbyEntity.addEffect(new MobEffectInstance(shock, 30 * 20, 0));
            nearbyEntity.addEffect(new MobEffectInstance(poison, 30 * 20, 2));
        }
    }
}
