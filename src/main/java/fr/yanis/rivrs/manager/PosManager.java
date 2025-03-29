package fr.yanis.rivrs.manager;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.UUID;

public class PosManager {

    private static HashMap<UUID, Pair<String, String>> datas = new HashMap<>();

    public static void set(UUID uuid, String x, String y) {
        datas.put(uuid, Pair.of(x, y));
    }

    public static Pair<String, String> get(UUID uuid) {
        return datas.get(uuid);
    }

    public static void remove(UUID uuid) {
        datas.remove(uuid);
    }

    public static boolean contains(UUID uuid) {
        return datas.containsKey(uuid);
    }

    public static String getFirst(UUID uuid) {
        return datas.get(uuid).getLeft();
    }

    public static String getSecond(UUID uuid) {
        return datas.get(uuid).getRight();
    }

    public static void setFirst(UUID uuid, String x) {
        datas.computeIfPresent(uuid, (k, pair) -> Pair.of(x, pair.getRight()));
    }

    public static void setSecond(UUID uuid, String y) {
        datas.computeIfPresent(uuid, (k, pair) -> Pair.of(pair.getLeft(), y));
    }

}
