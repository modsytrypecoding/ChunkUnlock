package org.Modstrype.twitchChunkPlugin.Chunk;


import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.Modstrype.twitchChunkPlugin.Main.Main;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_21_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;


import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class ChunkManager {

    private final Main plugin;

    private final Set<String> unlockedChunks = new HashSet<>();

    public ChunkManager(Main plugin) {
        this.plugin = plugin;
    }

    public void unlockChunk(World world, int x, int z) {
        String key = world.getName() + ";" + x + ";" + z;
        unlockedChunks.add(key);
    }


    public boolean isChunkUnlocked(World world, int x, int z) {
        String key = world.getName() + ";" + x + ";" + z;
        return unlockedChunks.contains(key);
    }

    public void loadUnlockedChunksFromConfig() {
        FileConfiguration config = plugin.getConfig();
        if(config.contains("unlockedChunks")) {
            for(String chunkKey : config.getStringList("unlockedChunks")) {
                unlockedChunks.add(chunkKey);
            }
        }
        plugin.getLogger().info("[ChunkUnlocker] " + unlockedChunks.size() + " Chunks geladen.");
    }

    public void saveUnlockedChunksToConfig() {
        FileConfiguration config = plugin.getConfig();
        config.set("unlockedChunks", unlockedChunks.stream().toList());
        plugin.saveConfig();
    }


    public Chunk getOrLoadChunk(World world, int chunkX, int chunkZ) {
        if (!world.isChunkLoaded(chunkX, chunkZ)) {
            world.loadChunk(chunkX, chunkZ, true);
        }
        return world.getChunkAt(chunkX, chunkZ);
    }


}
