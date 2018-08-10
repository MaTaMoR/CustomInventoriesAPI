package me.matamor.custominventories.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import me.matamor.custominventories.CustomInventories;

public class BungeeCord {

    private CustomInventories plugin;

    public BungeeCord(CustomInventories plugin) {
        this.plugin = plugin;
    }

    public void moveServer(Player player, String server) {
        Validate.notNull(plugin, "Plugin can't be null");
        Validate.notNull(player, "Player can't be null");
        Validate.notNull(server, "Server cant' be null");

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Connect");
                            out.writeUTF(server);

        player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
    }
}
