package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomAntiEnchantmentEntityConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomAntiEnchantmentEntityEvent {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide()) {
            return;
        }
        LivingEntity livingEntity = event.getEntityLiving();
        ResourceLocation entityResourceLocation = EntityType.getKey(livingEntity.getType());
        DamageSource source = event.getSource();

        List<? extends String> antiEnchantmentEntityList = CustomAntiEnchantmentEntityConfig.ANTI_ENCHANTMENT_ENTITY.get();
        List<? extends String> antiEnchantmentList = CustomAntiEnchantmentEntityConfig.ANTI_ENCHANTMENT.get();

        if (antiEnchantmentEntityList.contains(entityResourceLocation.toString()) && source.getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) source.getEntity();

            if (isItemEnchanted(attacker.getMainHandItem(), antiEnchantmentList) || isItemEnchanted(attacker.getOffhandItem(), antiEnchantmentList)) {
                event.setAmount(0);
            }
        }
    }

    private static boolean isItemEnchanted(ItemStack item, List<? extends String> antiEnchantmentList) {
        if(item.isEmpty()) return false;

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);

        for(Enchantment enchantment : enchantments.keySet()) {
            ResourceLocation enchantmentLocation = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
            if(enchantmentLocation != null && antiEnchantmentList.contains(enchantmentLocation.toString())) {
                return true;
            }
        }
        return false;
    }
}
