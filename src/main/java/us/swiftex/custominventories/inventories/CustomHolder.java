package us.swiftex.custominventories.inventories;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CustomHolder implements InventoryHolder {

    private CustomInventory customInventory;

    public CustomHolder(CustomInventory customInventory) {
        this.customInventory = customInventory;
    }

    public CustomInventory getCustomInventory() {
        return customInventory;
    }

    public void setCustomInventory(CustomInventory customInventory) {
        this.customInventory = customInventory;
    }

    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(null, 9);
    }
}
