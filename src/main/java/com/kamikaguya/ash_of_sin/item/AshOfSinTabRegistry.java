package com.kamikaguya.ash_of_sin.item;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.register.items.AshOfSinItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AshOfSinTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AshOfSin.MODID);
    public static final List<Supplier<Item>> TAB_WEAPONS_LIST = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> TAB_WEAPONS = TAB_REGISTER.register("weapons", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + AshOfSin.MODID + ".weapons"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(AshOfSinItemRegistry.MIRROR_OF_THE_DARK_NIGHT.get()))
            // properly order tab after block tab
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            // Add default items to tab
            .displayItems((params, output) -> {
                TAB_WEAPONS_LIST.forEach(block -> output.accept(block.get()));
            })
            .build()
    );
}