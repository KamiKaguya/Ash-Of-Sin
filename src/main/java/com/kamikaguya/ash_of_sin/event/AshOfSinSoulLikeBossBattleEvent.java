package com.kamikaguya.ash_of_sin.event;

import com.kamikaguya.ash_of_sin.config.SoulLikeBossBattleConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinSoulLikeBossBattleEvent {

    public static final boolean SOUL_LIKE_BOSS_BATTLE_ON = SoulLikeBossBattleConfig.SOUL_LIKE_BOSS_BATTLE_ON.get();
    public static final boolean ANTI_ON = SoulLikeBossBattleConfig.ANTI_IRON_GOLEM.get();

    private static final UUID BOSS_HEALTH_MODIFIER_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final UUID BOSS_DAMAGE_MODIFIER_UUID = UUID.fromString("b2c3d4e5-f6a7-8901-bcde-f234567890ab");

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (SOUL_LIKE_BOSS_BATTLE_ON) {
            if (!(event.getEntity() instanceof ServerPlayer player)) {
                return;
            }

            if (!validPlayerGamemode(player)) {
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
                EntityType<?> bossType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(bossName));
                if (bossType == null) {
                    continue;
                }

                List<LivingEntity> nearbyEntities = world.getEntitiesOfClass(LivingEntity.class, new AABB(
                        playerX - SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get(), playerY - 8, playerZ - SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get(),
                        playerX + SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get(), playerY + 8, playerZ + SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get()
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

    public static boolean validPlayerGamemode(ServerPlayer player){
        List<String> validplayergamemode = SoulLikeBossBattleConfig.PLAYER_GAMEMODE.get().stream()
                .map(s -> (String) s)
                .toList();

        for (String gamemode : validplayergamemode) {
            if (gamemode == null) {
                return false;
            }

            if (gamemode.equals(GameType.SURVIVAL.getName()) && player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL){
                return true;
            }

            if (gamemode.equals(GameType.ADVENTURE.getName()) && player.gameMode.getGameModeForPlayer() == GameType.ADVENTURE){
                return true;
            }

            if (gamemode.equals(GameType.CREATIVE.getName()) && player.gameMode.getGameModeForPlayer() == GameType.CREATIVE){
                return true;
            }

            if (gamemode.equals(GameType.SPECTATOR.getName()) && player.gameMode.getGameModeForPlayer() == GameType.SPECTATOR){
                return true;
            }
        }
        return false;
    }

    public static boolean allNearbyPlayerDied(Level world, LivingEntity boss, ServerPlayer player) {
        List<ServerPlayer> nearbyPlayers = world.getEntitiesOfClass(ServerPlayer.class, new AABB(
                boss.getX() - SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get(), boss.getY() - 8, boss.getZ() - SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get(),
                boss.getX() + SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get(), boss.getY() + 8, boss.getZ() + SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get()
        ));

        return nearbyPlayers.stream()
                .filter(AshOfSinSoulLikeBossBattleEvent::validPlayerGamemode)
                .allMatch(ServerPlayer::isDeadOrDying);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }

        if (SOUL_LIKE_BOSS_BATTLE_ON && ANTI_ON) {
            List<String> soulLikeBossEntityList = SoulLikeBossBattleConfig.SOUL_LIKE_BOSS_BATTLE_ENTITY.get().stream().map(s -> (String) s).toList();
            List<String> ironGolemEntityList = SoulLikeBossBattleConfig.IRON_GOLEM.get().stream().map(s -> (String) s).toList();

            for (String bossEntityName : soulLikeBossEntityList) {
                EntityType<?> bossType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(bossEntityName));
                for (String ironGolemEntityName : ironGolemEntityList) {
                    EntityType<?> ironGolemType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(ironGolemEntityName));
                    LivingEntity boss = event.getEntity();

                    DamageSource source = event.getSource();
                    Entity ironGolem01 = source.getEntity();
                    Entity ironGolem02 = source.getDirectEntity();

                    if ((ironGolem01 != null) && (boss.getType().equals(bossType)) && (ironGolem01.getType().equals(ironGolemType))) {
                        event.setAmount(0);
                        ironGolem01.kill();
                    }

                    if ((ironGolem02 != null) && (boss.getType().equals(bossType)) && (ironGolem02.getType().equals(ironGolemType))) {
                        event.setAmount(0);
                        ironGolem02.kill();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        var server = event.getServer();
        if (server == null) return;

        if (server.getTickCount() % 20 != 0) return;

        boolean isEnabled = SoulLikeBossBattleConfig.SOUL_LIKE_BOSS_BATTLE_ON.get();
        if (!isEnabled) return;

        List<String> soulLikeBossEntityList = SoulLikeBossBattleConfig.SOUL_LIKE_BOSS_BATTLE_ENTITY.get().stream()
                .map(s -> (String) s)
                .toList();
        if (soulLikeBossEntityList.isEmpty()) return;

        int range = SoulLikeBossBattleConfig.BOSS_BATTLE_DISTANCE.get();

        boolean multiplierOn = SoulLikeBossBattleConfig.MULTIPLIER_PER_PLAYER_ON.get();
        double multiplierPerPlayer = SoulLikeBossBattleConfig.MULTIPLIER_PER_PLAYER.get();

        for (var level : server.getAllLevels()) {
            for (var entity : level.getAllEntities()) {
                if (!(entity instanceof LivingEntity living)) continue;

                ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(living.getType());
                if (entityId == null || !soulLikeBossEntityList.contains(entityId.toString())) continue;

                if (!multiplierOn) {
                    adjustBossAttributes(living, 1.0);
                    continue;
                }

                long playerCount = level.getEntitiesOfClass(ServerPlayer.class,
                                new AABB(living.getX() - range, living.getY() - 8, living.getZ() - range,
                                        living.getX() + range, living.getY() + 8, living.getZ() + range))
                        .stream()
                        .filter(AshOfSinSoulLikeBossBattleEvent::validPlayerGamemode)
                        .count();

                double multiplier = 1.0;
                if (playerCount >= 2) {
                    multiplier = 1.0 + multiplierPerPlayer * (playerCount - 1);
                }

                adjustBossAttributes(living, multiplier);
            }
        }
    }

    private static void adjustBossAttributes(LivingEntity boss, double multiplier) {
        AttributeInstance maxHealthAttr = boss.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance attackDamageAttr = boss.getAttribute(Attributes.ATTACK_DAMAGE);
        if (maxHealthAttr == null || attackDamageAttr == null) return;

        float currentHealth = boss.getHealth();
        float oldMax = boss.getMaxHealth();

        maxHealthAttr.removeModifier(BOSS_HEALTH_MODIFIER_UUID);
        attackDamageAttr.removeModifier(BOSS_DAMAGE_MODIFIER_UUID);

        if (multiplier != 1.0) {
            double amount = multiplier - 1.0;
            maxHealthAttr.addPermanentModifier(new AttributeModifier(
                    BOSS_HEALTH_MODIFIER_UUID, "Boss battle health boost", amount, AttributeModifier.Operation.MULTIPLY_BASE));
            attackDamageAttr.addPermanentModifier(new AttributeModifier(
                    BOSS_DAMAGE_MODIFIER_UUID, "Boss battle damage boost", amount, AttributeModifier.Operation.MULTIPLY_BASE));

            float newMax = boss.getMaxHealth();
            float newHealth = currentHealth * newMax / oldMax;
            boss.setHealth(Math.min(newHealth, newMax));
        } else {
            float newMax = boss.getMaxHealth();
            if (currentHealth > newMax) {
                boss.setHealth(newMax);
            }
        }
    }
}