package com.kamikaguya.ash_of_sin.client;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.item.AshOfSinItems;
import com.kamikaguya.ash_of_sin.world.item.SubCravenBowItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinClientSetup {
    public static void clientInit() {

    }
    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemPropertyFunction pulling = ItemProperties.getProperty(Items.BOW, new ResourceLocation("pulling"));
            ItemPropertyFunction pull = (stack, worldIn, entity, p) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    SubCravenBowItem subCravenBowitem = ((SubCravenBowItem) stack.getItem());
                    return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                }
            };
            ItemProperties.register(AshOfSinItems.SUB_CRAVEN_BOW.get().asItem(), new ResourceLocation("pulling"), pulling);
            ItemProperties.register(AshOfSinItems.SUB_CRAVEN_BOW.get().asItem(), new ResourceLocation("pull"), pull);
        });
    }
}