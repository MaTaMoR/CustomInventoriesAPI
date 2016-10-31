package us.swiftex.custominventories.utils.server;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import us.swiftex.custominventories.CustomInventories;
import us.swiftex.custominventories.utils.Messages;
import us.swiftex.custominventories.utils.Settings;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class ServerManager {

    private final Map<String, ServerInfo> servers = new ConcurrentHashMap<>();

    private BukkitTask task;

    public ServerManager(final CustomInventories plugin) {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<ServerInfo> iterator = servers.values().iterator();

                while (iterator.hasNext()) {
                    ServerInfo info = iterator.next();

                    if (info.getLastUpdate() != 0 && System.currentTimeMillis() - info.getLastUpdate() > 600000) {
                        iterator.remove();
                        plugin.getLogger().log(Level.SEVERE, "Removed server " + info.getName() + " from tracking due to inactivity ");
                    } else if (info.should()) {
                        boolean displayOffline = false;

                        try {
                            PingResponse data = ServerPinger.fetchData(info.getAddress(), 1000);

                            if (data.isOnline()) {
                                info.setOnline(true);
                                info.setOnlinePlayers(data.getOnlinePlayers());
                                info.setMaxPlayers(data.getMaxPlayers());
                                info.setMotd(data.getMotd());
                            } else {
                                displayOffline = true;
                            }
                        } catch (UnknownHostException | ConnectException | SocketTimeoutException e) {
                            displayOffline = true;
                            if(Settings.LOG_ON_SERVER_DOWN.get()) {
                                plugin.getLogger().log(Level.SEVERE, "Couldn't fetch data from " + info.getName() + "(" + info.getAddress() + ") connection exception");
                            }
                        } catch (Exception e) {
                            displayOffline = true;
                            plugin.getLogger().log(Level.SEVERE, "Couldn't fetch data from " + info.getName() + "(" + info.getAddress() + "), unhandled exception: ", e);
                        }

                        info.setLastUpdate(System.currentTimeMillis());

                        if (displayOffline) {
                            info.setOnline(false);
                            info.setOnlinePlayers(0);
                            info.setMaxPlayers(0);
                            info.setMotd(Messages.OFFLINE.get());
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 0);
    }

    public synchronized void register(ServerInfo info) {
        if(servers.containsKey(info.getName())) {
            throw new RuntimeException("The server " + info.getName() + " is already registered");
        }

        info = new ServerInfo(new ServerAddress("82.223.67.170", info.getAddress().getPort()), info.getName(), info.getInterval());
        servers.put(info.getName(), info);
    }

    public synchronized void unregister(String name) {
        servers.remove(name);
    }

    public ServerInfo getServer(String name) {
        return servers.get(name);
    }

    public Collection<ServerInfo> getServers() {
        return Collections.unmodifiableCollection(servers.values());
    }

    public boolean isActive() {
        return task != null;
    }

    public synchronized void clear() {
        servers.clear();
    }

    public synchronized void stop() {
        if(isActive()) {
            task.cancel();
            task = null;
        }
    }
}
