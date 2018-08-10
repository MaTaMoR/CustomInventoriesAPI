package me.matamor.custominventories.actions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.matamor.custominventories.enums.ClickType;
import me.matamor.custominventories.inventories.CustomInventory;

public abstract class AdvancedClickAction implements ActionHandler {

    public abstract void execute(Player player, ClickType clickType, boolean shift);

    @Override
    public void handle(CustomInventory customInventory, Player player, ClickType clickType, boolean shift, InventoryClickEvent event) {
        execute(player, clickType, shift);
    }
}
