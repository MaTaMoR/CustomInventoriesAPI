package us.swiftex.custominventories.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import us.swiftex.custominventories.CustomInventoriesMain;

public class BungeeCordUtils {

    private static CustomInventoriesMain plugin;

    public BungeeCordUtils(CustomInventoriesMain plugin) {
        BungeeCordUtils.plugin = plugin;
    }

    public static void moveServer(Player player, String server) {
        Validate.notNull(plugin, "Plugin can't be null");
        Validate.notNull(player, "Player can't be null");
        Validate.notNull(server, "Server cant' be null");

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Connect");
                            out.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
