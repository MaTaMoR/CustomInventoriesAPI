package us.swiftex.custominventories.actions.defaults;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.utils.Utils;
import us.swiftex.custominventories.utils.Validate;
import us.swiftex.custominventories.utils.Variable;

public class ConsoleUseCommand extends ClickAction {

    private final String command;

    public ConsoleUseCommand(String command) {
        Validate.notNull(command, "Command can't be null");

        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public void execute(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), Variable.replace(Utils.colorize(command), player));
    }
}
