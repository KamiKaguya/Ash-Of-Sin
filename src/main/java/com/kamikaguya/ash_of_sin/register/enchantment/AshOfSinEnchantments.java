package com.kamikaguya.ash_of_sin.register.enchantment;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.enchantment.AbsoluteRuleEnchantment;
import com.kamikaguya.ash_of_sin.enchantment.ChalkWallEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AshOfSinEnchantments {
    private static final DeferredRegister<Enchantment> registry;
    public static final RegistryObject<Enchantment> ABSOLUTE_RULE;
    public static final RegistryObject<Enchantment> CHALK_WALL;

    public AshOfSinEnchantments() {
    }

    public static void register(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AshOfSin.MODID);
        ABSOLUTE_RULE = registry.register("absolute_rule", AbsoluteRuleEnchantment::new);
        CHALK_WALL = registry.register("chalk_wall", ChalkWallEnchantment::new);
    }
}