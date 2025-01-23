package org.Modstrype.twitchChunkPlugin.Main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import org.Modstrype.twitchChunkPlugin.Chunk.ChunkHider;
import org.Modstrype.twitchChunkPlugin.Chunk.ChunkManager;
import org.Modstrype.twitchChunkPlugin.Commands.MainCommand;
import org.Modstrype.twitchChunkPlugin.Commands.ResetChunksCommand;
import org.Modstrype.twitchChunkPlugin.Commands.UnlockChunksAllCommand;
import org.Modstrype.twitchChunkPlugin.Listener.PlayerJoinListener;
import org.Modstrype.twitchChunkPlugin.Listener.PlayerMovementListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main instance;
    private ChunkManager chunkManager;
    private ProtocolManager protocolManager;
    private ChunkHider chunkHider;

    public static Set<UUID> ignoringWorldChange = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;


        saveDefaultConfig();


        this.chunkManager = new ChunkManager(this);
        chunkManager.loadUnlockedChunksFromConfig();


        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
            chunkHider = new ChunkHider(this, protocolManager, chunkManager);
            chunkHider.register();
        } else {
            getLogger().warning("[ChunkUnlocker] ProtocolLib wurde nicht gefunden! Unsichtbare Chunks funktionieren evtl. nicht wie erwartet.");
        }


        getCommand("unlockchunk").setExecutor(new MainCommand(chunkManager));
        getCommand("expand").setExecutor(new UnlockChunksAllCommand(chunkManager));
        getCommand("ChunkReset").setExecutor(new ResetChunksCommand(chunkManager));

        Bukkit.getPluginManager().registerEvents(new PlayerMovementListener(chunkManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(chunkManager), this);

        getLogger().info("[ChunkUnlocker] Plugin aktiviert!");
    }

    @Override
    public void onDisable() {

        chunkManager.saveUnlockedChunksToConfig();
        getLogger().info("[ChunkUnlocker] Plugin deaktiviert!");
    }

}