package org.Modstrype.twitchChunkPlugin.Commands;

import org.Modstrype.twitchChunkPlugin.Chunk.ChunkManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ResetChunksCommand implements CommandExecutor {

    private final ChunkManager chunkManager;

    public ResetChunksCommand(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "[ChunkUnlocker] Dieser Befehl kann nur von einem Spieler verwendet werden!");
            return true;
        }


        World world = player.getWorld();
        Location spawnLocation = world.getSpawnLocation();
        player.teleport(spawnLocation);
        player.sendMessage(ChatColor.GREEN + "[ChunkUnlocker] Du wurdest zum Welt-Spawn teleportiert.");


        int spawnChunkX = spawnLocation.getBlockX() >> 4;
        int spawnChunkZ = spawnLocation.getBlockZ() >> 4;


        String spawnChunkKey = world.getName() + ";" + spawnChunkX + ";" + spawnChunkZ;


        Set<String> unlockedChunks = chunkManager.getUnlockedChunks();


        for (String chunkKey : unlockedChunks) {
            if (!chunkKey.equals(spawnChunkKey)) {
                String[] parts = chunkKey.split(";");

                String chunkWorldName = parts[0];
                int x = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);

                if (chunkWorldName.equals(world.getName())) {
                    world.unloadChunk(x, z, true);
                }
            }
        }


        chunkManager.resetUnlockedChunks();
        chunkManager.updateChunks(player);
        chunkManager.unlockChunk(world, spawnChunkX, spawnChunkZ);


        player.sendMessage(ChatColor.GREEN + " [ChunkUnlocker] Alle freigeschalteten Chunks wurden zurückgesetzt! Nur der Spawn-Chunk bleibt erhalten.");

        return true;
    }
}