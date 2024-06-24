package com.kamikaguya.ash_of_sin.events.engine;

import com.kamikaguya.ash_of_sin.client.renderer.entity.KamiKaguyaRenderer;
import com.kamikaguya.ash_of_sin.client.renderer.entity.GateRenderer;
import com.kamikaguya.ash_of_sin.client.renderer.entity.DoppelgangerRenderer;
import com.kamikaguya.ash_of_sin.client.renderer.entity.AnotherRenderer;
import com.kamikaguya.ash_of_sin.client.renderer.entity.AssassinRenderer;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.AshOfSinEntities;
import com.kamikaguya.ash_of_sin.world.item.AshOfSinItems;
import net.minecraft.world.item.Item;
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
        event.registerEntityRenderer(AshOfSinEntities.KAMIKAGUYA.get(), KamiKaguyaRenderer::new);
        event.registerEntityRenderer(AshOfSinEntities.GATE.get(), GateRenderer::new);
        event.registerEntityRenderer(AshOfSinEntities.DOPPELGANGER.get(), DoppelgangerRenderer::new);
        event.registerEntityRenderer(AshOfSinEntities.ANOTHER.get(), AnotherRenderer::new);
        event.registerEntityRenderer(AshOfSinEntities.ASSASSIN.get(), AssassinRenderer::new);
    }

    @SubscribeEvent
    public static void registerItemRenderer(PatchedRenderersEvent.Add event) {
        event.addItemRenderer(AshOfSinItems.FLAME_KATANA_CARAVELLA.get(), new RenderFlameKatanaCaravella());
    }
}