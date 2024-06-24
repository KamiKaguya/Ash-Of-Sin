package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomAntiHighATKEntityConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomAntiHighATKEntityEvent {

    public static final double MAX_ATK = CustomAntiHighATKEntityConfig.MAX_ATK.get();
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (CustomAntiHighATKEntityConfig.ANTI_ON.get()) {
            if (event.getEntityLiving().level.isClientSide()) {
                return;
            }
            LivingEntity highATKEntity = event.getEntityLiving();
            if (!(highATKEntity.level.isClientSide()) && (CustomAntiHighATKEntityConfig.isHighATKEntity(highATKEntity))) {
                double maxATK = highATKEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (maxATK > MAX_ATK) {
                    setMaxAttackDamage(highATKEntity, MAX_ATK);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (CustomAntiHighATKEntityConfig.ANTI_ON.get()) {
            if (!(event.getWorld().isClientSide()) && (event.getWorld() instanceof ServerLevel)) {
                if (event.getEntity() instanceof LivingEntity highATKEntity && CustomAntiHighATKEntityConfig.isHighATKEntity(highATKEntity)) {
                    double maxATK = highATKEntity.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    if (maxATK > MAX_ATK) {
                        setMaxAttackDamage(highATKEntity, MAX_ATK);
                    }
                }
            }
        }
    }

    public static void setMaxAttackDamage(LivingEntity entity, double maxATK){
        AttributeInstance highATK = entity.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
        if(highATK != null){
            highATK.removeModifiers();
            highATK.setBaseValue(maxATK);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (CustomAntiHighATKEntityConfig.ANTI_ON.get()) {
            if (event.getEntityLiving().level.isClientSide()) {
                return;
            }
            LivingEntity target = event.getEntityLiving();
            Entity highATKEntity = event.getSource().getDirectEntity();
            if (highATKEntity != null && CustomAntiHighATKEntityConfig.isHighATKEntity(highATKEntity)) {
                float originalDamage = event.getAmount();
                if (originalDamage >= MAX_ATK) {
                    float targetMaxHealth = target.getMaxHealth();
                    float damageAfterArmorReduction = damageAfterArmor(target, targetMaxHealth);
                    float damageAfterArmorProtection = damageAfterArmorProtection(target.getArmorSlots(),damageAfterArmorReduction);
                    float reducedDamage = targetMaxHealth - damageAfterArmorProtection;
                    float destinedDeath = targetMaxHealth + reducedDamage;
                    target.setHealth(0.1F);
                    event.setAmount(destinedDeath);
                }
            }
        }
    }

    public static float damageAfterArmor(LivingEntity entity, float baseDamage) {
        float armorValue = entity.getArmorValue();
        float toughnessValue = (float) entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        return damageAfterArmorReduction(armorValue, toughnessValue, baseDamage);
    }

    public static float damageAfterArmorReduction(float armorValue, float toughnessValue, float baseDamage) {
        float damageAfterArmorReduction;
        if (baseDamage <= 1.6 * armorValue + 0.2 * toughnessValue) {
            damageAfterArmorReduction = (1 / (6.25f * toughnessValue + 50f)) * baseDamage * baseDamage
                    + (1f - armorValue / 25f) * baseDamage;
        } else {
            damageAfterArmorReduction = (1f - armorValue / 125f) * baseDamage;
        }
        return damageAfterArmorReduction;
    }

    public static float damageAfterArmorProtection(Iterable<ItemStack> armorItems, float damageAfterArmorReduction) {
        float damageAfterArmorProtection = 0;
        for (ItemStack armorItem : armorItems) {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(armorItem);
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                if (entry.getKey() instanceof ProtectionEnchantment) {
                    int protectLevel = entry.getValue();

                    damageAfterArmorProtection += damageAfterArmorReduction * (10 / ((protectLevel + 10.0f) / 2));
                }
            }
        }
        return damageAfterArmorProtection;
    }
}