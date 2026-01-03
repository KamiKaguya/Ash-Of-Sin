package com.kamikaguya.ash_of_sin.register.sound;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AshOfSinSoundEvent {
    private static final DeferredRegister<SoundEvent> registry;
    public static final RegistryObject<SoundEvent> TICK_TACK;
    public static final RegistryObject<SoundEvent> SKILL_DEVOUR;

    public AshOfSinSoundEvent() {
    }

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return registry.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AshOfSin.MODID, name)));
    }

    public static void register(IEventBus modEventBus) {
        registry.register(modEventBus);
    }

    static {
        registry = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AshOfSin.MODID);
        TICK_TACK = registerSoundEvent("enchantment.chalk_wall.tick_tack");
        SKILL_DEVOUR = registerSoundEvent("item.mirror_of_the_dark_night.skill_devour");
    }
}