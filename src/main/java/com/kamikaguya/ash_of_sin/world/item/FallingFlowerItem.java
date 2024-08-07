package com.kamikaguya.ash_of_sin.world.item;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FallingFlowerItem extends WeaponItem {
    @OnlyIn(Dist.CLIENT)
    public List<Component> tooltipExpand;
    public FallingFlowerItem(Item.Properties build) {
        super(AshOfSinItemTier.FALLING_FLOWER, 0, -2.8F, build);
        if (AshOfSin.isPhysicalClient()) {
            this.tooltipExpand = new ArrayList<Component>();
            this.tooltipExpand.add(new TextComponent(""));
            this.tooltipExpand.add(new TranslatableComponent("item." + AshOfSin.MODID + ".falling_flower.tooltip.unique"));
            this.tooltipExpand.add(new TranslatableComponent("item." + AshOfSin.MODID + ".falling_flower.tooltip"));
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() == Items.AMETHYST_SHARD;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        for (Component txtComp : tooltipExpand) {
            tooltip.add(txtComp);
        }
    }
}