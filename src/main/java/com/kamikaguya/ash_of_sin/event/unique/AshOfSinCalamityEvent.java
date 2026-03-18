package com.kamikaguya.ash_of_sin.event.unique;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.damagesource.AshOfSinDamageSources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCalamityEvent {
    private static final UUID DAMAGE_BONUS_UUID = UUID.fromString("a4a4a4a4-b3b3-c3c3-d3d3-e3e3e3e3e3e3");
    private static final Random RANDOM = new Random();

    private static final int THRESHOLD_MINING_FATIGUE = 30 * 20;
    private static final int THRESHOLD_WEAKNESS = 60 * 20;
    private static final int THRESHOLD_MOVEMENT_SLOWDOWN = 90 * 20;
    private static final int THRESHOLD_UPGRADE = 120 * 20;

    private static final int MAX_CHARGES = 7;
    private static final int CHARGE_DURATION_PER_LEVEL = 3 * 20;

    private static int getPlayerData(ServerPlayer player, String key) {
        return player.getPersistentData().getInt(key);
    }

    private static void setPlayerData(ServerPlayer player, String key, int value) {
        player.getPersistentData().putInt(key, value);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer player)) return;

        boolean holding = holdCalamity(player);

        int holdTime = getPlayerData(player, "calamityHoldTime");
        if (holding) {
            holdTime++;
            setPlayerData(player, "calamityHoldTime", holdTime);
        } else {
            if (holdTime > 0) {
                setPlayerData(player, "calamityHoldTime", 0);
            }
            return;
        }

        // ---------- 基础厄运：霉运 ----------
        player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 100, 9, false, false, true));

        // ---------- 累积负面效果 ----------
        // 挖掘疲劳
        if (holdTime >= THRESHOLD_MINING_FATIGUE) {
            int amplifier = (holdTime >= THRESHOLD_UPGRADE) ? 2 : 1; // I级 -> II级
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, amplifier, false, false, true));
        }
        // 虚弱
        if (holdTime >= THRESHOLD_WEAKNESS) {
            int amplifier = (holdTime >= THRESHOLD_UPGRADE) ? 2 : 1;
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, amplifier, false, false, true));
        }
        // 缓慢
        if (holdTime >= THRESHOLD_MOVEMENT_SLOWDOWN) {
            int amplifier = (holdTime >= THRESHOLD_UPGRADE) ? 3 : 2;
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, amplifier, false, false, true));
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (!holdCalamity(player)) return;

        if (RANDOM.nextFloat() < 0.15f) {
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage == null) return;

            attackDamage.removeModifier(DAMAGE_BONUS_UUID);

            AttributeModifier modifier = new AttributeModifier(
                    DAMAGE_BONUS_UUID,
                    "Calamity attack damage bonus",
                    0.5,
                    AttributeModifier.Operation.MULTIPLY_BASE
            );
            attackDamage.addTransientModifier(modifier);
        }
    }

    /**
     * 击杀增加充能
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        Entity source = event.getSource().getEntity();
        if (!(source instanceof ServerPlayer player)) return;
        if (!holdCalamity(player)) return;

        int charges = getPlayerData(player, "calamityCharges");
        if (charges < MAX_CHARGES) {
            setPlayerData(player, "calamityCharges", charges + 1);
        }
    }

    /**
     * 右键释放充能（潜行+右键）
     */
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!holdCalamity(player)) return;
        if (!player.isShiftKeyDown()) return;

        int charges = getPlayerData(player, "calamityCharges");
        if (charges <= 0) return;

        setPlayerData(player, "calamityCharges", 0);

        // 添加力量和抗性，持续时间 = 充能层数 * 每层持续时间
        int duration = charges * CHARGE_DURATION_PER_LEVEL;
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, 4, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 3, false, false, true));
    }

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!holdCalamity(player)) {
            setPlayerData(player, "calamityHoldTime", 0);
        }
    }

    public static boolean holdCalamity(LivingEntity livingEntity) {
        ItemStack mainHand = livingEntity.getMainHandItem();
        boolean holdCalamity = ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "calamity_blade")) ||
                ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "calamity_blade_thin")) ||
                ForgeRegistries.ITEMS.getKey(mainHand.getItem()).equals(new ResourceLocation(AshOfSin.MODID, "calamity_scythe"));
        return !(mainHand.isEmpty()) && (holdCalamity);
    }
}