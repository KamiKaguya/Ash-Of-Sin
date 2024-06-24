package com.kamikaguya.ash_of_sin.client.renderer.entity;

import com.kamikaguya.ash_of_sin.world.entity.Assassin;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class AssassinRenderer extends HumanoidMobRenderer<Assassin, HumanoidModel<Assassin>> {
    public AssassinRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
    }

    @Override
    public boolean shouldRender(Assassin livingEntity, Frustum frustum, double x, double y, double z) {
        double d = this.getBlockDistanceSq(livingEntity);
        double renderDistanceSq = 256 * 256;
        return d < renderDistanceSq;
    }

    public double getBlockDistanceSq(Entity entity) {
        double deltaX = entity.getX() - this.entityRenderDispatcher.camera.getPosition().x();
        double deltaY = entity.getY() - this.entityRenderDispatcher.camera.getPosition().y();
        double deltaZ = entity.getZ() - this.entityRenderDispatcher.camera.getPosition().z();

        return (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    public ResourceLocation getTextureLocation(Assassin entity) {
        return new ResourceLocation("ash_of_sin:textures/entity/assassin.png");
    }
}