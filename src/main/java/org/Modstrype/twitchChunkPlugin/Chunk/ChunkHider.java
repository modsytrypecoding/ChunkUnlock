package org.Modstrype.twitchChunkPlugin.Chunk;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.PacketType;

import org.Modstrype.twitchChunkPlugin.Main.Main;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ChunkHider {

    private final Main plugin;
    private final ProtocolManager protocolManager;
    private final ChunkManager chunkManager;

    public ChunkHider(Main plugin, ProtocolManager protocolManager, ChunkManager chunkManager) {
        this.plugin = plugin;
        this.protocolManager = protocolManager;
        this.chunkManager = chunkManager;
    }

    public void register() {

        protocolManager.addPacketListener(
                new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.MAP_CHUNK) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        Player player = event.getPlayer();
                        if (player == null) return;


                        int chunkX = event.getPacket().getIntegers().read(0);
                        int chunkZ = event.getPacket().getIntegers().read(1);


                        World world = player.getWorld();


                        boolean unlocked = chunkManager.isChunkUnlocked(world, chunkX, chunkZ);
                        if (!unlocked) {

                            event.setCancelled(true);
                        }
                    }
                }
        );
    }
}