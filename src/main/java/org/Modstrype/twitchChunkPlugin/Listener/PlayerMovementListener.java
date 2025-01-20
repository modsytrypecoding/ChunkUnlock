package org.Modstrype.twitchChunkPlugin.Listener;


import org.Modstrype.twitchChunkPlugin.Chunk.ChunkManager;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovementListener implements Listener {

    private final ChunkManager chunkManager;

    public PlayerMovementListener(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null) return;

        Chunk toChunk = event.getTo().getChunk();
        if(!chunkManager.isChunkUnlocked(toChunk.getWorld(), toChunk.getX(), toChunk.getZ())) {
            event.setCancelled(true);
        }
    }
}