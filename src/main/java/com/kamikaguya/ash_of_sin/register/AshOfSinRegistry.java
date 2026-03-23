package com.kamikaguya.ash_of_sin.register;

import com.kamikaguya.ash_of_sin.register.enchantment.AshOfSinEnchantments;
import net.minecraftforge.eventbus.api.IEventBus;

public class AshOfSinRegistry {
    public AshOfSinRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        AshOfSinEnchantments.register(modEventBus);
    }
}