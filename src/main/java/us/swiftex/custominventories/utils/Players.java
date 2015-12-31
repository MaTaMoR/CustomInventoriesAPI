package us.swiftex.custominventories.utils;

import org.bukkit.Bukkit;

public class Players {

    private static long lastOnlinePlayersRefresh;
    private static int onlinePlayers;

    public static int getOnlinePlayers() {
        long now = System.currentTimeMillis();
        if (lastOnlinePlayersRefresh == 0 || now - lastOnlinePlayersRefresh > 1000) {

            lastOnlinePlayersRefresh = now;
            onlinePlayers = Bukkit.getOnlinePlayers().size();
        }

        return onlinePlayers;
    }
}