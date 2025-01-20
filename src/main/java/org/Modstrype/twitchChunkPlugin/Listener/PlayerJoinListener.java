package org.Modstrype.twitchChunkPlugin.Listener;


import org.Modstrype.twitchChunkPlugin.Chunk.ChunkManager;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ChunkManager chunkManager;

    public PlayerJoinListener(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Chunk spawnChunk = event.getPlayer().getLocation().getChunk();
        World world = spawnChunk.getWorld();

        int x = spawnChunk.getX();
        int z = spawnChunk.getZ();


        if (!chunkManager.isChunkUnlocked(world, x, z)) {
            chunkManager.unlockChunk(world, x, z);
            chunkManager.getOrLoadChunk(world, x, z);
        }
    }
}