package me.matamor.custominventories.actions.defaults;

import lombok.Getter;
import me.matamor.custominventories.actions.ClickAction;
import org.bukkit.entity.Player;
import me.matamor.custominventories.utils.Utils;
import me.matamor.custominventories.utils.Validate;

public class PlayerUseCommand implements ClickAction {

    @Getter
    private final String command;

    public PlayerUseCommand(String command) {
        Validate.notNull(command, "Command can't be null");

        this.command = command;
    }

    @Override
    public void execute(Player player) {
        player.performCommand(Utils.fullFormat(this.command, player));
    }
}
