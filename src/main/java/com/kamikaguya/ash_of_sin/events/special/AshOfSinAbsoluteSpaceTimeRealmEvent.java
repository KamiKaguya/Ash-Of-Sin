package com.kamikaguya.ash_of_sin.events.special;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinAbsoluteSpaceTimeRealmEvent {
    @SubscribeEvent
    public static void inAbsoluteSpaceTimeRealm(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (event.getEntityLiving() instanceof ServerPlayer player) {
            ResourceLocation dimension = player.level.dimension().location();

            boolean isOP = "KamiKaguya".equals(player.getGameProfile().getName()) ||
                    "wangumao".equals(player.getGameProfile().getName()) ||
                    "Tahora".equals(player.getGameProfile().getName())||
                    "vita8356".equals(player.getGameProfile().getName())||
                    "Death_Leaves".equals(player.getGameProfile().getName());

            if("ash_of_sin:absolute_space_time_realm".equals(dimension.toString()) && player.gameMode.getGameModeForPlayer() != GameType.CREATIVE && !(isOP)) {
                player.setGameMode(GameType.ADVENTURE);
                player.getPersistentData().putBoolean("InAbsoluteSpaceTimeRealm", true);
            } else if(!"ash_of_sin:absolute_space_time_realm".equals(dimension.toString())) {
                if(player.getPersistentData().getBoolean("InAbsoluteSpaceTimeRealm")) {
                    player.setGameMode(GameType.SURVIVAL);
                    player.getPersistentData().remove("InAbsoluteSpaceTimeRealm");
                }
            }
        }
    }
}
