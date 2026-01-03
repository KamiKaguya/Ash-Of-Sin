package com.kamikaguya.ash_of_sin.main;

import com.kamikaguya.ash_of_sin.CommonProxy;
import com.kamikaguya.ash_of_sin.client.ClientProxy;
import com.kamikaguya.ash_of_sin.config.*;
import com.kamikaguya.ash_of_sin.register.AshOfSinRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("ash_of_sin")
public class AshOfSin {

    public static final String MODID = "ash_of_sin";
    public final AshOfSinConfig ashOfSinConfig = new AshOfSinConfig();
    public final CustomAntiEnchantmentEntityConfig customAntiEnchantmentEntityConfig = new CustomAntiEnchantmentEntityConfig();
    public final CustomAntiSeatEntityConfig customAntiSeatEntityConfig = new CustomAntiSeatEntityConfig();
    public final CustomAntiItemEntityConfig customAntiItemEntityConfig = new CustomAntiItemEntityConfig();
    public final CustomEntityAntiEffectConfig customEntityAntiEffectConfig = new CustomEntityAntiEffectConfig();
    public final CustomAntiTrapCageEntityConfig customAntiTrapCageEntityConfig = new CustomAntiTrapCageEntityConfig();
    public final CustomEntityEffectConfigManager customEntityEffectConfigManager = new CustomEntityEffectConfigManager();
    public final CustomEntityItemConfigManager customEntityItemConfigManager = new CustomEntityItemConfigManager();
    public final CustomEntityAttackEffectConfig customEntityAttackEffectConfig = new CustomEntityAttackEffectConfig();
    public final AntiHighLevelEnchantmentConfig antiHighLevelEnchantmentConfig = new AntiHighLevelEnchantmentConfig();
    public final SoulLikeBossBattleConfig soulLikeBossBattleConfig = new SoulLikeBossBattleConfig();
    public final BetterAIConfig betterAIConfig = new BetterAIConfig();
    public final AntiSameModifierConfig antiSameModifierConfig = new AntiSameModifierConfig();
    public final AdventureDimensionConfig adventureDimensionConfig = new AdventureDimensionConfig();

    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public AshOfSin() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        AshOfSinRegistry.register(bus);
        PROXY.init();
        bus.addListener(this::setup);
        bus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent event) {
        ashOfSinConfig.loadConfig();
        customAntiEnchantmentEntityConfig.loadConfig();
        customAntiSeatEntityConfig.loadConfig();
        customAntiItemEntityConfig.loadConfig();
        customEntityAntiEffectConfig.loadConfig();
        customAntiTrapCageEntityConfig.loadConfig();
        customEntityEffectConfigManager.loadConfig();
        customEntityItemConfigManager.loadConfig();
        customEntityAttackEffectConfig.loadConfig();
        antiHighLevelEnchantmentConfig.loadConfig();
        soulLikeBossBattleConfig.loadConfig();
        betterAIConfig.loadConfig();
        antiSameModifierConfig.loadConfig();
        adventureDimensionConfig.loadConfig();
        event.enqueueWork(() -> {
            PROXY.setup();
        });
    }

    public void doClientStuff(final FMLClientSetupEvent event) {}

    public static boolean isPhysicalClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    public void setupClient(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> PROXY.clientInit());
    }

    public void setupComplete(final FMLLoadCompleteEvent event) {
        PROXY.postInit();
    }

    public static @NotNull ResourceLocation identifier(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @Deprecated(forRemoval = true)
    public static @NotNull ResourceLocation rl(@NotNull String path) {
        return identifier(path);
    }
}