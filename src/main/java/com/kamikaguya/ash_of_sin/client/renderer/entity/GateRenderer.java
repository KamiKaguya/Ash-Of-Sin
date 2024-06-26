package com.kamikaguya.ash_of_sin.client.renderer.entity;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Gate;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GateRenderer extends EntityRenderer<Gate> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(AshOfSin.MODID, "textures/entity/gate.png");

	public GateRenderer(EntityRendererProvider.Context mgr) {
		super(mgr);
	}

	@Override
	public ResourceLocation getTextureLocation(Gate entity) {
		return TEXTURE;
	}

	@Override
	public void render(Gate gate, float yaw, float partialTicks, PoseStack matrix, MultiBufferSource buf, int packedLight) {
		matrix.pushPose();

		Player player = Minecraft.getInstance().player;
		Vec3 playerPosition = player.getEyePosition(partialTicks);

		Vec3 entityPosition = gate.position();

		Vec3 toPlayer = playerPosition.subtract(entityPosition).normalize();

		float horizontalAngle = (float) Math.toDegrees(Math.atan2(toPlayer.z, toPlayer.x)) + 90.0F;
		float verticalAngle = (float) Math.toDegrees(Math.atan2(toPlayer.y, Math.sqrt(toPlayer.x * toPlayer.x + toPlayer.z * toPlayer.z)));

		matrix.mulPose(Vector3f.XP.rotationDegrees(-verticalAngle));
		matrix.mulPose(Vector3f.YP.rotationDegrees(horizontalAngle));
		matrix.mulPose(Vector3f.YP.rotationDegrees(90.0F));

		float baseScale = 1.0f;
		double yOffset = gate.getBbHeight() / 2.0;
		matrix.scale(baseScale, baseScale, baseScale);
		matrix.translate(0, yOffset, 0);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, this.getTextureLocation(gate));

		VertexConsumer builder = buf.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(gate)));

		float frameHeight = 1 / 9F;
		int frame = gate.tickCount % 9;
		renderQuad(matrix, builder, packedLight, frameHeight, frame);

		matrix.popPose();
	}

	public void renderQuad(PoseStack matrix, VertexConsumer builder, int packedLight, float frameHeight, int frame) {
		builder.vertex(matrix.last().pose(), -1, -1, 0).color(255, 192, 203, 255).uv(1, 1 - frame * frameHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix.last().normal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.last().pose(), -1, 1, 0).color(255, 192, 203, 255).uv(1, 8F / 9 - frame * frameHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix.last().normal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.last().pose(), 1, 1, 0).color(255, 192, 203, 255).uv(0, 8F / 9 - frame * frameHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix.last().normal(), 0, 1, 0).endVertex();
		builder.vertex(matrix.last().pose(), 1, -1, 0).color(255, 192, 203, 255).uv(0, 1 - frame * frameHeight).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix.last().normal(), 0, 1, 0).endVertex();
	}

	@Override
	public boolean shouldRender(Gate Entity, Frustum frustum, double x, double y, double z) {
		double d = this.getBlockDistanceSq(Entity);
		double renderDistanceSq = 256 * 256;
		return d < renderDistanceSq;
	}

	public double getBlockDistanceSq(Entity entity) {
		double deltaX = entity.getX() - this.entityRenderDispatcher.camera.getPosition().x();
		double deltaY = entity.getY() - this.entityRenderDispatcher.camera.getPosition().y();
		double deltaZ = entity.getZ() - this.entityRenderDispatcher.camera.getPosition().z();

		return (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
	}

}