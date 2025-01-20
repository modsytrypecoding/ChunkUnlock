package org.Modstrype.twitchChunkPlugin.Commands;

import org.Modstrype.twitchChunkPlugin.Chunk.ChunkManager;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ResetChunksCommand implements CommandExecutor {

    private final ChunkManager chunkManager;

    public ResetChunksCommand(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Dieser Befehl kann nur von einem Spieler verwendet werden!");
            return true;
        }

        // Spieler an den Welt-Spawn teleportieren
        World world = player.getWorld();
        Location spawnLocation = world.getSpawnLocation();
        player.teleport(spawnLocation);
        player.sendMessage(ChatColor.GREEN + "Du wurdest zum Welt-Spawn teleportiert.");

        // Alle freigeschalteten Chunks zurücksetzen, außer dem Start-Chunk
        Set<String> unlockedChunks = new HashSet<>(chunkManager.getUnlockedChunks());
        unlockedChunks.remove(world.getName() + ";0;0"); // Start-Chunk (0, 0) bleibt erhalten

        for (String chunkKey : unlockedChunks) {
            String[] parts = chunkKey.split(";");
            int x = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);

            // Entlade die übrigen freigeschalteten Chunks
            world.unloadChunk(x, z, true);
        }

        // Entferne die restlichen Chunks aus dem Speicher
        chunkManager.resetUnlockedChunks();

        // Start-Chunk wieder laden
        Chunk startChunk = world.getChunkAt(0, 0);
        chunkManager.unlockChunk(world, 0, 0);
        chunkManager.getOrLoadChunk(world, 0, 0);
        chunkManager.updateChunks(player);

        player.sendMessage(ChatColor.GREEN + "Alle freigeschalteten Chunks wurden zurückgesetzt! Das System startet von neuem.");
        return true;
    }
}