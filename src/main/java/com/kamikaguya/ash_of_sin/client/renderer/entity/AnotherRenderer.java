package com.kamikaguya.ash_of_sin.client.renderer.entity;

import com.kamikaguya.ash_of_sin.client.renderer.AnimatedPlayerModel;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class AnotherRenderer extends HumanoidMobRenderer<Another, PlayerModel<Another>> {

    public AnotherRenderer(EntityRendererProvider.Context context) {
        super(context, new AnimatedPlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), false), 0.5f);
        addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
        addLayer(new ItemInHandLayer<>(this));
        addLayer(new ArrowLayer<>(context, this));
        addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
        addLayer(new ElytraLayer<>(this, context.getModelSet()));
        addLayer(new SpinAttackEffectLayer<>(this, context.getModelSet()));
        addLayer(new BeeStingerLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Another entity) {
        UUID uuid = entity.getUUID();
        if (uuid != null) {
            PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);
            return (info == null ? DefaultPlayerSkin.getDefaultSkin(uuid) : info.getSkinLocation());
        }
        return DefaultPlayerSkin.getDefaultSkin();
    }
}