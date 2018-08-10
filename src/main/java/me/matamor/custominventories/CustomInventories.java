package me.matamor.custominventories;

import lombok.Getter;
import me.matamor.custominventories.events.Events;
import me.matamor.custominventories.reflections.PlayerReflections;
import me.matamor.custominventories.utils.*;
import me.matamor.custominventories.utils.server.ServerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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

        getServer().getPluginManager().registerEvents(new Events(this), this);
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

    private void registerVariables() {
        InventoryVariables.register(new SimpleInventoryVariable("{name}") {
            @Override
            public String getReplacement(Player player) {
                if (player == null) return null;

                return player.getName();
            }
        });

        InventoryVariables.register(new SimpleInventoryVariable("{online_players}") {
            @Override
            public String getReplacement(Player player) {
                return String.valueOf(Players.getOnlinePlayers());
            }
        });

        InventoryVariables.register(new SimpleInventoryVariable("{max_health}") {
            @Override
            public String getReplacement(Player player) {
                if (player == null) return null;

                return String.valueOf(Math.round(player.getMaxHealth()));
            }
        });

        InventoryVariables.register(new SimpleInventoryVariable("{health") {
            @Override
            public String getReplacement(Player player) {
                if (player == null) return null;

                return String.valueOf(Math.round(player.getHealth()));
            }
        });

        InventoryVariables.register(new SimpleInventoryVariable("{level}") {
            @Override
            public String getReplacement(Player player) {
                if (player == null) return null;

                return String.valueOf(player.getLevel());
            }
        });

        InventoryVariables.register(new SimpleInventoryVariable("{location}") {
            @Override
            public String getReplacement(Player player) {
                if (player == null) return null;

                Location location = player.getLocation();
                return "XYZ: " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
            }
        });

        InventoryVariables.register(new SimpleInventoryVariable("{ping}") {
            @Override
            public String getReplacement(Player player) {
                if (player == null) return null;

                return String.valueOf(PlayerReflections.getPing(player));
            }
        });
    }
}


