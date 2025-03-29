package fr.yanis.rivrs.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;

import java.util.UUID;

public class HologramUtil {

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
        spawnPacket.getIntegers().write(1, 106);

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

        try {
            protocolManager.sendServerPacket(target, metaPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
