package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.AdventureDimensionConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinAdventureDimensionEvent {

    private static final List<? extends String> ADVENTURE_DIMENSION_ID = AdventureDimensionConfig.ADVENTURE_DIMENSION_ID.get();
    private static final List<? extends String> ADVENTURE_DIMENSION_ALLOW_PLAYER_ID = AdventureDimensionConfig.ADVENTURE_DIMENSION_ALLOW_PLAYER_ID.get();

    @SubscribeEvent
    public static void inAbsoluteSpaceTimeRealm(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayer player) {
            if (event.getEntityLiving().level.isClientSide()) {
                return;
            }
            ResourceLocation dimension = player.level.dimension().location();

            boolean isExceptionPlayer = ADVENTURE_DIMENSION_ALLOW_PLAYER_ID.contains(player.getGameProfile().getName());

            if(ADVENTURE_DIMENSION_ID.contains(dimension.toString()) && player.gameMode.getGameModeForPlayer() != GameType.CREATIVE && !(isExceptionPlayer)) {
                player.setGameMode(GameType.ADVENTURE);
                player.getPersistentData().putBoolean("InAdventureDimension", true);
            } else if(!(ADVENTURE_DIMENSION_ID.contains(dimension.toString()))) {
                if(player.getPersistentData().getBoolean("InAdventureDimension")) {
                    player.setGameMode(GameType.SURVIVAL);
                    player.getPersistentData().remove("InAdventureDimension");
                }
            }
        }
    }
}
