package org.Modstrype.twitchChunkPlugin.Listener;


import org.Modstrype.twitchChunkPlugin.Chunk.ChunkManager;
import org.Modstrype.twitchChunkPlugin.Main.Main;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
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


        @EventHandler
        public void onWorldChang(PlayerChangedWorldEvent event) {
            Player player = event.getPlayer();

            Chunk chunk = player.getLocation().getChunk();
            int newChunkX = chunk.getX();
            int newChunkZ = chunk.getZ();

            if (Main.ignoringWorldChange.contains(player.getUniqueId())) {
                return;
            }

            if (player.getWorld().getName().equalsIgnoreCase("world_nether")) {
                chunkManager.unlockChunk(player.getWorld(), newChunkX, newChunkZ);
            }
            if (player.getWorld().getName().equalsIgnoreCase("world")) {
                chunkManager.unlockChunk(player.getWorld(), newChunkX, newChunkZ);
            }
        }

}