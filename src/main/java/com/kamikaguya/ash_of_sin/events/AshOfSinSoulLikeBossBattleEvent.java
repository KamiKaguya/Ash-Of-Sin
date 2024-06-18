package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.SoulLikeBossBattleConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinSoulLikeBossBattleEvent {

    private static final boolean SOUL_LIKE_BOSS_BATTLE_ON = SoulLikeBossBattleConfig.SOUL_LIKE_BOSS_BATTLE_ON.get();
    private static final int DISTANCE = SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get();
    private static final boolean ANTI_ON = SoulLikeBossBattleConfig.ANTI_IRON_GOLEM.get();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (SOUL_LIKE_BOSS_BATTLE_ON) {
            if (!(event.getEntityLiving() instanceof ServerPlayer player)) {
                return;
            }

            Level world = player.level;
            double playerX = player.getX();
            double playerY = player.getY();
            double playerZ = player.getZ();

            List<String> soulLikeBossEntityList = SoulLikeBossBattleConfig.SOUL_LIKE_BOSS_BATTLE_ENTITY.get().stream()
                    .map(s -> (String) s)
                    .toList();

            for (String bossName : soulLikeBossEntityList) {
                EntityType<?> bossType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(bossName));
                if (bossType == null) {
                    continue;
                }

                List<LivingEntity> nearbyEntities = world.getEntitiesOfClass(LivingEntity.class, new AABB(
                        playerX - DISTANCE, playerY - 8, playerZ - DISTANCE,
                        playerX + DISTANCE, playerY + 8, playerZ + DISTANCE
                ));

                for (LivingEntity nearbyEntity : nearbyEntities) {
                    if (bossType.equals(nearbyEntity.getType())) {
                        if (allNearbyPlayerDied(world, nearbyEntity, player)) {
                            nearbyEntity.removeAllEffects();
                            if (nearbyEntity.getLastHurtByMobTimestamp() > 20) {
                                nearbyEntity.setHealth(nearbyEntity.getMaxHealth());
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean allNearbyPlayerDied(Level world, LivingEntity boss, ServerPlayer player) {
        List<ServerPlayer> nearbyPlayers = world.getEntitiesOfClass(ServerPlayer.class, new AABB(
                boss.getX() - DISTANCE, boss.getY() - 8, boss.getZ() - DISTANCE,
                boss.getX() + DISTANCE, boss.getY() + 8, boss.getZ() + DISTANCE
        ));

        return nearbyPlayers.stream().allMatch(ServerPlayer::isDeadOrDying);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (SOUL_LIKE_BOSS_BATTLE_ON && ANTI_ON) {
            List<String> soulLikeBossEntityList = SoulLikeBossBattleConfig.SOUL_LIKE_BOSS_BATTLE_ENTITY.get().stream().map(s -> (String) s).toList();
            List<String> ironGolemEntityList = SoulLikeBossBattleConfig.IRON_GOLEM.get().stream().map(s -> (String) s).toList();

            for (String bossEntityName : soulLikeBossEntityList) {
                EntityType<?> bossType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(bossEntityName));
                for (String ironGolemEntityName : ironGolemEntityList) {
                    EntityType<?> ironGolemType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(ironGolemEntityName));
                    LivingEntity boss = event.getEntityLiving();

                    DamageSource source = event.getSource();
                    Entity ironGolem01 = source.getEntity();
                    Entity ironGolem02 = source.getDirectEntity();

                    if ((ironGolem01 != null) && (boss.getType().equals(bossType)) && (ironGolem01.getType().equals(ironGolemType))) {
                        if (!(boss.level.isClientSide())) {

                            event.setAmount(0);
                            ironGolem01.kill();
                        }
                    }

                    if ((ironGolem02 != null) && (boss.getType().equals(bossType)) && (ironGolem02.getType().equals(ironGolemType))) {
                        if (!(boss.level.isClientSide())) {

                            event.setAmount(0);
                            ironGolem02.kill();
                        }
                    }
                }
            }
        }
    }
}