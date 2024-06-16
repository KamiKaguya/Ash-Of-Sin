package com.kamikaguya.ash_of_sin.events;

import com.kamikaguya.ash_of_sin.config.CustomAntiItemEntityConfig;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinCustomAntiItemEntityEvent {
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (CustomAntiItemEntityConfig.ANTI_ON.get()) {
            if (event.getEntityLiving().level.isClientSide()) {
                return;
            }
            LivingEntity livingEntity = event.getEntityLiving();
            ResourceLocation entityResourceLocation = EntityType.getKey(livingEntity.getType());
            DamageSource source = event.getSource();

            List<? extends String> antiItemEntityList = CustomAntiItemEntityConfig.ANTI_ITEM_ENTITY.get();
            List<? extends String> antiItemList = CustomAntiItemEntityConfig.ANTI_ITEM.get();

            if (antiItemEntityList.contains(entityResourceLocation.toString()) && source.getEntity() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) source.getEntity();

                ItemStack mainHandItem = attacker.getMainHandItem();
                ItemStack offHandItem = attacker.getOffhandItem();
                ResourceLocation mainHandItemResLoc = mainHandItem.getItem().getRegistryName();
                ResourceLocation offHandItemResLoc = offHandItem.getItem().getRegistryName();

                if ((mainHandItemResLoc != null && antiItemList.contains(mainHandItemResLoc.toString())) ||
                        (offHandItemResLoc != null && antiItemList.contains(offHandItemResLoc.toString()))) {
                    event.setAmount(0);
                }
            }
        }
    }
}
