package us.swiftex.custominventories.actions.defaults;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.CustomInventories;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.utils.BungeeCord;
import us.swiftex.custominventories.utils.Validate;

public class MoveServer extends ClickAction {

    private final String server;

    public MoveServer(String server) {
        Validate.notNull(server, "Server can't be null");

        this.server = server;
    }

    public String getServer() {
        return server;
    }

    @Override
    public void execute(Player player) {
        CustomInventories.getBungeeCord().moveServer(player, server);
    }
}
