package us.swiftex.custominventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.swiftex.custominventories.events.Events;
import us.swiftex.custominventories.utils.*;
import us.swiftex.custominventories.utils.server.ServerManager;

public class CustomInventories extends JavaPlugin {

    private static Plugin plugin;
    private static BungeeCord bungeeCord;
    private static ServerManager serverManager;

    @Override
    public void onEnable() {
        Messages.load(this);
        Settings.load(this);

        plugin = this;

        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        bungeeCord = new BungeeCord(this);
        serverManager = new ServerManager(this);
    }

    @Override
    public void onDisable() {
        serverManager.stop();
        serverManager.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static BungeeCord getBungeeCord() {
        return bungeeCord;
    }

    public static ServerManager getServerManager() {
        return serverManager;
    }
}


