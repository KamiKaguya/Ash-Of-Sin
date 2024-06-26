package com.kamikaguya.ash_of_sin.world.entity;

import com.kamikaguya.ash_of_sin.config.AshOfSinConfig;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class Assassin extends PathfinderMob  {
    public ItemStack mainHandItemStack;
    public ItemStack offHandItemStack;

    public Assassin(EntityType<? extends Assassin> entityType, Level level) {

        super(entityType, level);
        if (!level.isClientSide) {
            this.EntityInventory();
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 39.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ARMOR, 39.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 39.0D)
                .add(Attributes.ATTACK_SPEED, 3.9D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public void EntityInventory() {
        applyItemNBT();

        this.setItemSlot(EquipmentSlot.MAINHAND, this.mainHandItemStack);
        this.setItemSlot(EquipmentSlot.OFFHAND, this.offHandItemStack);
    }

    public void applyItemNBT() {
        ItemStack mainHand = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(AshOfSinConfig.Assassin_MAIN_HAND_ITEM.get())));

        mainHand.setTag(parseStringToNBT(AshOfSinConfig.Assassin_MAIN_HAND_ITEM_NBT.get()));

        ItemStack offHand = AshOfSinConfig.Assassin_OFF_HAND_ITEM.get().equals("minecraft:air")
                ? ItemStack.EMPTY : new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(AshOfSinConfig.Assassin_OFF_HAND_ITEM.get())));

        if (!offHand.isEmpty()) {
            offHand.setTag(parseStringToNBT(AshOfSinConfig.Assassin_OFF_HAND_ITEM_NBT.get()));
        }

        this.mainHandItemStack = mainHand;
        this.offHandItemStack = offHand;
    }

    public CompoundTag parseStringToNBT(String jsonString) {
        try {
            return TagParser.parseTag(jsonString);
        } catch (CommandSyntaxException e) {
            return new CompoundTag();
        }
    }

    @Override
    protected float getEquipmentDropChance(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            return 0.03F;
        }
        return super.getEquipmentDropChance(slot);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 7.0F));

        this.targetSelector.addGoal(1, new PersistentNearestAttackableTargetGoal<>(this, Player.class, 10, 128));

        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }
    public static class PersistentNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        public final PathfinderMob mob;
        public final double distance;

        public PersistentNearestAttackableTargetGoal(PathfinderMob mob, Class<T> targetClass, int targetChance, double distance) {
            super(mob, targetClass, targetChance, true, false, null);
            this.mob = mob;
            this.distance = distance;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.mob.getTarget();
            if (target == null || !target.isAlive()) {
                return super.canUse();
            } else {
                double distanceSq = this.mob.distanceToSqr(target);
                return distanceSq <= distance * distance;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player) {
            if (source == DamageSource.MAGIC||
                    source.isCreativePlayer() ||
                    source.isExplosion() ||
                    source.isProjectile()
            ){
                return false;
            }
            return super.hurt(source, amount);
        }
        if (source == DamageSource.OUT_OF_WORLD ||
                source == DamageSource.LIGHTNING_BOLT ||
                source == DamageSource.MAGIC ||
                source == DamageSource.FALL ||
                source == DamageSource.IN_FIRE ||
                source == DamageSource.ON_FIRE ||
                source == DamageSource.HOT_FLOOR ||
                source == DamageSource.WITHER ||
                source == DamageSource.DROWN ||
                source.isCreativePlayer() ||
                source.isExplosion() ||
                source.isProjectile()) {
            return false;
        }
        return false;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        boolean success = super.doHurtTarget(entity);
        if (success && entity instanceof LivingEntity livingEntity) {

            for (String effectString : AshOfSinConfig.EFFECT_LIST.get()) {
                String[] parts = effectString.split(",");
                ResourceLocation effectRL = new ResourceLocation(parts[0]);
                MobEffect potionEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectRL);

                if (potionEffect != null) {
                    int duration = Integer.parseInt(parts[1]);
                    int amplifier = Integer.parseInt(parts[2]);
                    MobEffectInstance effectInstance = new MobEffectInstance(potionEffect, duration, amplifier);
                    livingEntity.addEffect(effectInstance);
                }
            }
        }
        return success;
    }

    @Override
    public void push(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeLeashed(@NotNull Player player) {
        return false;
    }
}