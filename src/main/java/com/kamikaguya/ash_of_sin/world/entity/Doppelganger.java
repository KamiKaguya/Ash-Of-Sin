package com.kamikaguya.ash_of_sin.world.entity;

import com.kamikaguya.ash_of_sin.config.AshOfSinConfig;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class Doppelganger extends PathfinderMob {
    public Set<UUID> attackersUUIDs;
    public ServerPlayer copiedPlayer;

    public Doppelganger(EntityType<? extends Doppelganger> entityType, Level level) {

        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ARMOR, 39.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 39.0D)
                .add(Attributes.ATTACK_SPEED, 3.9D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public void initialize(ServerPlayer player, Set<UUID> attackersUUIDs) {
        this.attackersUUIDs = attackersUUIDs;
        this.copiedPlayer = player;
        copyPlayerData(player);
        applyGlowingEffect();
    }

    public void copyPlayerData(ServerPlayer player) {
        this.setCustomName(player.getName());
        ItemStack headStack = player.getInventory().player.getItemBySlot(EquipmentSlot.HEAD);
        if (!headStack.isEmpty()) {
            this.setItemSlot(EquipmentSlot.HEAD, headStack.copy());
        }
        ItemStack chestStack = player.getInventory().player.getItemBySlot(EquipmentSlot.CHEST);
        if (!chestStack.isEmpty()) {
            this.setItemSlot(EquipmentSlot.CHEST, chestStack.copy());
        }
        ItemStack legsStack = player.getInventory().player.getItemBySlot(EquipmentSlot.LEGS);
        if (!legsStack.isEmpty()) {
            this.setItemSlot(EquipmentSlot.LEGS, legsStack.copy());
        }
        ItemStack feetStack = player.getInventory().player.getItemBySlot(EquipmentSlot.FEET);
        if (!feetStack.isEmpty()) {
            this.setItemSlot(EquipmentSlot.FEET, feetStack.copy());
        }
        ItemStack mainHandStack = player.getInventory().player.getMainHandItem();
        if (!mainHandStack.isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, mainHandStack.copy());
        }
        ItemStack offHandStack = player.getInventory().player.getOffhandItem();
        if (!offHandStack.isEmpty()) {
            this.setItemSlot(EquipmentSlot.OFFHAND, offHandStack.copy());
        }
        this.setDropChance(EquipmentSlot.HEAD,0);
        this.setDropChance(EquipmentSlot.CHEST,0);
        this.setDropChance(EquipmentSlot.LEGS,0);
        this.setDropChance(EquipmentSlot.FEET,0);
        this.setDropChance(EquipmentSlot.MAINHAND,0);
        this.setDropChance(EquipmentSlot.OFFHAND,0);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.PLAYER_HURT;
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level.isClientSide) {
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 780, 38, false, false));

            this.level.playSound(null, this.blockPosition(), SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 1.0F, 1.0F);

            ((ServerLevel)this.level).sendParticles(ParticleTypes.REVERSE_PORTAL,
                    this.getX(), this.getY() + this.getBbHeight() / 2.0F, this.getZ(),
                    20,
                    this.getBbWidth() / 2.0F, 0.5D, this.getBbWidth() / 2.0F,
                    0.1D);
        }
        super.die(source);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof ServerPlayer attacker) {
            if (attackersUUIDs.contains(attacker.getUUID()) &&
                    !isDamageImmune(source)) {
                return super.hurt(source, amount);
            }
        }
        return false;
    }

    public boolean isDamageImmune(DamageSource source) {
        return source == DamageSource.OUT_OF_WORLD ||
                source.isCreativePlayer();
    }

    public void applyGlowingEffect() {
        MobEffectInstance glowingEffect = new MobEffectInstance(MobEffects.GLOWING, Integer.MAX_VALUE, 38);
        this.addEffect(glowingEffect);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));

        this.targetSelector.addGoal(1, new TargetGoal(this));
        this.targetSelector.addGoal(1, new AggressiveTargetGoal(this, 128.0));
    }

    public boolean canTeleportToPlayer(ServerPlayer player) {
        return true;
    }

    public static class TargetGoal extends Goal {
        public final Doppelganger doppelganger;

        public TargetGoal(Doppelganger doppelganger) {
            this.doppelganger = doppelganger;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            return doppelganger.copiedPlayer != null && doppelganger.copiedPlayer.isAlive();
        }

        @Override
        public void start() {
            doppelganger.setTarget(doppelganger.copiedPlayer);
        }
    }

    public static class AggressiveTargetGoal extends Goal {
        public final Doppelganger doppelganger;
        public final double range;
        public LivingEntity target;

        public AggressiveTargetGoal(Doppelganger doppelganger, double range) {
            this.doppelganger = doppelganger;
            this.range = range;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (doppelganger.copiedPlayer == null || !doppelganger.copiedPlayer.isAlive()) {
                return false;
            }

            List<LivingEntity> list = doppelganger.level.getEntitiesOfClass(LivingEntity.class, doppelganger.getBoundingBox().inflate(range), e ->
                    doppelganger.attackersUUIDs.contains(e.getUUID()));

            for (LivingEntity entity : list) {
                if (doppelganger.hasLineOfSight(entity)) {
                    target = entity;
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && target.isAlive() && doppelganger.distanceToSqr(target) <= (range * range);
        }

        @Override
        public void start() {
            doppelganger.setTarget(target);
        }

        @Override
        public void stop() {
            target = null;
            doppelganger.setTarget(null);
        }
    }

    public void teleportAnotherToPlayer(Doppelganger doppelganger, @Nullable ServerPlayer player) {
        if (player != null && doppelganger.distanceToSqr(player) > 16 * 16) {
            doppelganger.teleportTo(player.getX(), player.getY(), player.getZ());
        }
    }

    @Override
    public void push(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeLeashed(@NotNull Player player) {
        return false;
    }
}