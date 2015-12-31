package us.swiftex.custominventories.actions.defaults;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.utils.Utils;
import us.swiftex.custominventories.utils.Variable;

public class SendMessage extends ClickAction {

    private final String message;

    public SendMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void execute(Player player) {
        player.sendMessage(Variable.replace(Utils.colorize(message), player));
    }
}
