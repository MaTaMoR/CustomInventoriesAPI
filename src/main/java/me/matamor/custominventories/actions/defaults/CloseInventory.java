package me.matamor.custominventories.actions.defaults;

import org.bukkit.entity.Player;
import me.matamor.custominventories.actions.ClickAction;

public class CloseInventory implements ClickAction {

    @Override
    public void execute(Player player) {
        player.closeInventory();
    }
}
