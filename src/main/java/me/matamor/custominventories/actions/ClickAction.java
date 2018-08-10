package me.matamor.custominventories.actions;

import me.matamor.custominventories.inventories.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.matamor.custominventories.enums.ClickType;

public interface ClickAction extends ActionHandler {

    void execute(Player player);

    @Override
    default void handle(CustomInventory customInventory, Player player, ClickType clickType, boolean shift, InventoryClickEvent event) {
        execute(player);
    }
}
