package org.Modstrype.twitchChunkPlugin.Commands;

import org.Modstrype.twitchChunkPlugin.Chunk.ChunkManager;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class UnlockChunksAllCommand implements CommandExecutor {

    private final ChunkManager chunkManager;

    public UnlockChunksAllCommand(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "[ChunkUnlocker] Dieser Befehl kann nur ingame benutzt werden!");
            return true;
        }

        Chunk currentChunk = player.getLocation().getChunk();
        World world = player.getWorld();

        int currentX = currentChunk.getX();
        int currentZ = currentChunk.getZ();


        int[][] directions = {
                {0, -1},  // North
                {0, 1},   // South
                {1, 0},   // East
                {-1, 0}   // West
        };

        for (int[] dir : directions) {
            int newX = currentX + dir[0];
            int newZ = currentZ + dir[1];
            chunkManager.unlockChunk(world, newX, newZ);
            chunkManager.getOrLoadChunk(world, newX, newZ);
        }

        chunkManager.updateChunks(player);
        player.sendMessage(ChatColor.GREEN + "[ChunkUnlocker] Alle angrenzenden Chunks wurden freigeschaltet!");
        return true;
    }
}
