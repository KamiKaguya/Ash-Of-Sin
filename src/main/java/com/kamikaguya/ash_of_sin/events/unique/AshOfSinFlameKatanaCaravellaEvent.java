package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.events.special.AshOfSinBindingEvent;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinFlameKatanaCaravellaEvent {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        Entity entity = damageSource.getEntity();
        if (entity instanceof LivingEntity attacker) {
            if (holdFlameKatanaCaravella(attacker)) {
                float originalDamage = event.getAmount();
                float flameDamage = originalDamage * 0.2F;
                if (attacker instanceof ServerPlayer serverPlayer) {
                    if (!(AshOfSinBindingEvent.mismatchingPlayerHoldUniqueWeapon(serverPlayer))) {
                        target.hurt(DamageSource.LAVA, flameDamage);
                        target.setSecondsOnFire(15);
                    }
                }
            }
        }
    }

    public static boolean holdFlameKatanaCaravella(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdFlameKatanaCaravella = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "flame_katana_caravella"));
        return !(mainHand.isEmpty()) && (holdFlameKatanaCaravella);
    }
}