package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinMurasameEvent {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        LivingEntity target = event.getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (holdMurasame(serverPlayer)) {
                instaKill(target);
            }
        }
    }

    public static boolean holdMurasame(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdMurasame = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "murasame")) ||
                ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "murasame_katana"));
        return !(mainHand.isEmpty()) && (holdMurasame);
    }

    public static void instaKill(LivingEntity livingEntity) {
        MobEffect infection = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("tensura", "infection"));
        livingEntity.addEffect(new MobEffectInstance(infection, 30 * 20, 12));
    }
}