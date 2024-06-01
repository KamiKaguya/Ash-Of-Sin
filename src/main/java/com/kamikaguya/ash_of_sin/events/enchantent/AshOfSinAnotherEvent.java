package com.kamikaguya.ash_of_sin.events.enchantent;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import com.kamikaguya.ash_of_sin.tracker.AshOfSinPlayerAttackTargetTracker;
import com.kamikaguya.ash_of_sin.world.entity.Another;
import com.kamikaguya.ash_of_sin.world.entity.AshOfSinEntities;
import com.kamikaguya.ash_of_sin.world.item.AshOfSinItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinAnotherEvent {

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack mainHand = player.getMainHandItem();
        boolean holdTickingWeapon = (mainHand.getItem().getRegistryName().equals(new ResourceLocation("wom","antitheus")) || mainHand.getItem().getRegistryName().equals(AshOfSinItems.SHIKAMA_DOJI));
        if (holdTickingWeapon) {
            return;
        }

        if (!(player.level.isClientSide())) {
            if (hasAnotherEnchantmentAromor(player, AshOfSin.ANOTHER.get())) {
                summonAnother(player);
            }
        }
    }

    private static boolean hasAnotherEnchantmentAromor(Player player, Enchantment enchantment) {
        Iterable<ItemStack> armors = player.getArmorSlots();
        for (ItemStack stack : armors) {
            if (EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack) > 0) {
                return true;
            }
        }
        return false;
    }

    private static final int KILL_COOLDOWN_REDUCTION = 3 * 20;

    private void summonAnother(ServerPlayer player) {
        if (!(player.level.isClientSide())) {

            long currentGameTime = player.level.getGameTime();
            long lastSummonTime = player.getPersistentData().getLong("AnotherLastSummonTime");
            long timeSinceLastSummon = currentGameTime - lastSummonTime;

            if (timeSinceLastSummon >= (3 * 20 * 60)) {
                ServerLevel serverLevel = (ServerLevel) player.level;
                Another another = new Another(AshOfSinEntities.ANOTHER.get(), serverLevel);

                another.setPos(player.getX(), player.getY(), player.getZ());
                another.copyPlayerDataToAnother(player);
                another.copyPlayerAttributes(player);

                serverLevel.addFreshEntity(another);

                player.getPersistentData().putLong("AnotherLastSummonTime", currentGameTime);
                player.getPersistentData().putInt("AnotherSummonCDKillCount", 0);
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (!(event.getEntity().level.isClientSide())) {
            LivingEntity livingEntity = event.getEntityLiving();
            DamageSource damageSource = event.getSource();
            Entity directEntity = damageSource.getDirectEntity();

             if ((livingEntity != null) && !(livingEntity instanceof ServerPlayer) && (directEntity instanceof ServerPlayer killer)) {
                if (hasAnotherEnchantmentAromor(killer, AshOfSin.ANOTHER.get())) {
                    int killCount = killer.getPersistentData().getInt("AnotherSummonCDKillCount") + 1;
                    killer.getPersistentData().putInt("AnotherSummonCDKillCount", killCount);
                    long lastSummonTime = killer.getPersistentData().getLong("AnotherLastSummonTime");
                    updateCoolDown(killer, lastSummonTime);
                }
            }
            if ((livingEntity != null) && !(livingEntity instanceof ServerPlayer) && (directEntity instanceof Another killer)) {
                ServerPlayer owner = (ServerPlayer) killer.level.getPlayerByUUID(killer.getOwnerUUID());
                if (owner != null && killer.getOwnerUUID().equals(owner.getUUID()) && (hasAnotherEnchantmentAromor(owner, AshOfSin.ANOTHER.get()))) {
                    int killCount = owner.getPersistentData().getInt("AnotherSummonCDKillCount") + 1;
                    owner.getPersistentData().putInt("AnotherSummonCDKillCount", killCount);
                    long lastSummonTime = owner.getPersistentData().getLong("AnotherLastSummonTime");
                    updateCoolDown(owner, lastSummonTime);
                }
            }

            if ((livingEntity instanceof ServerPlayer) && (directEntity instanceof ServerPlayer killer)) {
                if (hasAnotherEnchantmentAromor(killer, AshOfSin.ANOTHER.get())) {
                    int killCount = killer.getPersistentData().getInt("AnotherSummonCDKillCount") + 60;
                    killer.getPersistentData().putInt("AnotherSummonCDKillCount", killCount);
                    long lastSummonTime = killer.getPersistentData().getLong("AnotherLastSummonTime");
                    updateCoolDown(killer, lastSummonTime);
                }
            }
            if ((livingEntity instanceof ServerPlayer) && (directEntity instanceof Another killer)) {
                ServerPlayer owner = (ServerPlayer) killer.level.getPlayerByUUID(killer.getOwnerUUID());
                if (owner != null && killer.getOwnerUUID().equals(owner.getUUID()) && (hasAnotherEnchantmentAromor(owner, AshOfSin.ANOTHER.get()))) {
                    int killCount = owner.getPersistentData().getInt("AnotherSummonCDKillCount") + 60;
                    owner.getPersistentData().putInt("AnotherSummonCDKillCount", killCount);
                    long lastSummonTime = owner.getPersistentData().getLong("AnotherLastSummonTime");
                    updateCoolDown(owner, lastSummonTime);
                }
            }

            if ((livingEntity instanceof Another) && (directEntity instanceof ServerPlayer killer)) {
                if (hasAnotherEnchantmentAromor(killer, AshOfSin.ANOTHER.get())) {
                    int killCount = killer.getPersistentData().getInt("AnotherSummonCDKillCount") + 60;
                    killer.getPersistentData().putInt("AnotherSummonCDKillCount", killCount);
                    long lastSummonTime = killer.getPersistentData().getLong("AnotherLastSummonTime");
                    updateCoolDown(killer, lastSummonTime);
                }
            }
            if ((livingEntity instanceof Another) && (directEntity instanceof Another killer)) {
                ServerPlayer owner = (ServerPlayer) killer.level.getPlayerByUUID(killer.getOwnerUUID());
                if (owner != null && killer.getOwnerUUID().equals(owner.getUUID()) && (hasAnotherEnchantmentAromor(owner, AshOfSin.ANOTHER.get()))) {
                    int killCount = owner.getPersistentData().getInt("AnotherSummonCDKillCount") + 60;
                    owner.getPersistentData().putInt("AnotherSummonCDKillCount", killCount);
                    long lastSummonTime = owner.getPersistentData().getLong("AnotherLastSummonTime");
                    updateCoolDown(owner, lastSummonTime);
                }
            }
        }
    }

    private void updateCoolDown(ServerPlayer player, long lastSummonTime) {
        long newCoolDownTime = Math.max(0, lastSummonTime - ((long) AshOfSinAnotherEvent.KILL_COOLDOWN_REDUCTION * player.getPersistentData().getInt("AnotherSummonCDKillCount")));
        player.getPersistentData().putLong("AnotherLastSummonTime", newCoolDownTime);
        player.getPersistentData().putInt("AnotherSummonCDKillCount", 0);

        long currentGameTime = player.level.getGameTime();
        long timeToSummon = currentGameTime - newCoolDownTime;
        if (timeToSummon >= (3 * 20 * 60)) {
            player.displayClientMessage((new TranslatableComponent("message.ash_of_sin.another.timetosummon")).setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withBold(true)), true);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        AshOfSinPlayerAttackTargetTracker.removeOwnerTarget(event.getPlayer().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.getPlayer() instanceof Player && event.getTarget() instanceof LivingEntity target) {
            Player player = event.getPlayer();
            if (!player.level.isClientSide) {
                long lastUpdateTargetTime = player.getPersistentData().getLong("lastUpdateTargetTime");
                if (hasAnotherEnchantmentAromor(player, AshOfSin.ANOTHER.get())) {
                    AshOfSinPlayerAttackTargetTracker.removeOwnerTarget(player.getUUID());
                    player.getPersistentData().putLong("lastUpdateTargetTime", System.currentTimeMillis());

                    if (System.currentTimeMillis() - lastUpdateTargetTime > 1500) {
                        AshOfSinPlayerAttackTargetTracker.updateOwnerTarget(player.getUUID(), target);
                        player.getPersistentData().putLong("lastUpdateTargetTime", System.currentTimeMillis());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        AshOfSinPlayerAttackTargetTracker.removeOwnerTarget(event.getPlayer().getUUID());
    }
}