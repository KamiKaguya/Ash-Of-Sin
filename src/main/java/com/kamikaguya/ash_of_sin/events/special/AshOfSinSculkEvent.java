package com.kamikaguya.ash_of_sin.events.special;

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

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity target = event.getEntity();
        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof LivingEntity livingEntity) {
            if (RANDOM.nextFloat() < 0.15F && holdSculkWeapon(livingEntity)) {
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
                target.level.playSound(null, target.getOnPos(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.PLAYERS, 1.0f, 1.0f);
            }
        }
    }

    public static boolean holdSculkWeapon(LivingEntity entity) {
        ItemStack mainHand = entity.getMainHandItem();
        return !mainHand.isEmpty() && SCULK_WEAPONS.contains(ForgeRegistries.ITEMS.getKey(mainHand.getItem()));
    }

    @SubscribeEvent
    public static void immuneDarkness(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level.isClientSide() || event.getEntity().level.isClientSide()) {
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
        // 提前过滤虚拟实体
        if (isTargetDummy(target)) return;

        // 获取范围内实体
        AABB area = new AABB(target.blockPosition()).inflate(7);
        List<LivingEntity> entities = target.level.getEntitiesOfClass(LivingEntity.class, area);

        entities.stream()
                .filter(e -> e.getType() == target.getType())
                .filter(e -> !holdSculkWeapon(e))
                .forEach(e -> applySonicEffect(e, attacker, damage));
    }

    private static boolean isTargetDummy(LivingEntity entity) {
        return ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("dummmmmmy:target_dummy")) == entity.getType();
    }

    private static void applySonicEffect(LivingEntity entity, LivingEntity attacker, float baseDamage) {
        // 概率计算伤害倍率
        float damageMultiplier = ThreadLocalRandom.current().nextFloat() <= 0.25F ? 1.25F : 2.0F;
        entity.hurt(DamageSource.mobAttack(attacker).setMagic(), baseDamage * damageMultiplier);

        // 添加效果和粒子
        entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 33*20, 2));
        spawnSonicParticles(entity);
    }

    private static void spawnSonicParticles(LivingEntity entity) {
        if (entity.level instanceof ServerLevel serverLevel) {
            Vec3 pos = entity.position();
            serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x, pos.y, pos.z,
                    1, 0, 0, 0, 0);
            serverLevel.playSound(null, pos.x, pos.y, pos.z,
                    SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 5.0F, 1.0F);
        }
    }
}