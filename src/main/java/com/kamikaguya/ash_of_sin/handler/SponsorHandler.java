package com.kamikaguya.ash_of_sin.handler;

import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SponsorHandler {
    public SponsorHandler() {
    }

    private static final Set<String> SPONSORS = Set.of(
            "KamiKaguya", "wangumao", "Tahora", "vita8356", "Death_Leaves",
            "RomanticFlower", "DoremySweet_DS", "3_yyy", "S1mpo", "Cyuui",
            "Aurour_", "WenH_", "COC", "is_a_pigeon", "LiuLiuLiang",
            "Midnightovo", "Wh1t3zZ__", "Skadi_sukida", "Kirino_Sae",
            "Mkiuna", "lokmjikmk", "kizunaaiLOVER", "Ayase02", "hanqing666",
            "Kumamori_Kurumi", "Ruuuuuuuubbish", "Ink_TR", "9tail_fox",
            "yeming", "19811117", "ZhenLii_", "DeaDKIng", "Snow"
    );

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        // 多层空指针防护
        if (player.level.isClientSide()) return;

        MinecraftServer server = player.level.getServer();
        if (server == null) return;

        String username = player.getGameProfile().getName();
        if (!SPONSORS.contains(username)) return;

        // 异步执行前捕获实体引用
        ServerPlayer serverPlayer = (ServerPlayer) player;
        server.execute(() -> {
            // 二次验证玩家状态
            if (!serverPlayer.isAlive()) return;

            Advancement advancement = server.getAdvancements()
                    .getAdvancement(new ResourceLocation("ash_of_sin", "thank_you"));

            if (advancement != null) {
                AdvancementProgress progress = serverPlayer.getAdvancements()
                        .getOrStartProgress(advancement);
                if (!progress.isDone()) {
                    progress.getRemainingCriteria().forEach(criterion ->
                            serverPlayer.getAdvancements().award(advancement, criterion)
                    );
                }
            }
        });
    }
}