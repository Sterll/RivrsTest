package fr.yanis.rivrs.utils;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class HologramUtil {

    private static Map<UUID, Integer> entityIdMap = new HashMap<>();
    private static int nextEntityId = -1000;

    public static int generateEntityId() {
        return nextEntityId--;
    }

    public static void spawnTextDisplay(Player target, Location location, String text) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        if (protocolManager == null) {
            System.err.println("ProtocolManager n'a pas pu être récupéré ! ProtocolLib est-il chargé ?");
            return;
        }

        int entityId = generateEntityId();
        UUID entityUUID = UUID.randomUUID();

        removeTextDisplay(target);

        PacketContainer spawnPacket = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);

        spawnPacket.getIntegers().write(0, entityId);
        spawnPacket.getUUIDs().write(0, entityUUID);
        spawnPacket.getEntityTypeModifier().write(0, EntityType.TEXT_DISPLAY);
        spawnPacket.getDoubles().write(0, location.getX());
        spawnPacket.getDoubles().write(1, location.getY());
        spawnPacket.getDoubles().write(2, location.getZ());
        spawnPacket.getBytes().write(0, (byte) (location.getPitch() * 256.0F / 360.0F));
        spawnPacket.getBytes().write(1, (byte) (location.getYaw() * 256.0F / 360.0F));
        spawnPacket.getBytes().write(2, (byte) 0);
        spawnPacket.getIntegers().write(1, 0);


        PacketContainer metaPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metaPacket.getIntegers().write(0, entityId);

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedChatComponent chatComponent = WrappedChatComponent.fromLegacyText(text);
        Optional<Object> optChatComponent = Optional.of(chatComponent.getHandle());
        Serializer chatSerializer = Registry.getChatComponentSerializer(true);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(23, chatSerializer), optChatComponent);

        metaPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        entityIdMap.put(target.getUniqueId(), entityId);

        try {
            protocolManager.sendServerPacket(target, spawnPacket);
            protocolManager.sendServerPacket(target, metaPacket);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi des paquets d'hologramme pour " + target.getName() + " (ID: " + entityId + "):");
            e.printStackTrace();
            entityIdMap.remove(target.getUniqueId());
        }
    }

    public static void updateTextDisplay(Player target, String newText) {
        Integer entityId = entityIdMap.get(target.getUniqueId());

        if (entityId == null) {
            return;
        }

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        if (protocolManager == null) {
            System.err.println("ProtocolManager n'a pas pu être récupéré lors de la mise à jour ! ProtocolLib est-il chargé ?");
            return;
        }

        PacketContainer metaPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metaPacket.getIntegers().write(0, entityId);

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        WrappedChatComponent chatComponent = WrappedChatComponent.fromLegacyText(newText);
        Optional<Object> optChatComponent = Optional.of(chatComponent.getHandle());
        Serializer chatSerializer = Registry.getChatComponentSerializer(true);
        watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(23, chatSerializer), optChatComponent);

        metaPacket.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

        try {
            protocolManager.sendServerPacket(target, metaPacket);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du paquet de mise à jour d'hologramme pour " + target.getName() + " (ID: " + entityId + "):");
            e.printStackTrace();
        }
    }

    public static void removeTextDisplay(Player target) {
        Integer entityId = entityIdMap.remove(target.getUniqueId());

        if (entityId != null) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            if (protocolManager == null) {
                System.err.println("ProtocolManager n'a pas pu être récupéré lors de la suppression ! ProtocolLib est-il chargé ?");
                return;
            }

            PacketContainer destroyPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            destroyPacket.getIntLists().write(0, Collections.singletonList(entityId));

            try {
                protocolManager.sendServerPacket(target, destroyPacket);
            } catch (Exception e) {
                System.err.println("Erreur lors de l'envoi du paquet de destruction d'hologramme pour " + target.getName() + " (ID: " + entityId + "):");
                e.printStackTrace();
            }
        }
    }
}