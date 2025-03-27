package fr.yanis.rivrs.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cuboid {

    private final String worldName;
    @Getter
    private final int xMin, xMax;
    @Getter
    private final int yMin, yMax;
    @Getter
    private final int zMin, zMax;
    @Getter
    private final int rayon;

    public Cuboid(Location point1, Location point2) {
        if (!point1.getWorld().equals(point2.getWorld())) {
            throw new IllegalArgumentException("Les deux points doivent être dans le même monde.");
        }
        this.worldName = point1.getWorld().getName();
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());

        this.rayon = Math.max(getSizeX(), Math.max(getSizeY(), getSizeZ())) / 2;
    }

    public Cuboid(@NotNull Location center, int rayon) {
        this.worldName = center.getWorld().getName();
        this.rayon = rayon;

        this.xMin = center.getBlockX() - rayon;
        this.xMax = center.getBlockX() + rayon;
        this.yMin = center.getBlockY() - rayon;
        this.yMax = center.getBlockY() + rayon;
        this.zMin = center.getBlockZ() - rayon;
        this.zMax = center.getBlockZ() + rayon;
    }

    public @NotNull World getWorld() {
        return Objects.requireNonNull(Bukkit.getWorld(this.worldName), "World " + this.worldName + " is not loaded");
    }

    public int getSizeX() {
        return this.xMax - this.xMin + 1;
    }

    public int getSizeY() {
        return this.yMax - this.yMin + 1;
    }

    public int getSizeZ() {
        return this.zMax - this.zMin + 1;
    }

    public long getVolume() {
        return (long) this.getSizeX() * this.getSizeY() * this.getSizeZ();
    }

    public boolean contains(@NotNull Location location) {
        if (!this.worldName.equals(location.getWorld().getName()))
            return false;

        final int x = location.getBlockX();
        final int y = location.getBlockY();
        final int z = location.getBlockZ();
        return x >= this.xMin && x <= this.xMax &&
                y >= this.yMin && y <= this.yMax &&
                z >= this.zMin && z <= this.zMax;
    }

    public @NotNull List<Block> getFloorBlocks() {
        List<Block> blocks = new ArrayList<>();

        for (int x = this.xMin; x <= this.xMax; x++) {
            for (int z = this.zMin; z <= this.zMax; z++) {
                blocks.add(this.getWorld().getBlockAt(x, this.yMin, z));
            }
        }

        return blocks;
    }

    public @NotNull List<Block> getWalls(){
        List<Block> blocks = new ArrayList<>();

        for (int x = this.xMin; x <= this.xMax; x++) {
            for (int y = this.yMin; y <= this.yMax; y++) {
                blocks.add(this.getWorld().getBlockAt(x, y, this.zMin));
                blocks.add(this.getWorld().getBlockAt(x, y, this.zMax));
            }
        }

        for (int z = this.zMin; z <= this.zMax; z++) {
            for (int y = this.yMin; y <= this.yMax; y++) {
                blocks.add(this.getWorld().getBlockAt(this.xMin, y, z));
                blocks.add(this.getWorld().getBlockAt(this.xMax, y, z));
            }
        }

        return blocks;
    }

    public @NotNull List<Block> getAllBlocks(){
        List<Block> blocks = new ArrayList<>();

        for (int x = this.xMin; x <= this.xMax; x++) {
            for (int y = this.yMin; y <= this.yMax; y++) {
                for (int z = this.zMin; z <= this.zMax; z++) {
                    blocks.add(this.getWorld().getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    public @NotNull Location getCenter() {
        double centerX = (this.xMin + this.xMax) / 2.0 + 0.5;
        double centerY = (this.yMin + this.yMax) / 2.0 + 0.5;
        double centerZ = (this.zMin + this.zMax) / 2.0 + 0.5;

        return new Location(this.getWorld(), centerX, centerY, centerZ);
    }

    public static void deserialize(String data){
        String[] parts = data.split(";");
        String worldName = parts[0];
        int xMin = Integer.parseInt(parts[1]);
        int xMax = Integer.parseInt(parts[2]);
        int yMin = Integer.parseInt(parts[3]);
        int yMax = Integer.parseInt(parts[4]);
        int zMin = Integer.parseInt(parts[5]);
        int zMax = Integer.parseInt(parts[6]);

        new Cuboid(new Location(Bukkit.getWorld(worldName), xMin, yMin, zMin), new Location(Bukkit.getWorld(worldName), xMax, yMax, zMax));
    }

    public String serialize(){
        return this.worldName + ";" + this.xMin + ";" + this.xMax + ";" + this.yMin + ";" + this.yMax + ";" + this.zMin + ";" + this.zMax;
    }

    public static void toConfig(String path, Cuboid cuboid){
        String data = cuboid.serialize();

    }
}
