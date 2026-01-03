package com.kamikaguya.ash_of_sin.event.engine;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.register.items.AshOfSinItemRegistry;
import net.minecraft.resources.ResourceLocation;
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
    }

    @SubscribeEvent
    public static void registerItemRenderer(PatchedRenderersEvent.RegisterItemRenderer event) {
        event.addItemRenderer(ResourceLocation.tryBuild(AshOfSin.MODID, AshOfSinItemRegistry.FLAME_KATANA_CARAVELLA.getId().getNamespace()), RenderFlameKatanaCaravella::new);
        event.addItemRenderer(ResourceLocation.tryBuild(AshOfSin.MODID, AshOfSinItemRegistry.YAMATO.getId().getNamespace()), RenderYamato::new);
        event.addItemRenderer(ResourceLocation.tryBuild(AshOfSin.MODID, AshOfSinItemRegistry.MURASAME.getId().getNamespace()), RenderMurasame::new);
    }
}