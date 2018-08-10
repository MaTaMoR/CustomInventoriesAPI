package me.matamor.custominventories.actions.defaults;

import lombok.Getter;
import me.matamor.custominventories.actions.ClickAction;
import org.bukkit.entity.Player;
import me.matamor.custominventories.utils.Utils;
import me.matamor.custominventories.utils.Validate;

public class SendMessage implements ClickAction {

    @Getter
    private final String message;

    public SendMessage(String message) {
        Validate.notNull(message, "Message can't be null!");

        this.message = message;
    }

    @Override
    public void execute(Player player) {
        player.sendMessage(Utils.fullFormat(this.message, player));
    }
}
