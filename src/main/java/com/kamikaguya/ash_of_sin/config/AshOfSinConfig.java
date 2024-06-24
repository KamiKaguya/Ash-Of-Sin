package com.kamikaguya.ash_of_sin.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AshOfSinConfig {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> EFFECT_LIST;
    public static ForgeConfigSpec.ConfigValue<String> ENTITY_SUMMON_ID;
    public static ForgeConfigSpec.ConfigValue<String> ENTITY_SUMMON_NBT;
    public static ForgeConfigSpec.ConfigValue<String> KAMIKAGUYA_MAIN_HAND_ITEM;
    public static ForgeConfigSpec.ConfigValue<String> KAMIKAGUYA_OFF_HAND_ITEM;
    public static ForgeConfigSpec.ConfigValue<String> KAMIKAGUYA_MAIN_HAND_ITEM_NBT;
    public static ForgeConfigSpec.ConfigValue<String> KAMIKAGUYA_OFF_HAND_ITEM_NBT;
    public static ForgeConfigSpec.IntValue KAMIKAGUYA_MAIN_HAND_DROP_CHANCE;
    public static ForgeConfigSpec.IntValue KAMIKAGUYA_OFF_HAND_DROP_CHANCE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_DEAD;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITY_ENTER_REALM;
    public static ForgeConfigSpec.ConfigValue<String> REALM_GATE_LOCATION;
    public static ForgeConfigSpec.ConfigValue<String> SUNDERING_EFFECT_STRING;
    public static List<? extends String> getAllowedEntity() {
        return ENTITY_ENTER_REALM.get();
    }
    public static ForgeConfigSpec.ConfigValue<String> getConfigPos() {
        return REALM_GATE_LOCATION;
    }
    public static ForgeConfigSpec.IntValue CHUNK_LOAD_RADIUS;
    public static ForgeConfigSpec.ConfigValue<String> Assassin_MAIN_HAND_ITEM;
    public static ForgeConfigSpec.ConfigValue<String> Assassin_OFF_HAND_ITEM;
    public static ForgeConfigSpec.ConfigValue<String> Assassin_MAIN_HAND_ITEM_NBT;
    public static ForgeConfigSpec.ConfigValue<String> Assassin_OFF_HAND_ITEM_NBT;
    public static ForgeConfigSpec.ConfigValue<String> Doppelganger_MAIN_HAND_ITEM;
    public static ForgeConfigSpec.ConfigValue<String> Doppelganger_OFF_HAND_ITEM;
    public static ForgeConfigSpec.ConfigValue<String> Doppelganger_MAIN_HAND_ITEM_NBT;
    public static ForgeConfigSpec.ConfigValue<String> Doppelganger_OFF_HAND_ITEM_NBT;
    public final Path configPath;

    public static BlockPos getGateLocation() {
        String locationString = getConfigPos().get();

        String[] coords = locationString.split(",");
        if (coords.length == 3) {
            try {
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                int z = Integer.parseInt(coords[2]);

                return new BlockPos(x, y, z);
            } catch (NumberFormatException e) {
                System.err.println("Gate location string '" + locationString + "' is invalid. Using BlockPos.ZERO instead.");
            }
        } else {
            System.err.println("Gate location string must contain exactly 3 integer values separated by commas.");
        }

        return BlockPos.ZERO;
    }

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        // KamiKaguya Attack Effects Settings
        builder.comment("KamiKaguya Attack Effects Settings").push("KamiKaguyaAttackEffects");
        // Default Attack Effects
        List<String> defaultHurtEffects = List.of(
                "apotheosis:sundering," + (13 * 20) + ",38"
        );
        // More Attack Effects
        EFFECT_LIST = builder.comment("'EffectID,Duration,Amplifier'")
                .defineList("KamiKaguyaEffects",
                        defaultHurtEffects,
                        o -> (o instanceof String) && ((String)o).split(",").length == 3 && Integer.parseInt(((String)o).split(",")[1]) > 0 && Integer.parseInt(((String)o).split(",")[2]) >= 0);
        builder.pop();

        // KamiKaguya Summon Entity Settings
        builder.comment("KamiKaguya Summon Entity Settings").push("KamiKaguyaSummon");
        ENTITY_SUMMON_ID = builder.comment("entitySummonId").define("entitySummonId", "cataclysm:ignis");
        ENTITY_SUMMON_NBT = builder.comment("entitySummonNBT").define("entitySummonNBT", "{}");
        builder.pop();

        // KamiKaguya Hand Items Settings
        builder.comment("KamiKaguya Hand Items Settings").push("KamiKaguyaHandItems");
        KAMIKAGUYA_MAIN_HAND_ITEM = builder.comment("mainHandItem").define("mainHandItem", "ash_of_sin:dark_moon_greatsword");
        KAMIKAGUYA_OFF_HAND_ITEM = builder.comment("offHandItem").define("offHandItem", "ash_of_sin:mirror_of_the_dark_night");
        KAMIKAGUYA_MAIN_HAND_ITEM_NBT = builder.comment("mainHandItemNBT").define("mainHandItemNBT", "{affix_data:{affixes:{\"apotheosis:elusive\":1.0f,\"apotheosis:festive\":1.0f,\"apotheosis:intricate\":1.0f,\"apotheosis:lacerating\":1.0f,\"apotheosis:socket\":5.0f,\"apotheosis:sophisticated\":1.0f,\"apotheosis:telepathic\":1.0f,\"apotheosis:vampiric\":1.0f,\"apotheosis:violent\":1.0f,\"apotheosis:weakening\":1.0f},rarity:\"ancient\"},\"quark:RuneAttached\":1b,\"quark:RuneColor\":{Count:1b,id:\"quark:blank_rune\"}}");
        KAMIKAGUYA_OFF_HAND_ITEM_NBT = builder.comment("offHandItemNBT").define("offHandItemNBT", "");
        KAMIKAGUYA_MAIN_HAND_DROP_CHANCE = builder.comment("mainHandDropChance(%)").defineInRange("mainHandDropChance", 0, 0, 100);
        KAMIKAGUYA_OFF_HAND_DROP_CHANCE = builder.comment("offHandDropChance(%)").defineInRange("offHandDropChance", 0, 0, 100);
        builder.pop();

        // Realm Gate Open By Entity Dead
        builder.comment("Entities List Settings").push("entities_dead");
        ENTITY_DEAD = builder.comment("EntitiesList")
                .defineList("EntitiesList",
                        () -> List.of("witherstormmod:wither_storm"),
                        o -> o instanceof String);
        builder.pop();

        // Absolute Space Time Realm
        builder.comment("Entities Enter Realm Settings").push("enter_entities");
        ENTITY_ENTER_REALM = builder.comment("EntitiesEnterRealm")
                .defineList("EntitiesEnterRealm",
                        () -> List.of("ash_of_sin:kamikaguya"),
                        o -> o instanceof String);
        builder.pop();

        // Realm Gate Location
        builder.comment("Realm Gate Location Settings").push("location");
        REALM_GATE_LOCATION = builder.comment("Location").define("location", "0,321,0");
        builder.pop();

        // Chunk Load Radius Settings
        builder.comment("chunk pre-loading Radius Settings").push("chunk_radius_loading");
        CHUNK_LOAD_RADIUS = builder
                .comment("Radius of chunks to load. Minimum is 1, maximum is 256.")
                .defineInRange("Radius", 8, 1, 256);
        builder.pop();

        // Sundering Effect Settings
        builder.comment("Sundering Effect Settings").push("SunderingEffect");
        SUNDERING_EFFECT_STRING = builder.comment("Format: 'EffectID,DurationInSeconds,EffectLevel'")
                .define("SunderingEffectString", "apotheosis:sundering,0,-1");
        builder.pop();

        // Assassin Hand Items Settings
        builder.comment("Assassin Hand Items Settings").push("AssassinHandItems");
        Assassin_MAIN_HAND_ITEM = builder.comment("Main hand item for Assassin").define("mainHandItem", "wom:ender_blaster");
        Assassin_OFF_HAND_ITEM = builder.comment("Off hand item for Assassin").define("offHandItem", "wom:ender_blaster");
        Assassin_MAIN_HAND_ITEM_NBT = builder.comment("Main hand item NBT for Assassin").define("mainHandItemNBT", "{Enchantments:[{id:\"difficultraids:critical_burst\",lvl:5},{id:\"difficultraids:critical_strike\",lvl:10},{id:\"modification_of_critical_hit:critchance\",lvl:5},{id:\"modification_of_critical_hit:criteffect\",lvl:3},{id:\"minecraft:sweeping\",lvl:1},{id:\"lightmanscurrency:money_mending\",lvl:1}],affix_data:{affixes:{\"apotheosis:festive\":1.0f,\"apotheosis:graceful\":1.0f,\"apotheosis:intricate\":1.0f,\"apotheosis:lacerating\":1.0f,\"apotheosis:socket\":5.0f,\"apotheosis:thunderstruck\":1.0f,\"apotheosis:violent\":1.0f,\"apotheosis:weakeninged\":1.0f},rarity:\"mythic\"},\"quark:RuneAttached\":1b,\"quark:RuneColor\":{Count:1b,id:\"quark:purple_rune\"}}");
        Assassin_OFF_HAND_ITEM_NBT = builder.comment("Off hand item NBT for Assassin").define("offHandItemNBT", "{Enchantments:[{id:\"difficultraids:critical_burst\",lvl:5},{id:\"difficultraids:critical_strike\",lvl:10},{id:\"modification_of_critical_hit:critchance\",lvl:5},{id:\"modification_of_critical_hit:criteffect\",lvl:3},{id:\"minecraft:sweeping\",lvl:1},{id:\"lightmanscurrency:money_mending\",lvl:1}],affix_data:{affixes:{\"apotheosis:festive\":1.0f,\"apotheosis:graceful\":1.0f,\"apotheosis:intricate\":1.0f,\"apotheosis:lacerating\":1.0f,\"apotheosis:socket\":5.0f,\"apotheosis:thunderstruck\":1.0f,\"apotheosis:violent\":1.0f,\"apotheosis:weakeninged\":1.0f},rarity:\"mythic\"},\"quark:RuneAttached\":1b,\"quark:RuneColor\":{Count:1b,id:\"quark:purple_rune\"}}");
        builder.pop();

        // Doppelganger Hand Items Settings
        builder.comment("Doppelganger Hand Items Settings").push("DoppelgangerHandItems");
        Doppelganger_MAIN_HAND_ITEM = builder.comment("Main hand item for Doppelganger").define("mainHandItem", "wom:herrscher");
        Doppelganger_OFF_HAND_ITEM = builder.comment("Off hand item for Doppelganger").define("offHandItem", "wom:gesetz");
        Doppelganger_MAIN_HAND_ITEM_NBT = builder.comment("Main hand item NBT for Doppelganger").define("mainHandItemNBT", "{}");
        Doppelganger_OFF_HAND_ITEM_NBT = builder.comment("Off hand item NBT for Doppelganger").define("offHandItemNBT", "{}");
        builder.pop();

        COMMON_CONFIG = builder.build();
    }

    public AshOfSinConfig() {
        this.configPath = FMLPaths.CONFIGDIR.get().resolve("ash_of_sin/common.toml");
        loadConfig();
    }

    public void loadConfig() {
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default common config.", e);
            }
        }
        final CommentedFileConfig fileConfig = CommentedFileConfig.builder(configPath).sync().autosave().writingMode(WritingMode.REPLACE).build();
        fileConfig.load();
        COMMON_CONFIG.setConfig(fileConfig);
    }
}