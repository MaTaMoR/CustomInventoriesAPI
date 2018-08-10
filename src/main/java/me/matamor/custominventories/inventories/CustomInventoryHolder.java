package me.matamor.custominventories.inventories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@AllArgsConstructor
public class CustomInventoryHolder implements InventoryHolder {

    @Getter
    private CustomInventory customInventory;

    protected void setCustomInventory(CustomInventory customInventory) {
        this.customInventory = customInventory;
    }

    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(null, 9);
    }
}
