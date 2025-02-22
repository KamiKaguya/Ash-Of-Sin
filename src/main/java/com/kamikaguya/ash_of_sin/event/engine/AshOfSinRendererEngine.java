package com.kamikaguya.ash_of_sin.event.engine;

import com.kamikaguya.ash_of_sin.client.renderer.entity.*;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.register.entity.AshOfSinEntityTypes;
import com.kamikaguya.ash_of_sin.register.items.AshOfSinItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;

@EventBusSubscriber(modid = AshOfSin.MODID , bus = Bus.MOD, value = Dist.CLIENT)
public class AshOfSinRendererEngine {
    public AshOfSinRendererEngine() {
    }

    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AshOfSinEntityTypes.KAMIKAGUYA.get(), KamiKaguyaRenderer::new);
        event.registerEntityRenderer(AshOfSinEntityTypes.GATE.get(), GateRenderer::new);
    }

    @SubscribeEvent
    public static void registerItemRenderer(PatchedRenderersEvent.Add event) {
        event.addItemRenderer(AshOfSinItems.FLAME_KATANA_CARAVELLA.get(), new RenderFlameKatanaCaravella());
        event.addItemRenderer(AshOfSinItems.YAMATO.get(), new RenderYamato());
        event.addItemRenderer(AshOfSinItems.MURASAME.get(), new RenderMurasame());
    }
}