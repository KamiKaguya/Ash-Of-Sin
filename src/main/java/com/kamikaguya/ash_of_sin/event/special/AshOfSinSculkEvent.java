package com.kamikaguya.ash_of_sin.event.special;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinSculkEvent {
    private static final Set<ResourceLocation> SCULK_WEAPONS = Set.of(
            new ResourceLocation(AshOfSin.MODID, "sculk_axe"),
            new ResourceLocation(AshOfSin.MODID, "sculk_longsword"),
            new ResourceLocation(AshOfSin.MODID, "sculk_greatsword"),
            new ResourceLocation(AshOfSin.MODID, "sculk_cleaver"),
            new ResourceLocation(AshOfSin.MODID, "sculk_scythe"),
            new ResourceLocation(AshOfSin.MODID, "sculk_sword")
    );
    public static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = event.getEntity();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof LivingEntity livingEntity) {
            if (RANDOM.nextFloat() < 0.25F && holdSculkWeapon(livingEntity)) {
                float originalDamage = event.getAmount();
                if (RANDOM.nextFloat() > 0.75F) {
                    float sculkDamage = originalDamage * 1.25F;
                    diffuseDarkness(target, livingEntity, sculkDamage);
                    event.setAmount(sculkDamage);
                } else {
                    float sculkDamage = originalDamage * 2.0F;
                    diffuseDarkness(target, livingEntity, sculkDamage);
                    event.setAmount(sculkDamage);
                }
                target.level().playSound(null, target.getOnPos(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        }
    }

    public static boolean holdSculkWeapon(LivingEntity entity) {
        ItemStack mainHand = entity.getMainHandItem();
        return !mainHand.isEmpty() && SCULK_WEAPONS.contains(ForgeRegistries.ITEMS.getKey(mainHand.getItem()));
    }

    @SubscribeEvent
    public static void immuneDarkness(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide() || event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = event.getEntity();
        if (holdSculkWeapon(livingEntity)) {
            livingEntity.removeEffect(MobEffects.DARKNESS);
        }
    }

    public static void diffuseDarkness(LivingEntity target, LivingEntity attacker, float sculkDamage) {
        // 预计算基础参数
        final int DURATION = 33 * 20;
        final MobEffect DARKNESS = MobEffects.DARKNESS;

        // 安全获取效果实例
        Optional<MobEffectInstance> darknessEffect = Optional.ofNullable(target.getEffect(DARKNESS));

        // 判断效果状态
        boolean hasAnyDarkness = darknessEffect.isPresent();
        boolean hasMaxLevel = darknessEffect
                .map(e -> e.getAmplifier() >= 2)
                .orElse(false);

        if (!hasAnyDarkness) {
            target.addEffect(new MobEffectInstance(DARKNESS, DURATION, 0));
            return;
        }

        if (!hasMaxLevel) {
            int newAmplifier = darknessEffect
                    .map(MobEffectInstance::getAmplifier)
                    .map(a -> a + 1)
                    .orElse(0);
            target.addEffect(new MobEffectInstance(DARKNESS, DURATION, newAmplifier));
            return;
        }

        // 处理满级黑暗扩散
        processDarknessSpread(target, attacker, sculkDamage);
    }

    private static void processDarknessSpread(LivingEntity target, LivingEntity attacker, float damage) {
        // 提前过滤训练人偶实体
        if (isTargetDummy(target)) return;

        // 获取范围内实体
        AABB area = new AABB(target.blockPosition()).inflate(7);
        List<LivingEntity> entities = target.level().getEntitiesOfClass(LivingEntity.class, area);

        entities.stream()
                .filter(e -> e.getType() == target.getType())
                .filter(e -> !holdSculkWeapon(e))
                .forEach(e -> applySonicEffect(e, attacker, damage));
    }

    private static boolean isTargetDummy(LivingEntity entity) {
        return ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("dummmmmmy:target_dummy")) == entity.getType();
    }

    private static void applySonicEffect(LivingEntity source, LivingEntity attacker, float baseDamage) {
        // 25%概率广域链式音爆
        if (RANDOM.nextFloat() > 0.75F) {
            return;
        }

        // 获取链式传递参数
        final int MAX_CHAIN = 7; // 最大连锁次数
        final double CHAIN_RANGE = 7.0; // 连锁范围
        final float DAMAGE_DECAY = 0.7f; // 每次连锁伤害衰减

        float currentDamage = baseDamage;
        // 创建已影响实体集合防止循环
        Set<UUID> affectedEntities = new HashSet<>();
        Queue<LivingEntity> entityQueue = new LinkedList<>();

        // 先对原始目标应用基础效果&使用队列实现广度优先传播
        applySingleTargetEffect(attacker, source, currentDamage);
        affectedEntities.add(source.getUUID());
        entityQueue.add(source);

        for (int chainCount = 0; !entityQueue.isEmpty() && chainCount < MAX_CHAIN; chainCount++) {
            int levelSize = entityQueue.size();
            int processedCount = 0;

            while (processedCount++ < levelSize && !entityQueue.isEmpty()) {
                LivingEntity origin = entityQueue.poll();
                if (origin == null) continue;

                // 获取范围内可连锁目标
                List<LivingEntity> chainTargets = origin.level().getEntitiesOfClass(
                        LivingEntity.class,
                        new AABB(origin.blockPosition()).inflate(CHAIN_RANGE),
                        e -> !affectedEntities.contains(e.getUUID()) &&
                                !holdSculkWeapon(e) &&
                                e.isAlive()
                );

                if (chainTargets.isEmpty()) {
                    // 生成单体强化特效
                    if (chainCount == 0) {
                        // 首次连锁失败时强化特效
                        spawnIntensifiedEffect(origin);
                    }
                    continue;
                }

                // 寻找最近目标
                Optional<LivingEntity> nearest = chainTargets.stream()
                        .min(Comparator.comparingDouble(e -> e.distanceToSqr(origin)));

                LivingEntity nextTarget = nearest.get();
                affectedEntities.add(nextTarget.getUUID());
                entityQueue.add(nextTarget);

                // 应用链式音爆效果
                applyChainEffect(attacker, origin, nextTarget, currentDamage);
                currentDamage *= DAMAGE_DECAY; // 伤害衰减
            }
        }
    }

    private static void applySingleTargetEffect(LivingEntity attacker, LivingEntity target, float baseDamage) {
        // 概率伤害倍率
        float damageMultiplier = ThreadLocalRandom.current().nextFloat() <= 0.25F ? 1.25F : 2.0F;
        target.hurt(attacker.damageSources().magic(), baseDamage * damageMultiplier);
        target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 33*20, 2));

        // 单体特效
        if (target.level() instanceof ServerLevel serverLevel) {
            Vec3 center = target.position().add(0, 1.5, 0);

            // 环形粒子
            for (int i = 0; i < 24; i++) {
                double angle = i * Math.PI * 2 / 24;
                Vec3 pos = center.add(
                        Math.cos(angle) * 1.2,
                        Math.sin(System.currentTimeMillis()%2000/1000.0*Math.PI)*0.5, // 动态高度
                        Math.sin(angle) * 1.2
                );
                serverLevel.sendParticles(
                        ParticleTypes.SONIC_BOOM,
                        pos.x, pos.y, pos.z,
                        3, 0.2, 0.2, 0.2, 0.1
                );
            }

            // 中心聚爆特效
            serverLevel.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    center.x, center.y, center.z,
                    15, 0.5, 0.5, 0.5, 0.2
            );

            // 单体音效
            serverLevel.playSound(
                    null, center.x, center.y, center.z,
                    SoundEvents.WARDEN_SONIC_BOOM,
                    SoundSource.PLAYERS,
                    3.0F, 0.8F
            );
        }
    }

    private static void spawnIntensifiedEffect(LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = entity.position().add(0, 1, 0);

            // 垂直能量柱
            for (int y = 0; y < 5; y++) {
                serverLevel.sendParticles(
                        ParticleTypes.SCULK_CHARGE_POP,
                        pos.x, pos.y + y*0.8, pos.z,
                        15, 0.3, 0.5, 0.3, 0.2
                );
            }

            // 音爆
            serverLevel.sendParticles(
                    ParticleTypes.SONIC_BOOM,
                    pos.x, pos.y + 1, pos.z,
                    8, 1.0, 0.5, 1.0, 0
            );

            // 强化音效
            serverLevel.playSound(
                    null, pos.x, pos.y, pos.z,
                    SoundEvents.RESPAWN_ANCHOR_DEPLETE.get(),
                    SoundSource.PLAYERS,
                    2.0F, 0.5F
            );
        }
    }

    private static void applyChainEffect(LivingEntity attacker, LivingEntity from, LivingEntity to, float baseDamage) {
        // 概率伤害倍率
        float damageMultiplier = ThreadLocalRandom.current().nextFloat() <= 0.25F ? 1.25F : 2.0F;
        to.hurt(attacker.damageSources().magic(), baseDamage * damageMultiplier);
        to.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 33*20, 2));

        // 生成链式音爆粒子
        if (from.level() instanceof ServerLevel serverLevel) {
            // 计算粒子路径
            Vec3 start = from.getEyePosition(1.0f);
            Vec3 end = to.getEyePosition(1.0f);
            Vec3 direction = end.subtract(start);
            double distance = direction.length();
            direction = direction.normalize();

            // 生成连接线粒子
            for (double d = 0; d < distance; d += 0.5) {
                Vec3 pos = start.add(direction.scale(d));
                serverLevel.sendParticles(
                        ParticleTypes.ELECTRIC_SPARK,
                        pos.x, pos.y + 0.2, pos.z,
                        1, 0, 0, 0, 0
                );
            }

            // 生成端点特效
            serverLevel.sendParticles(
                    ParticleTypes.SONIC_BOOM,
                    start.x, start.y + 0.5, start.z,
                    3, 0.2, 0.2, 0.2, 0
            );
            serverLevel.sendParticles(
                    ParticleTypes.SONIC_BOOM,
                    end.x, end.y + 0.5, end.z,
                    3, 0.2, 0.2, 0.2, 0
            );

            // 播放音爆音效
            serverLevel.playSound(
                    null,
                    start.x, start.y, start.z,
                    SoundEvents.WARDEN_SONIC_BOOM,
                    SoundSource.PLAYERS,
                    2.0F, 1.8F - 0.2F * (float)(distance / 10)
            );
        }
    }
}