package us.swiftex.custominventories.actions.defaults;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ClickAction;

public class CloseInventory extends ClickAction {

    @Override
    public void execute(Player player) {
        player.closeInventory();
    }
}
