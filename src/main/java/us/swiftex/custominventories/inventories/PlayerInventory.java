package us.swiftex.custominventories.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import us.swiftex.custominventories.icons.Icon;

public abstract class PlayerInventory<T extends Icon> extends CustomInventory<T> {

    public PlayerInventory(String title, int size) {
        super(title, size);
    }

    public abstract Inventory getInventory(Player player);
}
