package us.swiftex.custominventories.inventories;

import org.bukkit.inventory.Inventory;
import us.swiftex.custominventories.icons.Icon;

public abstract class NormalInventory<T extends Icon> extends CustomInventory<T> {

    public NormalInventory(String title, int size) {
        super(title, size);
    }

    public abstract Inventory getInventory();
}
