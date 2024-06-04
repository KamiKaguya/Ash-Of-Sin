package com.kamikaguya.ash_of_sin.world.item;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AshOfSinCreativeModeTabs {
    public static final CreativeModeTab ITEMS = new CreativeModeTab(AshOfSin.MODID + ".items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AshOfSinItems.ASH_OF_SIN.get());
        }
    };
}