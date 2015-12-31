package us.swiftex.custominventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.swiftex.custominventories.events.Events;
import us.swiftex.custominventories.inventories.CustomHolder;
import us.swiftex.custominventories.utils.BungeeCordUtils;
import us.swiftex.custominventories.utils.Variable;


public class CustomInventoriesMain extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getServer().getPluginManager().registerEvents(new Events(), this);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        new BungeeCordUtils(this);

        Variable.register(this, Variable.NAME);
        Variable.register(this, Variable.ONLINE_PLAYERS);
        Variable.register(this, Variable.MAX_HEALTH);
        Variable.register(this, Variable.HEALTH);
        Variable.register(this, Variable.LEVEL);
        Variable.register(this, Variable.LOCATION);
    }

    @Override
    public void onDisable() {
        plugin = null;

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getOpenInventory().getTopInventory().getHolder() instanceof CustomHolder) {
                player.closeInventory();
            }
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
