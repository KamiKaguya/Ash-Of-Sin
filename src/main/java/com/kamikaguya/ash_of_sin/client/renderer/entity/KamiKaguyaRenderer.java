package com.kamikaguya.ash_of_sin.client.renderer.entity;


import com.kamikaguya.ash_of_sin.client.renderer.AnimatedPlayerModel;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.entity.KamiKaguya;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.resources.ResourceLocation;

public class KamiKaguyaRenderer extends HumanoidMobRenderer<KamiKaguya, PlayerModel<KamiKaguya>>
{
    public KamiKaguyaRenderer(EntityRendererProvider.Context context) {
        super(context, new AnimatedPlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), false), 0.5f);
        addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
        addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        addLayer(new ArrowLayer<>(context, this));
        addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        addLayer(new ElytraLayer<>(this, context.getModelSet()));
        addLayer(new SpinAttackEffectLayer<>(this, context.getModelSet()));
        addLayer(new BeeStingerLayer<>(this));
    }

    public ResourceLocation getTextureLocation(KamiKaguya entity) {
        return new ResourceLocation(AshOfSin.MODID, "textures/entity/kamikaguya.png");
    }
}