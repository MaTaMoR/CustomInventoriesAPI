package us.swiftex.custominventories;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.swiftex.custominventories.events.Events;
import us.swiftex.custominventories.utils.*;
import us.swiftex.custominventories.utils.server.ServerManager;

public class CustomInventories extends JavaPlugin {

    @Getter
    private static Plugin plugin;

    @Getter
    private static BungeeCord bungeeCord;

    @Getter
    private static ServerManager serverManager;

    @Override
    public void onEnable() {
        Messages.load(this);
        Settings.load(this);

        plugin = this;

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        bungeeCord = new BungeeCord(this);
        serverManager = new ServerManager(this);
    }

    @Override
    public void onDisable() {
        serverManager.stop();
        serverManager.clear();

        for (Player player : getServer().getOnlinePlayers()) {
            player.closeInventory();
        }
    }
}


