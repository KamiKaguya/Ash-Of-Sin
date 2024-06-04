package com.kamikaguya.ash_of_sin.world.item;

import com.kamikaguya.ash_of_sin.main.AshOfSin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

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

public class EaItem extends WeaponItem{
    @OnlyIn(Dist.CLIENT)
    private List<Component> tooltipExpand;
    public EaItem(Item.Properties build) {
        super(AshOfSinItemTier.EA, 0, 0F, build);
        if (AshOfSin.isPhysicalClient()) {
            this.tooltipExpand = new ArrayList<Component> ();
            this.tooltipExpand.add(new TextComponent(""));
            this.tooltipExpand.add(new TranslatableComponent("item." + AshOfSin.MODID + ".ea.tooltip.unique"));
            this.tooltipExpand.add(new TranslatableComponent("item." + AshOfSin.MODID + ".ea.tooltip"));
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() == Items.NETHERITE_BLOCK;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        for (Component txtComp : tooltipExpand) {
            tooltip.add(txtComp);
        }
    }
}