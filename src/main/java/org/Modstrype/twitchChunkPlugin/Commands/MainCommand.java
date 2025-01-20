package org.Modstrype.twitchChunkPlugin.Commands;


import org.Modstrype.twitchChunkPlugin.Chunk.ChunkManager;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MainCommand implements CommandExecutor {

    private final ChunkManager chunkManager;

    public MainCommand(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "[ChunkUnlocker] Dieser Befehl kann nur ingame benutzt werden!");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(ChatColor.YELLOW + "[ChunkUnlocker] Bitte nutze /unlockchunk [Nord|Sued|Ost|West]");
            return true;
        }

        String direction = args[0].toLowerCase();
        Chunk currentChunk = player.getLocation().getChunk();
        World world = player.getWorld();

        int newChunkX = currentChunk.getX();
        int newChunkZ = currentChunk.getZ();

        switch (direction) {
            case "nord" -> newChunkZ -= 1;
            case "sued" -> newChunkZ += 1;
            case "ost"  -> newChunkX += 1;
            case "west"  -> newChunkX -= 1;
            default -> {
                player.sendMessage(ChatColor.RED + "[ChunkUnlocker] Ungültige Richtung! (Nord, Sued, Ost, West)");
                return true;
            }
        }


        chunkManager.unlockChunk(world, newChunkX, newChunkZ);
        chunkManager.getOrLoadChunk(world, newChunkX, newChunkZ);
        chunkManager.updateChunks(player);



        player.sendMessage(ChatColor.GREEN + "[ChunkUnlocker] Ein neuer Chunk in Richtung " + direction + " wurde freigeschaltet!");
        return true;
    }
}