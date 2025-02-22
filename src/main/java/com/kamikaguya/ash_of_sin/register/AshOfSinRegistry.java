package com.kamikaguya.ash_of_sin.register;

import com.kamikaguya.ash_of_sin.register.enchantment.AshOfSinEnchantments;
import com.kamikaguya.ash_of_sin.register.entity.AshOfSinEntityTypes;
import com.kamikaguya.ash_of_sin.register.items.AshOfSinItems;
import com.kamikaguya.ash_of_sin.register.sound.AshOfSinSoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class AshOfSinRegistry {
    public AshOfSinRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        AshOfSinEntityTypes.register(modEventBus);
        AshOfSinSoundEvent.register(modEventBus);
        AshOfSinItems.register(modEventBus);
        AshOfSinEnchantments.register(modEventBus);
    }
}