package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomEntityItemConfig;
import com.kamikaguya.ash_of_sin.config.CustomItemConfig;
import com.kamikaguya.ash_of_sin.config.CustomEntityItemConfigManager;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomEntityItemEvent {
    private static CustomEntityItemConfigManager customEntityItemConfigManager = new CustomEntityItemConfigManager();

    @SubscribeEvent
    public void onLivingSpawnEvent(LivingSpawnEvent event) {
        LivingEntity entity = event.getEntityLiving();
        List<CustomEntityItemConfig> configs = customEntityItemConfigManager.getCustomEntityItemConfig();

        for (CustomEntityItemConfig config : configs) {
            if (config.getEntity().contains(entity.getType().getRegistryName().toString())) {
                CustomItemConfig itemsConfig = config.getItem();

                if (!itemsConfig.getHelmet().isEmpty()) {
                    ItemStack helmetItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemsConfig.getHelmet().get(0))));

                    if (!itemsConfig.getHelmetNBT().isEmpty() && !itemsConfig.getHelmetNBT().get(0).isEmpty()) {
                        try {
                            CompoundTag nbtData = TagParser.parseTag(itemsConfig.getHelmetNBT().get(0));
                            helmetItem.setTag(nbtData);
                        } catch (CommandSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    entity.setItemSlot(EquipmentSlot.HEAD, helmetItem);
                }

                if (!itemsConfig.getChestplate().isEmpty()) {
                    ItemStack chestplateItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemsConfig.getChestplate().get(0))));

                    if (!itemsConfig.getChestplateNBT().isEmpty() && !itemsConfig.getChestplateNBT().get(0).isEmpty()) {
                        try {
                            CompoundTag nbtData = TagParser.parseTag(itemsConfig.getChestplateNBT().get(0));
                            chestplateItem.setTag(nbtData);
                        } catch (CommandSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    entity.setItemSlot(EquipmentSlot.CHEST, chestplateItem);
                }

                if (!itemsConfig.getLeggings().isEmpty()) {
                    ItemStack leggingsItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemsConfig.getLeggings().get(0))));

                    if (!itemsConfig.getLeggingsNBT().isEmpty() && !itemsConfig.getLeggingsNBT().get(0).isEmpty()) {
                        try {
                            CompoundTag nbtData = TagParser.parseTag(itemsConfig.getLeggingsNBT().get(0));
                            leggingsItem.setTag(nbtData);
                        } catch (CommandSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    entity.setItemSlot(EquipmentSlot.LEGS, leggingsItem);
                }

                if (!itemsConfig.getBoots().isEmpty()) {
                    ItemStack bootsItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemsConfig.getBoots().get(0))));

                    if (!itemsConfig.getBootsNBT().isEmpty() && !itemsConfig.getBootsNBT().get(0).isEmpty()) {
                        try {
                            CompoundTag nbtData = TagParser.parseTag(itemsConfig.getBootsNBT().get(0));
                            bootsItem.setTag(nbtData);
                        } catch (CommandSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    entity.setItemSlot(EquipmentSlot.FEET, bootsItem);
                }

                if (!itemsConfig.getMainhand().isEmpty()) {
                    ItemStack mainhandItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemsConfig.getMainhand().get(0))));

                    if (!itemsConfig.getMainhandNBT().isEmpty() && !itemsConfig.getMainhandNBT().get(0).isEmpty()) {
                        try {
                            CompoundTag nbtData = TagParser.parseTag(itemsConfig.getMainhandNBT().get(0));
                            mainhandItem.setTag(nbtData);
                        } catch (CommandSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    entity.setItemSlot(EquipmentSlot.MAINHAND, mainhandItem);
                }

                if (!itemsConfig.getOffhand().isEmpty()) {
                    ItemStack offhandItem = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemsConfig.getOffhand().get(0))));

                    if (!itemsConfig.getOffhandNBT().isEmpty() && !itemsConfig.getOffhandNBT().get(0).isEmpty()) {
                        try {
                            CompoundTag nbtData = TagParser.parseTag(itemsConfig.getOffhandNBT().get(0));
                            offhandItem.setTag(nbtData);
                        } catch (CommandSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    entity.setItemSlot(EquipmentSlot.OFFHAND, offhandItem);
                }
            }
        }
    }
}
