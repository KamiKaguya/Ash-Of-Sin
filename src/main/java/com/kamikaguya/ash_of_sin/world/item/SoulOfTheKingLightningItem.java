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

public class SoulOfTheKingLightningItem extends WeaponItem{
    @OnlyIn(Dist.CLIENT)
    private List<Component> tooltipExpand;
    public SoulOfTheKingLightningItem(Item.Properties build) {
        super(AshOfSinItemTier.SOUL_OF_THE_KING_LIGHTNING, 0, -2.2F, build);
        if (AshOfSin.isPhysicalClient()) {
            this.tooltipExpand = new ArrayList<Component>();
            this.tooltipExpand.add(new TextComponent(""));
            this.tooltipExpand.add(new TranslatableComponent("item." + AshOfSin.MODID + ".soul_of_the_king_lightning.tooltip.unique"));
            this.tooltipExpand.add(new TranslatableComponent("item." + AshOfSin.MODID + ".soul_of_the_king_lightning.tooltip"));
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() == Items.NETHERITE_INGOT;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        for (Component txtComp : tooltipExpand) {
            tooltip.add(txtComp);
        }
    }
}