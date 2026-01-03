package com.kamikaguya.ash_of_sin.register;

import com.kamikaguya.ash_of_sin.register.enchantment.AshOfSinEnchantments;
import com.kamikaguya.ash_of_sin.register.entity.AshOfSinEntityTypes;
import com.kamikaguya.ash_of_sin.register.items.AshOfSinItemRegistry;
import com.kamikaguya.ash_of_sin.register.sound.AshOfSinSoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class AshOfSinRegistry {
    public AshOfSinRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        AshOfSinItemRegistry.ITEMS.register(modEventBus);
        AshOfSinItemRegistry.ASH_OF_SIN_WEAPONS_TAB.register(modEventBus);
        AshOfSinEntityTypes.register(modEventBus);
        AshOfSinSoundEvent.register(modEventBus);
        AshOfSinEnchantments.register(modEventBus);
    }
}