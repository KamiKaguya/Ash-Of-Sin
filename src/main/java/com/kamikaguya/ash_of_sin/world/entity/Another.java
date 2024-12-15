package com.kamikaguya.ash_of_sin.world.entity;

import com.kamikaguya.ash_of_sin.tracker.AshOfSinPlayerAttackTargetTracker;
import com.kamikaguya.ash_of_sin.util.OwnerHelper;
import com.kamikaguya.ash_of_sin.util.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class Another extends PathfinderMob implements AbsoluteSpaceTimeRealmEntity {
    public static final double MAX_HEALTH = 20.0;
    public static final double MOVEMENT_SPEED = 0.1;
    public static final double ATTACK_DAMAGE = 1.0;
    public static final double ATTACK_SPEED = 4.0;
    protected LivingEntity cachedOwner;
    public UUID ownerUUID;

    public Another(EntityType<? extends Another> entitytype, Level level) {
        super(entitytype, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
                .add(Attributes.ATTACK_SPEED, ATTACK_SPEED);
    }

    MobEffect effect01 = MobEffects.REGENERATION;
    int effect01Duration = 10 * 20;
    int effect01Amplifier = 9;

    MobEffect effect02 = MobEffects.DAMAGE_RESISTANCE;
    int effect02Duration = 10 * 20;
    int effect02Amplifier = 9;

    public void copyPlayerDataToAnother(ServerPlayer player) {
        this.setOwner(player);
        this.setOwnerUUID(player.getUUID());
        this.setCustomName(player.getName());
        this.isAlliedTo(player);

        this.setPersistenceRequired();
        this.getPersistentData().putBoolean("PersistenceRequired", true);

        this.addEffect(new MobEffectInstance(effect01, effect01Duration, effect01Amplifier));
        this.addEffect(new MobEffectInstance(effect02, effect02Duration, effect02Amplifier));

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

    public static final List<Attribute> ATTRIBUTES = new ArrayList<>();

    static {
        ATTRIBUTES.add(Attributes.ATTACK_DAMAGE);
        ATTRIBUTES.add(Attributes.FOLLOW_RANGE);
        ATTRIBUTES.add(Attributes.ARMOR);
        ATTRIBUTES.add(Attributes.ARMOR_TOUGHNESS);
        ATTRIBUTES.add(Attributes.KNOCKBACK_RESISTANCE);
        ATTRIBUTES.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
        ATTRIBUTES.add(Attributes.ATTACK_KNOCKBACK);
        ATTRIBUTES.add(Attributes.ATTACK_SPEED);
        ATTRIBUTES.add(Attributes.FLYING_SPEED);
        ATTRIBUTES.add(Attributes.JUMP_STRENGTH);
        ATTRIBUTES.add(Attributes.LUCK);
        ATTRIBUTES.add(Attributes.MAX_HEALTH);
        ATTRIBUTES.add(Attributes.MOVEMENT_SPEED);
    }

    public void copyPlayerAttributes(ServerPlayer player) {
        for (Attribute attribute : ATTRIBUTES) {
            AttributeInstance playerAttributeInstance = player.getAttribute(attribute);
            AttributeInstance anotherAttributeInstance = this.getAttribute(attribute);

            if (playerAttributeInstance != null) {
                double playerAttributeBaseValue = playerAttributeInstance.getBaseValue();
                if (anotherAttributeInstance != null) {
                    anotherAttributeInstance.setBaseValue(playerAttributeBaseValue);
                    for (AttributeModifier modifier : playerAttributeInstance.getModifiers()) {
                        AttributeModifier anotherModifier = new AttributeModifier(modifier.getId(), modifier.getName(), modifier.getAmount(), modifier.getOperation());

                        anotherAttributeInstance.removeModifier(modifier);

                        anotherAttributeInstance.addPermanentModifier(anotherModifier);
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {

            ServerPlayer ownerPlayer = (ServerPlayer) this.level.getPlayerByUUID(this.getOwnerUUID());
            if (ownerPlayer != null) {
                setOwnerUUID(ownerPlayer.getUUID());
                teleportAnotherToPlayer(this, ownerPlayer);
            } else {
                setHealth(0);
            }
            hasRegenerationBuff();
        }
    }

    public LivingEntity getOwner() {
        return OwnerHelper.getAndCacheOwner(this.level, this.cachedOwner, this.ownerUUID);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }

    }

    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.ownerUUID = OwnerHelper.deserializeOwner(compoundTag);
    }

    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, this.ownerUUID);
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void hasRegenerationBuff() {
        if (hasEffect(effect01)) {
            return;
        }

        if (getHealth() < getMaxHealth() * 0.25) {
            this.addEffect(new MobEffectInstance(effect01, 20, 3));
        } else if (getHealth() < getMaxHealth() * 0.5) {
            this.addEffect(new MobEffectInstance(effect01, 20, 2));
        } else if (getHealth() < getMaxHealth() * 0.75) {
            this.addEffect(new MobEffectInstance(effect01, 20, 1));
        } else if (getHealth() < getMaxHealth() * 1.0) {
            this.addEffect(new MobEffectInstance(effect01, 20, 0));
        }

    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (!level.isClientSide) {
            if (damageSource.getEntity() instanceof Player player) {
                if (player.getUUID().equals(getOwnerUUID())) {
                    return false;
                }
            }

            if (damageSource.getEntity() instanceof Another another && another.getOwnerUUID() != null) {
                if (another.getOwnerUUID().equals(getOwnerUUID())) {
                    return false;
                }
            }

            if (damageSource.getEntity() instanceof Another another) {
                if (another.getName().equals(getName()) && another.getCustomName().equals(getCustomName())) {
                    return false;
                }
            }
        }
        return super.hurt(damageSource, amount);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.PLAYER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PORTAL_TRAVEL;
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if (!this.level.isClientSide) {
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 780, 38, false, false));

            ((ServerLevel)this.level).sendParticles(ParticleTypes.REVERSE_PORTAL,
                    this.getX(), this.getY() + this.getBbHeight() / 2.0F, this.getZ(),
                    20,
                    this.getBbWidth() / 2.0F, 0.5D, this.getBbWidth() / 2.0F,
                    0.1D);

            AshOfSinPlayerAttackTargetTracker.removeOwnerTarget(ownerUUID);
        }
        this.onDeathHelper();
        super.die(source);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public boolean doHurtTarget(Entity entity) {
        ServerPlayer ownerPlayer = (ServerPlayer) this.level.getPlayerByUUID(this.getOwnerUUID());
        return Utils.doMeleeAttack(this, entity, DamageSource.playerAttack(ownerPlayer));
    }

    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity) || this.isAlliedHelper(entity);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new AnotherOwnerAttackTargetGoal(this));
        this.targetSelector.addGoal(2, new AnotherOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new AnotherHurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new AnotherFollowOwnerGoal(this, 1.0D, 3.0F, 64.0F));
    }

    public class AnotherFollowOwnerGoal extends Goal {
        public final Another another;
        public Player owner;
        public final double speed;
        public final float minDist;
        public final float maxDist;

        @Override
        public boolean canUse() {
            if (ownerUUID == null) {
                return false;
            }
            Player owner = another.level.getPlayerByUUID(ownerUUID);
            if (owner == null) {
                return false;
            }
            this.owner = owner;
            double distanceSq = owner.distanceToSqr(another);
            return (distanceSq > minDist * minDist);
        }

        public AnotherFollowOwnerGoal(Another another, double speed, float minDist, float maxDist) {
            this.another = another;
            this.speed = speed;
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public void tick() {
            if (!this.another.level.isClientSide) {
                this.another.getLookControl().setLookAt(this.owner, 10.0F, (float) this.another.getMaxHeadXRot());

                if (this.owner.distanceToSqr(another) > (minDist * minDist)) {
                    double x = this.owner.getX();
                    double y = this.owner.getY() + 1;
                    double z = this.owner.getZ();

                    double dx = x - this.another.getX();
                    double dy = y - this.another.getY();
                    double dz = z - this.another.getZ();
                    if (dx != 0 || dy != 0 || dz != 0) {
                        this.another.getNavigation().moveTo(x, y, z, this.speed);
                    }
                }

                if (this.owner.distanceToSqr(another) <= (minDist * minDist)) {
                    this.another.getNavigation().stop();
                }
            }
            super.tick();
        }

        @Override
        public boolean canContinueToUse() {
            return this.another.distanceToSqr(this.owner) <= (this.maxDist * this.maxDist) && this.another.distanceToSqr(this.owner) > (this.minDist * this.minDist);
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public void stop() {
            this.owner = null;
            this.another.getNavigation().stop();
            super.stop();
        }
    }

    public static class AnotherOwnerAttackTargetGoal extends TargetGoal {
        public final Another another;
        public int timestamp;

        public AnotherOwnerAttackTargetGoal(Another another) {
            super(another, false);
            this.another = another;
            this.timestamp = -1;
        }

        @Override
        public boolean canUse() {
            if (another.getOwnerUUID() == null) {
                return false;
            }
            Player owner = another.level.getPlayerByUUID(another.getOwnerUUID());
            if (owner == null) {
                return false;
            }
            LivingEntity ownerTarget = AshOfSinPlayerAttackTargetTracker.getOwnerTarget(another.getOwnerUUID());
            if (ownerTarget != null && ownerTarget.getUUID().equals(owner.getUUID())){
                return false;
            }
            if (ownerTarget != null && ownerTarget.getName().equals(another.getName()) && ownerTarget.getCustomName().equals(another.getCustomName())){
                return false;
            }
            if (ownerTarget != null && another.tickCount - timestamp > 10) {
                this.another.setTarget(ownerTarget);
                timestamp = another.tickCount;
                return true;
            }
            return false;
        }
    }

    public static class AnotherOwnerHurtByTargetGoal extends TargetGoal {
        public final Another another;
        public LivingEntity attacker;
        public int timestamp;

        public AnotherOwnerHurtByTargetGoal(Another another) {
            super(another, true);
            this.another = another;
            this.timestamp = -1;
        }

        @Override
        public boolean canUse() {
            if (another.getOwnerUUID() == null) {
                return false;
            }
            Player owner = another.level.getPlayerByUUID(another.getOwnerUUID());
            if (owner == null) {
                return false;
            }
            LivingEntity potentialAttacker = owner.getLastHurtByMob();
            if (potentialAttacker != null && potentialAttacker.getUUID().equals(owner.getUUID())){
                return false;
            }
            if (potentialAttacker != null && potentialAttacker.getName().equals(another.getName()) && potentialAttacker.getCustomName().equals(another.getCustomName())){
                return false;
            }
            if ((potentialAttacker != null && another.tickCount - timestamp > 10)) {
                attacker = potentialAttacker;
                timestamp = another.tickCount;
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            this.another.setTarget(this.attacker);
            super.start();
        }

    }

    public static class AnotherHurtByTargetGoal extends TargetGoal {
        public final Another another;
        public LivingEntity attacker;
        public int timestamp;

        public AnotherHurtByTargetGoal(Another another) {
            super(another, true);
            this.another = another;
            this.timestamp = -1;
        }

        @Override
        public boolean canUse() {
            if (another.getOwnerUUID() == null) {
                return false;
            }
            Player owner = another.level.getPlayerByUUID(another.getOwnerUUID());
            if (owner == null) {
                return false;
            }
            LivingEntity potentialAttacker = another.getLastHurtByMob();
            if (potentialAttacker != null && potentialAttacker.getUUID().equals(owner.getUUID())){
                return false;
            }
            if (potentialAttacker != null && potentialAttacker.getName().equals(another.getName()) && potentialAttacker.getCustomName().equals(another.getCustomName())){
                return false;
            }
            if ((potentialAttacker != null && another.tickCount - timestamp > 10)) {
                attacker = potentialAttacker;
                timestamp = another.tickCount;
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            this.another.setTarget(this.attacker);
            super.start();
        }

    }

    public void teleportAnotherToPlayer(Another another, ServerPlayer player) {
        if (player != null && another.distanceToSqr(player) > 64 * 64) {
            another.teleportTo(player.getX(), player.getY(), player.getZ());
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