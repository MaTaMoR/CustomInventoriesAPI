package me.matamor.custominventories.inventories;

import me.matamor.custominventories.icons.InventoryIcon;
import me.matamor.custominventories.utils.Size;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.Map;

public interface CustomInventory {

    Size getSize();

    void setSize(Size size);

    String getTitle();

    void setTitle(String title);

    boolean canInteractInventory();

    void setInteractInventory(boolean interactInventory);

    void setIcon(int position, InventoryIcon icon);

    InventoryIcon getIcon(int position);

    Map<Integer, InventoryIcon> getIcons();

    default boolean hasInventory(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        return inventory != null && inventory.getHolder() instanceof CustomInventoryHolder && ((CustomInventoryHolder) inventory.getHolder()).getCustomInventory() == this;
    }

    Inventory createInventory(Player player);

    Inventory openInventory(Player player);

    void update(Player player);

    void update();

    Collection<Player> getViewers();

    default void onOpen(Player player) {

    }

    default void onClose(Player player) {

    }

}
