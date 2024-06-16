package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.BetterAIConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinBetterAIEvent {

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (BetterAIConfig.BETTER_AI_ON.get()) {
            if (event.getEntityLiving().level.isClientSide()) {
                return;
            }
            LivingEntity livingEntity = event.getEntityLiving();
            if (livingEntity instanceof Mob) {
                Mob mob = (Mob) event.getEntityLiving();
                Entity target = event.getSource().getDirectEntity();
                GoalSelector goalSelector = mob.targetSelector;
                if (!(mob.level.isClientSide()) && target instanceof LivingEntity) {
                    mob.setTarget(null);
                    if (mob.getLastHurtByMobTimestamp() > 20) {
                        mob.setTarget((LivingEntity) target);
                    }
                }
            }
        }
    }
}