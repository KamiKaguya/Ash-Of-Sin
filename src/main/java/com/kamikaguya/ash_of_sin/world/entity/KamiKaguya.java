package com.kamikaguya.ash_of_sin.world.entity;


import com.kamikaguya.ash_of_sin.config.AshOfSinConfig;
import com.kamikaguya.ash_of_sin.world.effect.AshOfSinEffects;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class KamiKaguya extends PathfinderMob {

    public ItemStack mainHandItemStack;
    public ItemStack offHandItemStack;
    public boolean isInSecondPhase;
    public boolean isSummonedEntityAlive;
    public Entity summonedEntity;
    public final Set<UUID> attackersUUIDs = new HashSet<>();
    public static final Map<UUID, Doppelganger> doppelgangers = new HashMap<>();
    public static void storeDoppelganger(ServerPlayer originalPlayer, Doppelganger doppelganger) {
        doppelgangers.put(originalPlayer.getUUID(), doppelganger);
    }
    public static Doppelganger getStoredDoppelganger(UUID uuid) {
        return doppelgangers.get(uuid);
    }

    public KamiKaguya(EntityType<? extends KamiKaguya> entityType, Level level) {

        super(entityType, level);
        if (!level.isClientSide) {
            this.initEntityInventory();
            this.updateResistanceBuff();

            if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                Component joinMessage = new TranslatableComponent("message.ash_of_sin.kamikaguya.join");
                serverLevel.getServer().getPlayerList().broadcastMessage(joinMessage, ChatType.SYSTEM, Util.NIL_UUID);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3939.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.ATTACK_DAMAGE, 39.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ARMOR, 3939.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 3939.0D)
                .add(Attributes.ATTACK_SPEED, 3.9D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.LUCK, 3939.0D);
    }

    public void initEntityInventory() {
        applyItemNBT();

        this.setItemSlot(EquipmentSlot.MAINHAND, this.mainHandItemStack);
        this.setItemSlot(EquipmentSlot.OFFHAND, this.offHandItemStack);

        this.setDropChance(EquipmentSlot.MAINHAND, AshOfSinConfig.KAMIKAGUYA_MAIN_HAND_DROP_CHANCE.get());
        this.setDropChance(EquipmentSlot.OFFHAND, AshOfSinConfig.KAMIKAGUYA_OFF_HAND_DROP_CHANCE.get());
    }

    public void applyItemNBT() {
        ItemStack mainHand = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(AshOfSinConfig.KAMIKAGUYA_MAIN_HAND_ITEM.get())));
        ItemStack offHand = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(AshOfSinConfig.KAMIKAGUYA_OFF_HAND_ITEM.get())));

        mainHand.setTag(parseStringToNBT(AshOfSinConfig.KAMIKAGUYA_MAIN_HAND_ITEM_NBT.get()));
        offHand.setTag(parseStringToNBT(AshOfSinConfig.KAMIKAGUYA_OFF_HAND_ITEM_NBT.get()));

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
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, 128.0D));
    }

    public class HurtByTargetGoal extends TargetGoal {
        public final KamiKaguya kamiKaguya;
        public LivingEntity target;
        public final double distance;
        public int timestamp;

        public HurtByTargetGoal(KamiKaguya kamiKaguya, double distance) {
            super(kamiKaguya, true);
            this.kamiKaguya = kamiKaguya;
            this.distance = distance;
            this.timestamp = -1;
        }

        @Override
        public boolean canUse() {
            LivingEntity attacker = kamiKaguya.getLastHurtByMob();
            if (attacker !=null && kamiKaguya.tickCount - timestamp > 10) {
                target = attacker;
                timestamp = kamiKaguya.tickCount;
                return true;
            }
            return false;
        }

        @Override
        public void start() {
            this.kamiKaguya.setTarget(this.target);
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.kamiKaguya.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            } else {
                double distanceSq = this.kamiKaguya.distanceToSqr(target);
                return distanceSq <= distance * distance;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (source.getEntity() instanceof Player && source.getDirectEntity() instanceof Player) {
            if (isInSecondPhase && isSummonedEntityAlive) {
                return false;
            }
            if (source.isCreativePlayer()) {
                return false;
            }
            attackersUUIDs.add(entity.getUUID());
            return super.hurt(source, amount);
        }
        if (source.getEntity() instanceof Another && source.getDirectEntity() instanceof Another) {
            if (isInSecondPhase && isSummonedEntityAlive) {
                return false;
            }
            return super.hurt(source, amount);
        }
        if (
                source == DamageSource.OUT_OF_WORLD ||
                source == DamageSource.FALL ||
                source == DamageSource.IN_FIRE ||
                source == DamageSource.ON_FIRE ||
                source == DamageSource.HOT_FLOOR ||
                source == DamageSource.WITHER ||
                source == DamageSource.DROWN
        ) {
            return false;
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsInSecondPhase", isInSecondPhase);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        isInSecondPhase = compound.getBoolean("IsInSecondPhase");

        updateResistanceBuff();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
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
    public void tick() {
        super.tick();
        //if (!level.isClientSide) {
            //if (getHealth() <= getMaxHealth() * 0.25 && !isInSecondPhase) {
                //enterSecondPhase();
                //updateTarget();
            //}
        //}
        removeSlowness();
        updateResistanceBuff();
        checkSummonedEntity();
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public void die(DamageSource source) {
        if (isInSecondPhase && isSummonedEntityAlive) {
            if (!isDoppelgangersDead()) {
                return;
            }
        }
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
        if (!this.level.isClientSide) {
            int dropChance = this.random.nextInt(100);
            if (dropChance < AshOfSinConfig.KAMIKAGUYA_MAIN_HAND_DROP_CHANCE.get()) {
                this.spawnAtLocation(this.getItemBySlot(EquipmentSlot.MAINHAND));
            }
            if (dropChance < AshOfSinConfig.KAMIKAGUYA_OFF_HAND_DROP_CHANCE.get()) {
                this.spawnAtLocation(this.getItemBySlot(EquipmentSlot.OFFHAND));
            }
        }
    }

    public void enterSecondPhase() {
        isInSecondPhase = true;
        updateResistanceBuff();
        summonEntity();
        summonDoppelgangers();
    }

    public void removeSlowness() {
        MobEffect slownessEffect = MobEffects.MOVEMENT_SLOWDOWN;

        this.removeEffect(slownessEffect);
    }

    public void updateResistanceBuff() {
        MobEffect resistanceEffect = MobEffects.DAMAGE_RESISTANCE;

        this.removeEffect(resistanceEffect);

        int effectLevel = isInSecondPhase ? 7 : 6;

        MobEffectInstance effectInstance = new MobEffectInstance(resistanceEffect, Integer.MAX_VALUE, effectLevel);
        this.addEffect(effectInstance);
    }

    public void summonEntity() {
        EntityType<?> entityTypeToSummon = ForgeRegistries.ENTITIES.getValue(
                new ResourceLocation(AshOfSinConfig.ENTITY_SUMMON_ID.get())
        );

        if (entityTypeToSummon != null) {
            summonedEntity = entityTypeToSummon.create(this.level);
            summonedEntity.setPos(this.getX(), this.getY(), this.getZ());
            this.level.addFreshEntity(summonedEntity);

            isSummonedEntityAlive = true;
        } else {
        }
    }

    public void checkSummonedEntity() {
        if (summonedEntity == null || !summonedEntity.isAlive()) {
            isSummonedEntityAlive = false;
        }
    }

    public void summonDoppelgangers() {
        if (!this.level.isClientSide && this.level instanceof ServerLevel serverLevel) {

            for (UUID attackerUUID : attackersUUIDs) {
                Entity originalEntity = serverLevel.getEntity(attackerUUID);
                if (originalEntity instanceof ServerPlayer originalPlayer) {

                    MobEffectInstance wrathOfGodEffect = new MobEffectInstance(AshOfSinEffects.WRATH_OF_GOD.get(), Integer.MAX_VALUE);
                    originalPlayer.addEffect(wrathOfGodEffect);

                    double doppelgangerX = originalPlayer.getX() + (random.nextDouble() * 6D - 3D);
                    double doppelgangerY = originalPlayer.getY();
                    double doppelgangerZ = originalPlayer.getZ() + (random.nextDouble() * 6D - 3D);

                    BlockPos doppelgangerPos = new BlockPos(doppelgangerX, doppelgangerY, doppelgangerZ);
                    BlockPos groundPos = doppelgangerPos.below();
                    BlockPos abovePos = doppelgangerPos.above();

                    if (serverLevel.getBlockState(groundPos).isSolidRender(serverLevel, groundPos) && serverLevel.isEmptyBlock(doppelgangerPos) && serverLevel.isEmptyBlock(abovePos)) {
                        Doppelganger doppelganger = new Doppelganger(AshOfSinEntities.DOPPELGANGER.get(), serverLevel);

                        Set<UUID> singlePlayerSet = new HashSet<>();
                        singlePlayerSet.add(originalPlayer.getUUID());

                        doppelganger.initialize(originalPlayer, singlePlayerSet);
                        doppelganger.setPos(doppelgangerX, doppelgangerY, doppelgangerZ);
                        serverLevel.addFreshEntity(doppelganger);
                        storeDoppelganger(originalPlayer, doppelganger);
                    }
                }
            }
        }
    }

    public boolean isDoppelgangersDead() {
        for (UUID uuid : attackersUUIDs) {
            Entity entity = ((ServerLevel) this.level).getEntity(uuid);
            if (entity instanceof Doppelganger && entity.isAlive()) {
                return false;
            }
        }
        return true;
    }
}