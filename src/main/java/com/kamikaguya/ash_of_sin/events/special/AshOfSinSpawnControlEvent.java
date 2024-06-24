package com.kamikaguya.ash_of_sin.events.special;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.dimension.AbsoluteSpaceTimeRealmDimension;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinSpawnControlEvent {
    @SubscribeEvent
    public static void onEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
        if (!(event.getWorld() instanceof ServerLevel serverLevel)) {
            return;
        }

        ResourceKey<Level> currentDimensionKey = serverLevel.dimension();

        if (currentDimensionKey.equals(AbsoluteSpaceTimeRealmDimension.ABSOLUTE_SPACE_TIME_REALM)) {
            if (event.getSpawnReason() == MobSpawnType.NATURAL || event.getSpawnReason() == MobSpawnType.CHUNK_GENERATION) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}