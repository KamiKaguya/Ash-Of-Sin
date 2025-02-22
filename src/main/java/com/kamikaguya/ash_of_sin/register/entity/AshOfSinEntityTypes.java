package com.kamikaguya.ash_of_sin.register.entity;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.entity.Gate;
import com.kamikaguya.ash_of_sin.entity.KamiKaguya;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AshOfSinEntityTypes {
    private static final DeferredRegister<EntityType<?>> registry;
    public static final RegistryObject<EntityType<KamiKaguya>> KAMIKAGUYA;
    public static final RegistryObject<EntityType<Gate>> GATE;

    public AshOfSinEntityTypes() {
    }

    public static void register(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AshOfSin.MODID);
        KAMIKAGUYA = registry.register("kamikaguya", () -> EntityType.Builder.of(KamiKaguya::new, MobCategory.MISC)
                .sized(0.6F, 1.8F)
                .build("kamikaguya"));
        GATE = registry.register("gate", () -> EntityType.Builder.of(Gate::new, MobCategory.MISC)
                .sized(2F, 3F)
                .build("gate"));
    }
}