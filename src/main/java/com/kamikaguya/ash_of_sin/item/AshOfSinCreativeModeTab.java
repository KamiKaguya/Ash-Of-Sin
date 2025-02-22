package com.kamikaguya.ash_of_sin.item;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.register.items.AshOfSinItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class AshOfSinCreativeModeTab {
    public static final CreativeModeTab WEAPONS = new CreativeModeTab("ash_of_sin.weapons") {
        public ItemStack makeIcon() {
            return new ItemStack((ItemLike) AshOfSinItems.MIRROR_OF_THE_DARK_NIGHT.get());
        }
    };

    public AshOfSinCreativeModeTab() {
    }
}