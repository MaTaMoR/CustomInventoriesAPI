package me.matamor.custominventories.actions.defaults;

import lombok.Getter;
import org.bukkit.entity.Player;
import me.matamor.custominventories.CustomInventories;
import me.matamor.custominventories.actions.ClickAction;
import me.matamor.custominventories.utils.Validate;

public class MoveServer implements ClickAction {

    @Getter
    private final String server;

    public MoveServer(String server) {
        Validate.notNull(server, "Server can't be null");

        this.server = server;
    }

    @Override
    public void execute(Player player) {
        CustomInventories.getBungeeCord().moveServer(player, this.server);
    }
}
