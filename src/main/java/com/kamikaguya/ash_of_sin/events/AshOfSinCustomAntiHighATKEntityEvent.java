package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomAntiHighATKEntityConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomAntiHighATKEntityEvent {

    public static final double MAX_ATK = CustomAntiHighATKEntityConfig.MAX_ATK.get();
    public static final String HIGH_ATK_ENTITY = CustomAntiHighATKEntityConfig.ANTI_HIGH_ATK_ENTITY.get().toString();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (CustomAntiHighATKEntityConfig.ANTI_ON.get() && HIGH_ATK_ENTITY != null) {
            if (event.getEntity().level.isClientSide()) {
                return;
            }
            LivingEntity target = event.getEntity();
            Entity highATKEntity = event.getSource().getDirectEntity();

            EntityType<?> highATKEntityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(HIGH_ATK_ENTITY));
            if (highATKEntity.getType().equals(highATKEntityType) || highATKEntity.getCustomName().equals(HIGH_ATK_ENTITY)) {
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
        float damageAfterToughnessReduction;
        if (toughnessValue == 0) {
            damageAfterArmorReduction = (baseDamage * Math.max(10 / (10 + armorValue), 0.2f));
        } else {
            if (baseDamage > (40 / (toughnessValue + 1))) {
                damageAfterToughnessReduction = baseDamage - ((40 / (toughnessValue + 1)) / 2);
                damageAfterArmorReduction = (damageAfterToughnessReduction * Math.max(10 / (10 + armorValue), 0.2f));
            } else {
                damageAfterToughnessReduction = baseDamage - (40 / (toughnessValue + 1));
                damageAfterArmorReduction = (damageAfterToughnessReduction * Math.max(10 / (10 + armorValue), 0.2f));
            }
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

                    damageAfterArmorProtection += damageAfterArmorReduction * (10 / (10.0f + protectLevel));
                }
            }
        }
        return damageAfterArmorProtection;
    }
}