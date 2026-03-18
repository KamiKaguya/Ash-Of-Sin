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
public class AshOfSinStarBurstStreamEvent {
    private static final UUID DAMAGE_BONUS_UUID = UUID.fromString("a2a2a2a2-b1b1-c1c1-d1d1-e1e1e1e1e1e1");

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
            if (isKirito(serverPlayer)) {
                    MobEffect haste = MobEffects.DIG_SPEED;
                    boolean alreadyDualBlades = serverPlayer.getActiveEffects().stream()
                            .anyMatch(existingEffect -> existingEffect.getEffect().equals(haste) && existingEffect.getAmplifier() >= 0);
                    boolean alreadyStarBurstStream = serverPlayer.getActiveEffects().stream()
                            .anyMatch(existingEffect -> existingEffect.getEffect().equals(haste) && existingEffect.getAmplifier() == 9);
                    if (!alreadyDualBlades) {
                        MobEffectInstance hasteEffect = new MobEffectInstance(haste, 7 * 20, 0);
                        serverPlayer.addEffect(hasteEffect);
                    } else {
                        int amplifier = serverPlayer.getEffect(haste).getAmplifier();
                        if (!alreadyStarBurstStream) {
                            MobEffectInstance hasteEffect = new MobEffectInstance(haste, 7 * 20, amplifier + 1);
                            serverPlayer.addEffect(hasteEffect);
                        } else {
                            MobEffectInstance hasteEffect = new MobEffectInstance(haste, 13 * 20, 9);
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
            AttributeInstance attackDamage = serverPlayer.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage == null) return;

            attackDamage.removeModifier(DAMAGE_BONUS_UUID);

            if (isKirito(serverPlayer)) {
                double missingHealth = serverPlayer.getMaxHealth() - serverPlayer.getHealth();
                AttributeModifier modifier = new AttributeModifier(
                        DAMAGE_BONUS_UUID,
                        "Kirito damage bonus",
                        missingHealth,
                        AttributeModifier.Operation.ADDITION
                );
                attackDamage.addTransientModifier(modifier);
            }
        }
    }

    public static boolean isKirito(ServerPlayer serverPlayer) {
        ItemStack mainHand = serverPlayer.getMainHandItem();
        ItemStack offHand = serverPlayer.getOffhandItem();
        boolean holdKiritoWeapon = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "elucidator")) &&
                (ForgeRegistries.ITEMS.getKey(offHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "dark_repulser")) ||
                        ForgeRegistries.ITEMS.getKey(offHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "lambent_light")));
        return !(mainHand.isEmpty()) && (holdKiritoWeapon);
    }
}