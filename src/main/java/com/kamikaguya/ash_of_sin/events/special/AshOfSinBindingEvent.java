package com.kamikaguya.ash_of_sin.events.special;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import com.kamikaguya.ash_of_sin.world.entity.KamiKaguya;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinBindingEvent {
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide() || event.getEntity().level.isClientSide()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        DamageSource damageSource = event.getSource();
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof ServerPlayer playerHolder) {
            if (mismatchingPlayerHoldUniqueWeapon(playerHolder)) {
                event.setAmount(0);
            }
        }
    }

    public static boolean mismatchingPlayerHoldUniqueWeapon(ServerPlayer holder) {
        ItemStack mainHand = holder.getMainHandItem();
        ItemStack offHand = holder.getOffhandItem();
        boolean kamiWeapon = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "mirror_of_the_dark_night")) ||
                mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "crescent")) ||
                mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "crescent_sheath")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "mirror_of_the_dark_night")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "crescent")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "crescent_sheath")) ||
                mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "venuzdonoa")) ||
                mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "dark_moon_greatsword"));
        boolean isOP = holder.getGameProfile().getName().contains("KamiKaguya") ||
                holder.getGameProfile().getName().contains("wangumao") ||
                holder.getGameProfile().getName().contains("Tahora") ||
                holder.getGameProfile().getName().contains("vita8356") ||
                holder.getGameProfile().getName().contains("Death_Leaves") ||
                holder.getGameProfile().getName().contains("RomanticFlower");
        if (!(mainHand.isEmpty()) && (kamiWeapon)) {
            return !isOP;
        }
        if (!(offHand.isEmpty()) && (kamiWeapon)) {
            return !isOP;
        }

        boolean carianKnightsSword = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "carian_knights_sword"));
        if (!(mainHand.isEmpty()) && (carianKnightsSword)) {
            return !isOP;
        }

        boolean ea = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "ea"));
        boolean is3_yyy = holder.getGameProfile().getName().contains("3_yyy");
        if (!(mainHand.isEmpty()) && (ea)) {
            if (!isOP) {
                return !is3_yyy;
            }
        }

        boolean chaosMeleeBlade = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "chaos_melee_blade"));
        boolean isS1mpo = holder.getGameProfile().getName().contains("S1mpo");
        if (!(mainHand.isEmpty()) && (chaosMeleeBlade)) {
            if (!isOP) {
                return !isS1mpo;
            }
        }

        boolean subCravenBow = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "sub_craven_bow"));
        boolean isCyuui = holder.getGameProfile().getName().contains("Cyuui");
        if (!(mainHand.isEmpty()) && (subCravenBow)) {
            if (!isOP) {
                return !isCyuui;
            }
        }

        boolean shikamaDoji = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "shikama_doji"));
        boolean ishhhh = holder.getGameProfile().getName().contains("hhhh");
        if (!(mainHand.isEmpty()) && (shikamaDoji)) {
            if (!isOP) {
                return !ishhhh;
            }
        }

        boolean flameKatanaCaravella = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "flame_katana_caravella")) ||
                mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "flame_katana_caravella_sheath")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "flame_katana_caravella_sheath")) ||
                mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "soul_of_the_king_fire")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "soul_of_the_king_fire")) ||
                mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "soul_of_the_king_lightning")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "soul_of_the_king_lightning"));
        boolean isWenH_ = holder.getGameProfile().getName().contains("WenH_");
        if (!(mainHand.isEmpty()) && (flameKatanaCaravella)) {
            if (!isOP) {
                return !isWenH_;
            }
        }
        if (!(offHand.isEmpty()) && (flameKatanaCaravella)) {
            if (!isOP) {
                return !isWenH_;
            }
        }

        boolean kirito = mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "elucidator")) ||
                mainHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "dark_repulser")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "elucidator")) ||
                offHand.getItem().getRegistryName().equals(new ResourceLocation(AshOfSin.MODID, "dark_repulser"));
        if (!(mainHand.isEmpty()) && (kirito)) {
            return !isOP;
        }
        if (!(offHand.isEmpty()) && (kirito)) {
            return !isOP;
        }

        return false;
    }
}