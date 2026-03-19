package com.kamikaguya.ash_of_sin.event;

import com.google.gson.*;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinNoMoreInvalidStatisticEvent {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        Path statsDir = server.getWorldPath(LevelResource.PLAYER_STATS_DIR);

        if (!Files.isDirectory(statsDir)) {
            return;
        }

        try (Stream<Path> paths = Files.list(statsDir)) {
            paths.filter(path -> path.toString().endsWith(".json"))
                    .forEach(AshOfSinNoMoreInvalidStatisticEvent::cleanStatFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cleanStatFile(Path filePath) {
        try (Reader reader = Files.newBufferedReader(filePath)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            boolean modified = false;

            for (Map.Entry<String, JsonElement> categoryEntry : root.entrySet()) {
                String categoryKey = categoryEntry.getKey(); // 例如 "stats.minecraft:used"
                JsonElement categoryValue = categoryEntry.getValue();
                if (!categoryValue.isJsonObject()) continue;

                JsonObject categoryObj = categoryValue.getAsJsonObject();
                RegistryType registryType = getRegistryType(categoryKey);
                if (registryType == RegistryType.UNKNOWN) continue;

                for (Map.Entry<String, JsonElement> statEntry : categoryObj.entrySet()) {
                    String statId = statEntry.getKey();
                    if (!isValidStatId(statId, registryType)) {
                        categoryObj.remove(statId);
                        modified = true;
                    }
                }

                if (categoryObj.size() == 0) {
                    root.remove(categoryKey);
                    modified = true;
                }
            }

            if (modified) {
                try (Writer writer = Files.newBufferedWriter(filePath)) {
                    GSON.toJson(root, writer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private enum RegistryType {
        ITEM, BLOCK, ENTITY, CUSTOM, UNKNOWN
    }

    private static RegistryType getRegistryType(String categoryKey) {
        String[] parts = categoryKey.split(":");
        if (parts.length != 2) return RegistryType.UNKNOWN;
        String categoryName = parts[1];

        return switch (categoryName) {
            case "used", "broken", "crafted", "picked_up", "dropped" -> RegistryType.ITEM;
            case "mined" -> RegistryType.BLOCK;
            case "killed", "killed_by" -> RegistryType.ENTITY;
            case "custom" -> RegistryType.CUSTOM;
            default -> RegistryType.UNKNOWN; // 可扩展其他模组的统计类型
        };
    }

    private static boolean isValidStatId(String id, RegistryType type) {
        ResourceLocation loc = ResourceLocation.tryParse(id);
        if (loc == null) return false;

        return switch (type) {
            case ITEM -> ForgeRegistries.ITEMS.containsKey(loc);
            case BLOCK -> ForgeRegistries.BLOCKS.containsKey(loc);
            case ENTITY -> ForgeRegistries.ENTITY_TYPES.containsKey(loc);
            case CUSTOM -> BuiltInRegistries.CUSTOM_STAT.containsKey(loc);
            default -> false;
        };
    }
}