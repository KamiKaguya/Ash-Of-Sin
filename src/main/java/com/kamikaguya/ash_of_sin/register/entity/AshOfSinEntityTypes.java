package com.kamikaguya.ash_of_sin.register.entity;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AshOfSinEntityTypes {
    private static final DeferredRegister<EntityType<?>> registry;

    public AshOfSinEntityTypes() {
    }

    public static void register(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AshOfSin.MODID);
    }
}