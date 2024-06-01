package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.config.AntiHighLevelEnchantmentConfig;
import com.kamikaguya.ash_of_sin.config.AntiHighLevelEnchantmentLevelConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinAntiHighLevelEnchantmentEvent {
    private static final AntiHighLevelEnchantmentConfig config = new AntiHighLevelEnchantmentConfig();

    static {
        config.loadConfig();
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        Map<String, Integer> maxEnchantmentLevels = loadConfigFrom(config.getAntiHighLevelEnchantmentConfig());

        ItemStack targetItem = event.getLeft();
        ItemStack sacrificeItem = event.getRight();

        Map<Enchantment, Integer> targetEnchantments = EnchantmentHelper.getEnchantments(targetItem);
        checkAndCancelIfExceedMaxLevel(targetEnchantments, maxEnchantmentLevels, event);

        Map<Enchantment, Integer> sacrificeEnchantments = EnchantmentHelper.getEnchantments(sacrificeItem);
        checkAndCancelIfExceedMaxLevel(sacrificeEnchantments, maxEnchantmentLevels, event);
    }

    private static void checkAndCancelIfExceedMaxLevel(Map<Enchantment, Integer> enchantments, Map<String, Integer> maxEnchantmentLevels, AnvilUpdateEvent event) {
        boolean shouldCancel = false;
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            String registryName = entry.getKey().getRegistryName().toString();
            Integer maxLevel = maxEnchantmentLevels.get(registryName);
            if (maxLevel != null && entry.getValue() >= maxLevel) {
                shouldCancel = true;
                break;
            }
        }
        if (shouldCancel) {
            event.setCanceled(true);
        }
    }

    private static Map<String, Integer> loadConfigFrom(List<AntiHighLevelEnchantmentLevelConfig> configList) {
        Map<String, Integer> configMap = new HashMap<>();
        for (AntiHighLevelEnchantmentLevelConfig configItem : configList) {
            configMap.put(configItem.getEnchantment(), configItem.getLevel());
        }
        return configMap;
    }
}