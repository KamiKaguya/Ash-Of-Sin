package com.kamikaguya.ash_of_sin.register.enchantment;

import com.kamikaguya.ash_of_sin.enchantment.MainTankEnchantment;
import com.kamikaguya.ash_of_sin.enchantment.SubTankEnchantment;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AshOfSinEnchantments {
    private static final DeferredRegister<Enchantment> registry;
    public static final RegistryObject<Enchantment> MAIN_TANK;
    public static final RegistryObject<Enchantment> SUB_TANK;

    public AshOfSinEnchantments() {
    }

    public static void register(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AshOfSin.MODID);
        MAIN_TANK = registry.register("main_tank", MainTankEnchantment::new);
        SUB_TANK = registry.register("sub_tank", SubTankEnchantment::new);
    }
}