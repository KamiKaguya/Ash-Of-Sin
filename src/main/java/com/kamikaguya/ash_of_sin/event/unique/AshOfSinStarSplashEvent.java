package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinStarSplashEvent {
    private static final UUID ATTACK_SPEED_BONUS_UUID = UUID.fromString("a3a3a3a3-b2b2-c2c2-d2d2-e2e2e2e2e2e2");

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            if (isAsuna(serverPlayer)) {
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

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof ServerPlayer serverPlayer) {
            AttributeInstance attackSpeed = serverPlayer.getAttribute(Attributes.ATTACK_SPEED);
            if (attackSpeed == null) return;

            attackSpeed.removeModifier(ATTACK_SPEED_BONUS_UUID);

            if (isAsuna(serverPlayer)) {
                double missingHealth = (serverPlayer.getMaxHealth() - serverPlayer.getHealth()) * 0.1;
                AttributeModifier modifier = new AttributeModifier(
                        ATTACK_SPEED_BONUS_UUID,
                        "Asuna attack speed bonus",
                        missingHealth,
                        AttributeModifier.Operation.ADDITION
                );
                attackSpeed.addTransientModifier(modifier);
            }
        }
    }

    public static boolean isAsuna(ServerPlayer serverPlayer) {
        ItemStack mainHand = serverPlayer.getMainHandItem();
        ItemStack offHand = serverPlayer.getOffhandItem();
        boolean holdAsunaWeapon = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "lambent_light"));
        return !(mainHand.isEmpty()) && (holdAsunaWeapon) && (offHand.isEmpty());
    }
}