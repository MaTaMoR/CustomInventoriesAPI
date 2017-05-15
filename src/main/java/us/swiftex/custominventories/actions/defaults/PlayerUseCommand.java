package us.swiftex.custominventories.actions.defaults;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.utils.Utils;
import us.swiftex.custominventories.utils.Validate;
import us.swiftex.custominventories.utils.Variable;

public class PlayerUseCommand extends ClickAction {

    private final String command;

    public PlayerUseCommand(String command) {
        Validate.notNull(command, "Command can't be null");

        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public void execute(Player player) {
        player.performCommand(Variable.replace(Utils.color(command), player));
    }
}
