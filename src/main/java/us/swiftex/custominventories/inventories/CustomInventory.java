package us.swiftex.custominventories.inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import us.swiftex.custominventories.utils.*;
import us.swiftex.custominventories.icons.Icon;

import java.util.HashMap;
import java.util.Map;

public abstract class CustomInventory<T extends Icon> {

    private final Map<Integer, T> icons = new HashMap<>();
    private final CustomHolder customHolder;

    private String title;
    private Size size;

    public CustomInventory(String title, int size) {
        Validate.notNull(title, "Title can't be null");

        this.customHolder = new CustomHolder(this);

        this.title = title;
        this.size = Size.fit(size);
    }

    public Map<Integer, T> getIcons() {
        return icons;
    }

    public CustomHolder getCustomHolder() {
        return customHolder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Validate.notNull(title, "Title can't be null");

        this.title = title;
    }

    public abstract T setIcon(int position, T icon);

    public T getIcon(int position) {
        return icons.get(position);
    }

    public abstract void removeIcon(int position);

    public Size size() {
        return size;
    }

    public void onOpen(Player player) { }
    public void onClose(Player player) { }

    public static CustomInventory get(Player player) {
        if(player.getOpenInventory().getTopInventory().getHolder() instanceof CustomHolder) {
            return ((CustomHolder) player.getOpenInventory().getTopInventory().getHolder()).getCustomInventory();
        }

        return null;
    }

    public static CustomInventory get(Inventory inventory) {
        if(inventory.getHolder() instanceof CustomHolder) {
            return ((CustomHolder) inventory.getHolder()).getCustomInventory();
        }

        return null;
    }
}
