package us.swiftex.custominventories.utils.server;

import us.swiftex.custominventories.utils.Messages;

import java.lang.String;
import java.lang.System;
import java.net.InetSocketAddress;

public class ServerInfo {

    private final ServerAddress serverAddress;
    private final String name;
    private final int interval;
    private final long millisInterval;

    private long lastUpdate;

    private boolean online;
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;

    public ServerInfo(InetSocketAddress serverAddress, String name, int interval) {
        this(new ServerAddress(serverAddress.getHostString(), serverAddress.getPort()), name, interval);
    }

    public ServerInfo(ServerAddress serverAddress, String name, int interval) {
        this.serverAddress = serverAddress;
        this.name = name;
        this.interval = interval;
        this.millisInterval = interval * 50;

        this.online = false;
        this.motd = Messages.OFFLINE.get();
        this.onlinePlayers = 0;
        this.maxPlayers = 0;
    }

    public synchronized ServerAddress getAddress() {
        return this.serverAddress;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized int getInterval() {
        return interval;
    }

    public synchronized long getMillisInterval() {
        return millisInterval;
    }

    public synchronized boolean should() {
        return System.currentTimeMillis() >= (millisInterval + lastUpdate);
    }

    public synchronized long getLastUpdate() {
        return lastUpdate;
    }

    public synchronized void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public synchronized void setOnline(boolean online) {
        this.online = online;
    }

    public synchronized boolean isOnline() {
        return online;
    }

    public synchronized void setMotd(String motd) {
        this.motd = motd;
    }

    public synchronized String getMotd() {
        return motd;
    }

    public synchronized int getOnlinePlayers() {
        return onlinePlayers;
    }

    public synchronized void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public synchronized int getMaxPlayers() {
        return maxPlayers;
    }

    public synchronized void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}