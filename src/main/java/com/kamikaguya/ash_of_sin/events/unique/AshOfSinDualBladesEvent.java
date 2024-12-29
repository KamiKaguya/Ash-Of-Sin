package com.kamikaguya.ash_of_sin.events.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinDualBladesEvent {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (holdElucidatorOrDarkRepulser(serverPlayer)) {
                    MobEffect haste = MobEffects.DIG_SPEED;
                    boolean alreadyDualBlades = serverPlayer.getActiveEffects().stream()
                            .anyMatch(existingEffect -> existingEffect.getEffect().equals(haste) && existingEffect.getAmplifier() >= 0);
                    boolean alreadyStarBurstStream = serverPlayer.getActiveEffects().stream()
                            .anyMatch(existingEffect -> existingEffect.getEffect().equals(haste) && existingEffect.getAmplifier() == 4);
                    if (!alreadyDualBlades) {
                        MobEffectInstance hasteEffect = new MobEffectInstance(haste, 7 * 20, 0);
                        serverPlayer.addEffect(hasteEffect);
                    } else {
                        int amplifier = serverPlayer.getEffect(haste).getAmplifier();
                        if (!alreadyStarBurstStream) {
                            MobEffectInstance hasteEffect = new MobEffectInstance(haste, 7 * 20, amplifier + 1);
                            serverPlayer.addEffect(hasteEffect);
                        } else {
                            MobEffectInstance hasteEffect = new MobEffectInstance(haste, 13 * 20, 4);
                            serverPlayer.addEffect(hasteEffect);
                        }
                    }
            }
        }
    }

    public static boolean holdElucidatorOrDarkRepulser(ServerPlayer serverPlayer) {
        ItemStack mainHand = serverPlayer.getMainHandItem();
        boolean holdElucidatorOrDarkRepulser = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "elucidator")) ||
                ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "dark_repulser"));
        return !(mainHand.isEmpty()) && (holdElucidatorOrDarkRepulser);
    }
}