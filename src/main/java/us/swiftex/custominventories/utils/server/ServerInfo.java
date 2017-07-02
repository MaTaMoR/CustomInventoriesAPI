package us.swiftex.custominventories.utils.server;

import lombok.Getter;
import lombok.Setter;
import us.swiftex.custominventories.utils.Messages;

import java.lang.String;
import java.lang.System;
import java.net.InetSocketAddress;

public class ServerInfo {

    @Getter
    private final ServerAddress serverAddress;

    @Getter
    private final String name;

    @Getter
    private final int interval;

    @Getter
    private final long millisInterval;

    @Getter @Setter
    private long lastUpdate;

    @Getter @Setter
    private boolean online;

    @Getter @Setter
    private String motd;

    @Getter @Setter
    private int onlinePlayers;

    @Getter @Setter
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

    public boolean should() {
        return System.currentTimeMillis() >= (millisInterval + lastUpdate);
    }
}