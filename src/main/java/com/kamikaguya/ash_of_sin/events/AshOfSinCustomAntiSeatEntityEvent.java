package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomAntiSeatEntityConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomAntiSeatEntityEvent {

    @SubscribeEvent
    public static void onEntityMountEvent(EntityMountEvent event) {
        if (CustomAntiSeatEntityConfig.ANTI_ON.get()) {
            if (!event.isCanceled() && event.isMounting() && !event.getEntityBeingMounted().getLevel().isClientSide()) {
                Entity entityBeingMounted = event.getEntityBeingMounted();
                EntityType<?> entityMountingType = event.getEntityMounting().getType();
                ResourceLocation entityResourceLocation = EntityType.getKey(entityMountingType);

                List<? extends String> antiSeatEntityList = CustomAntiSeatEntityConfig.ANTI_SEAT_ENTITY.get();

                if (antiSeatEntityList.contains(entityResourceLocation.toString())) {
                    BlockPos entityPos = entityBeingMounted.blockPosition();
                    entityBeingMounted.getLevel().destroyBlock(entityPos, true);
                    entityBeingMounted.discard();
                }
            }
        }
    }
}