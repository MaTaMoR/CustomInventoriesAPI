package us.swiftex.custominventories.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.swiftex.custominventories.utils.*;
import us.swiftex.custominventories.icons.Icon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CustomInventory {

    private final Map<Integer, Icon> icons = new HashMap<>();

    private String title;
    private Size size;

    public CustomInventory(Size size, String title) {
        Validate.notNull(title, "Title can't be null");

        this.title = title;
        this.size = size;
    }

    public CustomInventory(CustomInventory customInventory) {
        this.title = customInventory.getTitle();
        this.size = customInventory.size();

        for(Entry<Integer, Icon> entry : customInventory.getIcons().entrySet()) {
            setIcon(entry.getKey(), entry.getValue());
        }
    }

    public Map<Integer, Icon>  getIcons() {
        return Collections.unmodifiableMap(icons);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Validate.notNull(title, "Title can't be null");

        this.title = title;
    }

    public void setIcon(int position, Icon icon) {
        icons.put(position, icon);
    }

    public Icon getIcon(int position) {
        return icons.get(position);
    }

    public void removeIcon(int position) {
        icons.remove(position);
    }

    public Size size() {
        return size;
    }

    public Inventory createInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(new CustomHolder(this), size.getSize(), Utils.colorize(Variable.replace(title, player)));

        for(Entry<Integer, Icon> entry : icons.entrySet()) {
            if(entry.getKey() > size.getSize()) break;

            if(entry.getValue().canSee(player)) {
                inventory.setItem(entry.getKey(), entry.getValue().getItem(player).build(player));
            }
        }

        return inventory;
    }

    public Inventory openInventory(Player player) {
        Inventory inventory = createInventory(player);
        player.openInventory(inventory);

        return inventory;
    }

    public void update(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if(inventory == null || size().getSize() != inventory.getSize()) return;

        if(inventory.getHolder() instanceof CustomHolder) {
            CustomHolder customHolder = (CustomHolder) inventory.getHolder();

            if(customHolder.getInventory() != this) customHolder.setCustomInventory(this); //Set CustomInventory if it's different

            Inventory newInventory = createInventory(player);

            for(int i = 0; inventory.getSize() > i; i++) {
                ItemStack oldStack = inventory.getItem(i);
                ItemStack newStack = newInventory.getItem(i);

                if(oldStack == null && newStack != null) {
                    inventory.setItem(i, newStack);
                } else if(oldStack != null && newStack != null) {
                    if(!oldStack.isSimilar(newStack)) inventory.setItem(i, newStack);
                } else {
                    inventory.setItem(i, null);
                }
            }
        }
    }

    public void onOpen(Player player) { }

    public void onClose(Player player) { }

    public static CustomInventory get(Player player) {
        return get(player.getOpenInventory().getTopInventory());
    }

    public static CustomInventory get(Inventory inventory) {
        if(inventory.getHolder() instanceof CustomHolder) {
            return ((CustomHolder) inventory.getHolder()).getCustomInventory();
        }

        return null;
    }
}
