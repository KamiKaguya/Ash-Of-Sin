package com.kamikaguya.ash_of_sin.world.entity;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AshOfSinEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, AshOfSin.MODID);

    public static final RegistryObject<EntityType<KamiKaguya>> KAMIKAGUYA = ENTITY_TYPES.register("kamikaguya", () -> EntityType.Builder.of(KamiKaguya::new, MobCategory.MISC)
            .sized(0.6F, 1.8F)
            .build(new ResourceLocation(AshOfSin.MODID, "kamikaguya").toString()));
    public static final RegistryObject<EntityType<Gate>> GATE = ENTITY_TYPES.register("gate", () -> EntityType.Builder.of(Gate::new, MobCategory.MISC)
            .sized(2F, 3F)
            .build(new ResourceLocation(AshOfSin.MODID, "gate").toString()));

    public static final RegistryObject<EntityType<Doppelganger>> DOPPELGANGER = ENTITY_TYPES.register("doppelganger", () -> EntityType.Builder.of(Doppelganger::new, MobCategory.MISC)
            .sized(0.6F, 1.8F)
            .build(new ResourceLocation(AshOfSin.MODID, "doppelganger").toString()));

    public static final RegistryObject<EntityType<Another>> ANOTHER = ENTITY_TYPES.register("another", () -> EntityType.Builder.of(Another::new, MobCategory.MISC)
            .sized(0.6F, 1.8F)
            .build(new ResourceLocation(AshOfSin.MODID, "another").toString()));
    public static final RegistryObject<EntityType<Assassin>> ASSASSIN = ENTITY_TYPES.register("assassin", () -> EntityType.Builder.of(Assassin::new, MobCategory.MISC)
            .sized(0.6F, 1.8F)
            .build(new ResourceLocation(AshOfSin.MODID, "assassin").toString()));

    @SubscribeEvent
    public static void onEntityAttributeCreation(final EntityAttributeCreationEvent event) {
        event.put(KAMIKAGUYA.get(), KamiKaguya.createAttributes().build());
        event.put(DOPPELGANGER.get(), Doppelganger.createAttributes().build());
        event.put(ANOTHER.get(), Another.createAttributes().build());
        event.put(ASSASSIN.get(), Assassin.createAttributes().build());
    }
}