package com.kamikaguya.ash_of_sin.gameasset;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AshOfSinSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AshOfSin.MODID);
    public static final RegistryObject<SoundEvent> GATE_AMBIENT = registerSound("mob.gate.gate_ambient_loop");
    public static final RegistryObject<SoundEvent> GATE_OPEN = registerSound("mob.gate.gate_open");
    public static final RegistryObject<SoundEvent> GATE_WARP = registerSound("mob.gate.gate_warp");
    public static final RegistryObject<SoundEvent> TICK_TACK = registerSound("enchantment.chalk_wall.tick_tack");
    public static final RegistryObject<SoundEvent> SKILL_DEVOUR = registerSound("item.mirror_of_the_dark_night.skill_devour");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(AshOfSin.MODID, name)));
    }
}