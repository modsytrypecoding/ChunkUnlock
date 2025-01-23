package org.Modstrype.twitchChunkPlugin.Chunk;



import org.Modstrype.twitchChunkPlugin.Main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import java.util.HashSet;
import java.util.Optional;
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

    public void updateChunks(Player player) {

        Main.ignoringWorldChange.add(player.getUniqueId());

        // 2) Teleport-Logik
        Location oldLocation = player.getLocation();
        Optional<World> randomWorld = Bukkit.getWorlds().stream()
                .filter(world -> !world.equals(player.getWorld()))
                .findFirst();


        randomWorld.ifPresent(world -> player.teleport(world.getSpawnLocation()));

        player.teleport(oldLocation);


        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Main.ignoringWorldChange.remove(player.getUniqueId());
        }, 1L);
    }

    public void resetUnlockedChunks() {
        unlockedChunks.clear();
        saveUnlockedChunksToConfig();
        plugin.saveConfig();
    }

    public Set<String> getUnlockedChunks() {
        return new HashSet<>(unlockedChunks);
    }



}
