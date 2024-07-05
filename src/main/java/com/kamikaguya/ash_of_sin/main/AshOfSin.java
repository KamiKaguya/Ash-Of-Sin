package com.kamikaguya.ash_of_sin.main;

import com.kamikaguya.ash_of_sin.CommonProxy;
import com.kamikaguya.ash_of_sin.client.ClientProxy;
import com.kamikaguya.ash_of_sin.config.*;
import com.kamikaguya.ash_of_sin.events.*;
import com.kamikaguya.ash_of_sin.events.enchantent.AshOfSinAnotherEvent;
import com.kamikaguya.ash_of_sin.events.enchantent.AshOfSinChalkWallEvent;
import com.kamikaguya.ash_of_sin.events.special.*;
import com.kamikaguya.ash_of_sin.events.unique.*;
import com.kamikaguya.ash_of_sin.gameasset.AshOfSinSounds;
import com.kamikaguya.ash_of_sin.world.biome.AshOfSinBiomes;
import com.kamikaguya.ash_of_sin.world.dimension.AbsoluteSpaceTimeRealmDimension;
import com.kamikaguya.ash_of_sin.world.effect.WrathOfGod;
import com.kamikaguya.ash_of_sin.world.enchantment.AbsoluteRuleEnchantment;
import com.kamikaguya.ash_of_sin.world.enchantment.AnotherEnchantment;
import com.kamikaguya.ash_of_sin.world.enchantment.ChalkWallEnchantment;
import com.kamikaguya.ash_of_sin.world.enchantment.VenuzdonoaEnchantent;
import com.kamikaguya.ash_of_sin.world.entity.AshOfSinEntities;
import com.kamikaguya.ash_of_sin.world.item.AshOfSinItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.biome.Biome;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("ash_of_sin")
public class AshOfSin {

    public static final String MODID = "ash_of_sin";
    public final AshOfSinConfig ashOfSinConfig = new AshOfSinConfig();
    public final CustomAntiEnchantmentEntityConfig customAntiEnchantmentEntityConfig = new CustomAntiEnchantmentEntityConfig();
    public final CustomAntiSeatEntityConfig customAntiSeatEntityConfig = new CustomAntiSeatEntityConfig();
    public final CustomAntiItemEntityConfig customAntiItemEntityConfig = new CustomAntiItemEntityConfig();
    public final CustomEntityAntiEffectConfig customEntityAntiEffectConfig = new CustomEntityAntiEffectConfig();
    public final CustomAntiHighATKEntityConfig customAntiHighATKEntityConfig = new CustomAntiHighATKEntityConfig();
    public final CustomAntiTrapCageEntityConfig customAntiTrapCageEntityConfig = new CustomAntiTrapCageEntityConfig();
    public final CustomEntityEffectConfigManager customEntityEffectConfigManager = new CustomEntityEffectConfigManager();
    public final CustomEntityItemConfigManager customEntityItemConfigManager = new CustomEntityItemConfigManager();
    public final CustomEntityAttackEffectConfig customEntityAttackEffectConfig = new CustomEntityAttackEffectConfig();
    public final AntiHighLevelEnchantmentConfig antiHighLevelEnchantmentConfig = new AntiHighLevelEnchantmentConfig();
    public final EternalEntityConfig eternalEntityConfig = new EternalEntityConfig();
    public final SoulLikeBossBattleConfig soulLikeBossBattleConfig = new SoulLikeBossBattleConfig();
    public final BetterAIConfig betterAIConfig = new BetterAIConfig();
    public final AntiSameModifierConfig antiSameModifierConfig = new AntiSameModifierConfig();
    public final AdventureDimensionConfig adventureDimensionConfig = new AdventureDimensionConfig();
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, AshOfSin.MODID);
    public static final RegistryObject<MobEffect> WRATH_OF_GOD = MOB_EFFECTS.register("wrath_of_god", WrathOfGod::new);
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, AshOfSin.MODID);
    public static final RegistryObject<Biome> ABSOLUTE_SPACE_TIME_REALM = BIOMES.register("absolute_space_time_realm", AshOfSinBiomes::createBiome);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);
    public static final RegistryObject<Enchantment> ABSOLUTE_RULE = ENCHANTMENTS.register("absolute_rule", () ->
            new AbsoluteRuleEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON));
    public static final RegistryObject<Enchantment> ANOTHER = ENCHANTMENTS.register("another", () ->
            new AnotherEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST));
    public static final RegistryObject<Enchantment> CHALK_WALL = ENCHANTMENTS.register("chalk_wall", () ->
            new ChalkWallEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR));
    public static final RegistryObject<Enchantment> VENUZDONOA = ENCHANTMENTS.register("venuzdonoa", () ->
            new VenuzdonoaEnchantent(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON));

    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public AshOfSin() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PROXY.init();
        bus.addListener(this::setup);
        bus.addListener(this::doClientStuff);

        AshOfSinItems.ITEMS.register(bus);
        AshOfSinEntities.ENTITY_TYPES.register(bus);
        AbsoluteSpaceTimeRealmDimension.register();
        MOB_EFFECTS.register(bus);
        BIOMES.register(bus);
        ENCHANTMENTS.register(bus);
        AshOfSinSounds.SOUNDS.register(bus);
        MinecraftForge.EVENT_BUS.register(new AshOfSinRegisterCommandsEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinRealmGateOpenEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinSounds());
        MinecraftForge.EVENT_BUS.register(new AshOfSinAbsoluteSpaceTimeRealmEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinSpawnControlEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinSculkEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinBindingEvent());

        MinecraftForge.EVENT_BUS.register(new AshOfSinAnotherEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinChalkWallEvent());

        MinecraftForge.EVENT_BUS.register(new AshOfSinCarianEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCrescentEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinMirrorOfTheDarkNightEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinSubCravenBowEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinShikamaDojiEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinFlameKatanaCaravellaEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinDualBladesEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinDespairScytheEvent());

        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomAntiEnchantmentEntityEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomAntiSeatEntityEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomAntiItemEntityEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomEntityAntiEffectEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomEntityEffectEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomEntityItemEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomEntityAttackEffectEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinAntiHighLevelEnchantmentEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinEternalEntityEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinSoulLikeBossBattleEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomAntiHighATKEntityEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinAntiSameModifierEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinBetterAIEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinCustomAntiTrapCageEntityEvent());
        MinecraftForge.EVENT_BUS.register(new AshOfSinAdventureDimensionEvent());

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent event) {
        ashOfSinConfig.loadConfig();
        customAntiEnchantmentEntityConfig.loadConfig();
        customAntiSeatEntityConfig.loadConfig();
        customAntiItemEntityConfig.loadConfig();
        customEntityAntiEffectConfig.loadConfig();
        customAntiHighATKEntityConfig.loadConfig();
        customAntiTrapCageEntityConfig.loadConfig();
        customEntityEffectConfigManager.loadConfig();
        customEntityItemConfigManager.loadConfig();
        customEntityAttackEffectConfig.loadConfig();
        antiHighLevelEnchantmentConfig.loadConfig();
        eternalEntityConfig.loadConfig();
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
}