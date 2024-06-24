package com.kamikaguya.ash_of_sin.world.entity;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.kamikaguya.ash_of_sin.gameasset.AshOfSinSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.util.ITeleporter;
import com.kamikaguya.ash_of_sin.config.AshOfSinConfig;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class Gate extends Entity {

    public Gate(EntityType<?> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide) {
            this.setInvulnerable(true);
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        BlockPos pos = this.getOnPos();
        this.level.playSound(
                null,
                pos,
                AshOfSinSounds.GATE_OPEN.get(),
                SoundSource.AMBIENT,
                1.0f,
                1.0f
        );
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("PersistenceRequired", true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("PersistenceRequired")) {
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.isCreativePlayer()){
            return super.hurt(source, amount);
        }
        return  false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide) {

            List<Entity> nearbyEntities = this.level.getEntities(this, this.getBoundingBox().inflate(1.0D));
            for (Entity currentEntity : nearbyEntities) {
                if (this.getBoundingBox().inflate(0.5D).intersects(currentEntity.getBoundingBox())
                        && isEntityAllowedThroughGateway(currentEntity)) {
                    teleportEntityToDimension(currentEntity);
                }

                if(currentEntity.getPersistentData().contains("TeleportCooldown")) {
                    int cooldown = currentEntity.getPersistentData().getInt("TeleportCooldown");
                    if (cooldown > 0) {
                        currentEntity.getPersistentData().putInt("TeleportCooldown", --cooldown);
                    } else {
                        currentEntity.getPersistentData().remove("TeleportCooldown");
                        currentEntity.getPersistentData().putBoolean("Teleported", false);
                    }
                }
            }
        } else {
            BlockPos pos = this.getOnPos();
            this.level.playSound(
                    null,
                    pos,
                    AshOfSinSounds.GATE_AMBIENT.get(),
                    SoundSource.AMBIENT,
                    1.0f,
                    1.0f
            );
        }
    }

    public boolean isEntityAllowedThroughGateway(Entity entity) {
        if (!entity.getPersistentData().getBoolean("Teleported") || entity.tickCount % 20 == 0) {
            if (entity instanceof Player) {
                return true;
            } else {
                ResourceLocation entityRegistryName = EntityType.getKey(entity.getType());
                return AshOfSinConfig.getAllowedEntity().contains(entityRegistryName.toString());
            }
        }
        return false;
    }

    public void teleportEntityToDimension(Entity entity) {
        ServerLevel currentLevel = (ServerLevel) entity.level;

        ResourceKey<Level> targetDimensionKey = currentLevel.dimension().equals(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ash_of_sin:absolute_space_time_realm")))
                ? Level.OVERWORLD
                : ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ash_of_sin:absolute_space_time_realm"));

        ServerLevel targetLevel = Objects.requireNonNull(entity.getServer()).getLevel(targetDimensionKey);

        if (targetLevel != null && isGatePresentInDimension(targetLevel) && !entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions()) {
            entity.getPersistentData().putBoolean("Teleported", true);
            entity.changeDimension(targetLevel, new ITeleporter() {
                @Override
                public Entity placeEntity(Entity entity, ServerLevel currentLevel, ServerLevel destinationLevel, float yaw, Function<Boolean, Entity> repositionEntity) {
                    entity.moveTo(Gate.this.getX(), Gate.this.getY(), Gate.this.getZ(), Gate.this.getYRot(), entity.getXRot());
                    entity.unRide();
                    destinationLevel.addDuringTeleport(entity);

                    entity.getPersistentData().putInt("TeleportCooldown", 200);

                    return entity;
                }
            });
            if (entity instanceof ServerPlayer player) {
                if (player.level.isClientSide()) {
                    BlockPos pos = this.getOnPos();
                    this.level.playSound(
                            null,
                            pos,
                            AshOfSinSounds.GATE_WARP.get(),
                            SoundSource.AMBIENT,
                            1.0f,
                            1.0f
                    );
                }
            }
        }
    }

    public boolean isGatePresentInDimension(ServerLevel targetLevel) {
        for (Entity entity : targetLevel.getAllEntities()) {
            if (entity instanceof Gate) {
                return true;
            }
        }
        return false;
    }
}