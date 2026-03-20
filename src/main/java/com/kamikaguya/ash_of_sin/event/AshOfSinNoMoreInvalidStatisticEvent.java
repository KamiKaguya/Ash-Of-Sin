package com.kamikaguya.ash_of_sin.event;

import com.google.gson.*;
import com.kamikaguya.ash_of_sin.main.AshOfSin;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = AshOfSin.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AshOfSinNoMoreInvalidStatisticEvent {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // 服务器启动时清理所有统计文件
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        Path statsDir = server.getWorldPath(LevelResource.PLAYER_STATS_DIR);
        if (!Files.isDirectory(statsDir)) {
            LOGGER.info("Stats directory not found, skipping.");
            return;
        }

        LOGGER.info("Scanning stats directory for invalid entries: {}", statsDir);
        try (Stream<Path> paths = Files.list(statsDir)) {
            paths.filter(path -> path.toString().endsWith(".json"))
                    .forEach(AshOfSinNoMoreInvalidStatisticEvent::cleanStatFile);
        } catch (IOException e) {
            LOGGER.error("Failed to list stats directory", e);
        }
    }

    // 玩家登录时再次清理该玩家的文件（防止启动后首次登录的玩家文件在启动时未被清理）
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        UUID uuid = event.getEntity().getUUID();
        MinecraftServer server = event.getEntity().getServer();
        if (server == null) return;

        Path statsDir = server.getWorldPath(LevelResource.PLAYER_STATS_DIR);
        Path playerStatFile = statsDir.resolve(uuid + ".json");
        if (Files.exists(playerStatFile)) {
            cleanStatFile(playerStatFile);
        }
    }

    private enum RegistryType { ITEM, BLOCK, ENTITY, CUSTOM, UNKNOWN }

    private static RegistryType getRegistryType(String categoryKey) {
        int lastColon = categoryKey.lastIndexOf(':');
        if (lastColon == -1 || lastColon == categoryKey.length() - 1) {
            LOGGER.debug("Invalid category key format: {}", categoryKey);
            return RegistryType.UNKNOWN;
        }
        String categoryName = categoryKey.substring(lastColon + 1);

        RegistryType type = switch (categoryName) {
            case "used", "broken", "crafted", "picked_up", "dropped" -> RegistryType.ITEM;
            case "mined" -> RegistryType.BLOCK;
            case "killed", "killed_by" -> RegistryType.ENTITY;
            case "custom" -> RegistryType.CUSTOM;
            default -> RegistryType.UNKNOWN;
        };
        LOGGER.debug("Category key: {} -> extracted name: {} -> type: {}", categoryKey, categoryName, type);
        return type;
    }

    private static void cleanStatFile(Path filePath) {
        try (Reader reader = Files.newBufferedReader(filePath)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            JsonObject statsObject;
            if (root.has("stats") && root.get("stats").isJsonObject()) {
                statsObject = root.getAsJsonObject("stats");
            } else {
                statsObject = root;
            }

            boolean modified = false;

            for (Map.Entry<String, JsonElement> categoryEntry : statsObject.entrySet()) {
                String categoryKey = categoryEntry.getKey();
                JsonElement categoryValue = categoryEntry.getValue();
                if (!categoryValue.isJsonObject()) continue;

                JsonObject categoryObj = categoryValue.getAsJsonObject();
                RegistryType registryType = getRegistryType(categoryKey);
                if (registryType == RegistryType.UNKNOWN) {
                    LOGGER.debug("Skipping unknown category: {}", categoryKey);
                    continue;
                }

                List<String> keysToRemove = new ArrayList<>();
                for (Map.Entry<String, JsonElement> statEntry : categoryObj.entrySet()) {
                    String statId = statEntry.getKey();
                    if (!isValidStatId(statId, registryType)) {
                        keysToRemove.add(statId);
                    }
                }

                if (!keysToRemove.isEmpty()) {
                    LOGGER.debug("Category {}: found {} invalid entries", categoryKey, keysToRemove.size());
                    for (String key : keysToRemove) {
                        categoryObj.remove(key);
                        LOGGER.debug("Removed invalid stat: {} from category {}", key, categoryKey);
                    }
                    modified = true;
                }

                if (categoryObj.size() == 0) {
                    statsObject.remove(categoryKey);
                    LOGGER.debug("Removed empty category: {}", categoryKey);
                    modified = true;
                }
            }

            if (modified) {
                try (Writer writer = Files.newBufferedWriter(filePath)) {
                    GSON.toJson(root, writer);
                    LOGGER.info("Cleaned stats file: {} (removed entries)", filePath.getFileName());
                } catch (IOException e) {
                    LOGGER.error("Failed to write cleaned stats file: {}", filePath, e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to process stats file: {}", filePath, e);
        }
    }

    private static boolean isValidStatId(String id, RegistryType type) {
        ResourceLocation loc = ResourceLocation.tryParse(id);
        if (loc == null) return false;
        return switch (type) {
            case ITEM -> ForgeRegistries.ITEMS.containsKey(loc);
            case BLOCK -> ForgeRegistries.BLOCKS.containsKey(loc);
            case ENTITY -> ForgeRegistries.ENTITY_TYPES.containsKey(loc);
            case CUSTOM -> Registry.CUSTOM_STAT.containsKey(loc);
            default -> false;
        };
    }
}