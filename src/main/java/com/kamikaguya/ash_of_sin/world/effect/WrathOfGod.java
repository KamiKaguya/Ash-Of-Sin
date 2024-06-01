package com.kamikaguya.ash_of_sin.world.effect;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;
import java.util.UUID;

public class WrathOfGod extends MobEffect {
    private static final UUID MODIFIER_ID = UUID.fromString("6e0c1304-ae8d-4c24-8099-d9e3f5a8e5ee");

    public WrathOfGod() {
        super(MobEffectCategory.HARMFUL, 0xFFC0CB);
        new ResourceLocation(AshOfSin.MODID, "textures/mob_effect/wrath_of_god.png");
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        AttributeInstance maxHealthAttribute = entity.getAttribute(Attributes.MAX_HEALTH);

        if (entity.hasEffect(this)) {
            if (maxHealthAttribute != null && maxHealthAttribute.getModifier(MODIFIER_ID) == null) {
                AttributeModifier modifier = new AttributeModifier(MODIFIER_ID, "WrathOfGod", 2.0D - maxHealthAttribute.getBaseValue(), AttributeModifier.Operation.ADDITION);
                maxHealthAttribute.addPermanentModifier(modifier);
            }

            if (entity.getHealth() > 2.0F) {
                entity.setHealth(2.0F);
            }
        }
        if (entity.level instanceof ServerLevel serverLevel && entity.hasEffect(this)) {
            if (serverLevel.dimension() == Level.OVERWORLD) {
                teleportToNether(entity);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap attributeMap, int amplifier) {
        boolean passedThroughPortal = entity.getPersistentData().getBoolean("PassedThroughPortal");
        AttributeInstance maxHealthAttribute = entity.getAttribute(Attributes.MAX_HEALTH);

        if (!passedThroughPortal && entity instanceof ServerPlayer) {
            entity.addEffect(new MobEffectInstance(this, Integer.MAX_VALUE));
        }

        if (maxHealthAttribute != null && maxHealthAttribute.getModifier(MODIFIER_ID) != null) {
            maxHealthAttribute.removeModifier(MODIFIER_ID);
        }

        entity.getPersistentData().remove("PassedThroughPortal");
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }

    private static final int TOTAL_MAX_ATTEMPTS = 3939;

    private void teleportToNether(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        ServerLevel nether = player.server.getLevel(Level.NETHER);
        if (nether == null) {
            return;
        }

        BlockPos centerPoint = new BlockPos(0, nether.getSeaLevel(), 0);
        final int minRadius = 500;
        final int maxRadius = 3939;
        final int maxAttemptsPerRound = 39;
        final int totalMaxAttempts = 3939;
        int totalAttempts = 0;

        Random rand = new Random();

        while (totalAttempts < TOTAL_MAX_ATTEMPTS) {
            int attempts = 0;
            while (attempts < maxAttemptsPerRound) {
                double angle = rand.nextDouble() * Math.PI * 2;
                int radius = minRadius + rand.nextInt(maxRadius - minRadius);
                int x = centerPoint.getX() + (int)(radius * Math.cos(angle));
                int z = centerPoint.getZ() + (int)(radius * Math.sin(angle));
                int y = nether.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

                if (y > nether.getMinBuildHeight() && isSafeLocation(nether, x, y, z)) {
                    player.teleportTo(nether, x, y, z, entity.getYRot(), entity.getXRot());
                    return;
                }

                attempts++;
                totalAttempts++;
            }
        }

        BlockPos defaultSpawn = nether.getSharedSpawnPos();
        player.teleportTo(nether, defaultSpawn.getX(), defaultSpawn.getY(), defaultSpawn.getZ(), entity.getYRot(), entity.getXRot());
    }

    private boolean isSafeLocation(ServerLevel level, int x, int y, int z) {
        BlockPos groundPos = new BlockPos(x, y - 1, z);
        BlockPos playerPos = new BlockPos(x, y, z);
        BlockPos abovePos = new BlockPos(x, y + 1, z);

        boolean isGroundSafe = isGroundSafe(level, groundPos);
        boolean isPlayerPosSafe = isAirSafe(level, playerPos) && isAirSafe(level, abovePos);

        return isGroundSafe && isPlayerPosSafe;
    }

    private boolean isGroundSafe(ServerLevel level, BlockPos groundPos) {
        BlockState state = level.getBlockState(groundPos);
        if (state.getMaterial().isSolid()) {
            boolean isNotFire = !state.is(BlockTags.FIRE);
            boolean isNotLava = !state.getFluidState().is(FluidTags.LAVA);
            return isNotFire && isNotLava;
        }
        return false;
    }

    private boolean isAirSafe(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }
}