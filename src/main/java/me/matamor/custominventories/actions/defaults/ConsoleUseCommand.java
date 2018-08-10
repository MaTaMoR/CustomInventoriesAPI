package me.matamor.custominventories.actions.defaults;

import lombok.Getter;
import me.matamor.custominventories.actions.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.matamor.custominventories.utils.Utils;
import me.matamor.custominventories.utils.Validate;

public class ConsoleUseCommand implements ClickAction {

    @Getter
    private final String command;

    public ConsoleUseCommand(String command) {
        Validate.notNull(command, "Command can't be null");

        this.command = command;
    }

    @Override
    public void execute(Player player) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), Utils.fullFormat(this.command, player));
    }
}
