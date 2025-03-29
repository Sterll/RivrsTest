package fr.yanis.rivrs.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.HashMap;
import java.util.UUID;

public class HologramUtil {

    private static HashMap<UUID, Integer> entityIdMap = new HashMap<>();

    private static int nextEntityId = 1000;

    public static int generateEntityId() {
        return nextEntityId++;
    }

    public static void spawnTextDisplay(Player target, Location location, String text) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        int entityId = generateEntityId();

        PacketContainer spawnPacket = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawnPacket.getIntegers().write(0, entityId);
        spawnPacket.getUUIDs().write(0, UUID.randomUUID());
        spawnPacket.getDoubles().write(0, location.getX());
        spawnPacket.getDoubles().write(1, location.getY());
        spawnPacket.getDoubles().write(2, location.getZ());
        spawnPacket.getMinecraftKeys().write(1, new MinecraftKey("minecraft:text_display"));

        try {
            protocolManager.sendServerPacket(target, spawnPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PacketContainer metaPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metaPacket.getIntegers().write(0, entityId);

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedChatComponent chatComponent = WrappedChatComponent.fromJson("{\"text\":\"" + text + "\"}");
        watcher.setObject(10, chatComponent);

        metaPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        entityIdMap.put(target.getUniqueId(), entityId);

        try {
            protocolManager.sendServerPacket(target, metaPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeTextDisplay(Player target) {
        int entityId = entityIdMap.getOrDefault(target.getUniqueId(), -1);
        if (entityId != -1) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            PacketContainer destroyPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            destroyPacket.getIntegerArrays().write(0, new int[]{entityId});

            try {
                protocolManager.sendServerPacket(target, destroyPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }

            entityIdMap.remove(target.getUniqueId());
        }
    }
}
