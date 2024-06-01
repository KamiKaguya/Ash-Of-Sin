package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.AntiSameModifierConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinAntiSameModifierEvent {

    private static final List<Attribute> STANDARD_ATTRIBUTES = new ArrayList<>();

    static {
        STANDARD_ATTRIBUTES.add(Attributes.ATTACK_DAMAGE);
        STANDARD_ATTRIBUTES.add(Attributes.FOLLOW_RANGE);
        STANDARD_ATTRIBUTES.add(Attributes.ARMOR);
        STANDARD_ATTRIBUTES.add(Attributes.ARMOR_TOUGHNESS);
        STANDARD_ATTRIBUTES.add(Attributes.KNOCKBACK_RESISTANCE);
        STANDARD_ATTRIBUTES.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
        STANDARD_ATTRIBUTES.add(Attributes.ATTACK_KNOCKBACK);
        STANDARD_ATTRIBUTES.add(Attributes.ATTACK_SPEED);
        STANDARD_ATTRIBUTES.add(Attributes.FLYING_SPEED);
        STANDARD_ATTRIBUTES.add(Attributes.JUMP_STRENGTH);
        STANDARD_ATTRIBUTES.add(Attributes.LUCK);
        STANDARD_ATTRIBUTES.add(Attributes.MAX_HEALTH);
        STANDARD_ATTRIBUTES.add(Attributes.MOVEMENT_SPEED);
    }

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (AntiSameModifierConfig.ANTI_ON.get()) {
            if (!(event.getEntityLiving().level.isClientSide())) {
                LivingEntity livingEntity = event.getEntityLiving();

                if (livingEntity == null) {
                    return;
                }

                if (livingEntity instanceof KamiKaguya) {
                    return;
                }

                if (livingEntity instanceof Another) {
                    return;
                }

                if (livingEntity instanceof Doppelganger) {
                    return;
                }

                if (livingEntity instanceof Assassin) {
                    return;
                }

                if (livingEntity instanceof ServerPlayer) {
                    return;
                }

                for (Attribute attribute : STANDARD_ATTRIBUTES) {
                    AttributeInstance attributeInstance = livingEntity.getAttribute(attribute);

                    if (attributeInstance != null) {
                        Collection<AttributeModifier> modifiers = attributeInstance.getModifiers();

                        HashSet<String> existingNames = new HashSet<>();
                        List<AttributeModifier> modifiersToRemove = new ArrayList<>();

                        for (AttributeModifier modifier : modifiers) {
                            if (!existingNames.add(modifier.getName())) {
                                modifiersToRemove.add(modifier);
                            }
                        }

                        for (AttributeModifier modifier : modifiersToRemove) {
                            attributeInstance.removeModifier(modifier);
                        }
                    }
                }
            }
        }
    }
}