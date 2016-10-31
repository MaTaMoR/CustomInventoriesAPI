package us.swiftex.custominventories.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import us.swiftex.custominventories.enums.ClickType;
import us.swiftex.custominventories.inventories.CustomInventory;

public abstract class AdvancedClickAction implements ActionHandler {

    public abstract void execute(Player player, ClickType clickType, boolean shift);

    @Override
    public void handle(CustomInventory customInventory, Player player, ClickType clickType, boolean shift, InventoryClickEvent event) {
        execute(player, clickType, shift);
    }
}
