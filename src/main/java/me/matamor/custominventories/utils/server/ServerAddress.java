package me.matamor.custominventories.utils.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ServerAddress {

    @Getter
    private String address;

    @Getter
    private int port;

    @Override
    public String toString() {
        return this.address + ":" + this.port;
    }
}
