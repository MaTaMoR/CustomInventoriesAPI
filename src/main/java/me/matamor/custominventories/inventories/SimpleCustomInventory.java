package me.matamor.custominventories.inventories;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.matamor.custominventories.CustomInventories;
import me.matamor.custominventories.icons.InventoryIcon;
import me.matamor.custominventories.utils.Size;
import me.matamor.custominventories.utils.Utils;
import me.matamor.custominventories.utils.Validate;

import java.util.*;
import java.util.logging.Level;

public class SimpleCustomInventory implements CustomInventory {

    private final Map<Integer, InventoryIcon> icons = new LinkedHashMap<>();
    private final Set<Player> viewers = new HashSet<>();

    @Getter
    private Size size;

    @Getter
    private String title;

    @Setter
    private boolean interactInventory = true;

    public SimpleCustomInventory() {
        this(Size.ONE_LINE, "");
    }

    public SimpleCustomInventory(Size size, String title) {
        this(size, title, null);
    }

    public SimpleCustomInventory(Size size, String title, Map<Integer, InventoryIcon> defaults) {
        Validate.notNull(size, "Size can't be null!");
        Validate.notNull(title, "Title can't be null!");

        this.size = size;
        this.title = title;

        if (defaults != null) {
            defaults.forEach(this::setIcon);
        }
    }

    @Override
    public void setSize(Size size) {
        Validate.notNull(size, "Size can't be null!");

        this.size = size;
    }

    @Override
    public void setTitle(String title) {
        Validate.notNull(title, "Title can't be null!");

        this.title = title;
    }

    @Override
    public boolean canInteractInventory() {
        return this.interactInventory;
    }

    @Override
    public void setIcon(int position, InventoryIcon icon) {
        if (icon == null) {
            this.icons.remove(position);
        } else {
            this.icons.put(position, icon);
        }
    }

    @Override
    public InventoryIcon getIcon(int position) {
        return this.icons.get(position);
    }

    @Override
    public Map<Integer, InventoryIcon> getIcons() {
        return Collections.unmodifiableMap(this.icons);
    }

    @Override
    public Inventory createInventory(Player player) {
        Validate.notNull(player, "Player can't be null!");

        Inventory inventory = Bukkit.createInventory(new CustomInventoryHolder(this), this.size.getSize(), Utils.fullFormat(this.title, player));

        for (Map.Entry<Integer, InventoryIcon> entry : this.icons.entrySet()) {
            if (entry.getKey() < 0 || entry.getKey() >= this.size.getSize()) continue;

            try {
                inventory.setItem(entry.getKey(), entry.getValue().getItem(player).build(player));
            } catch (Exception e) {
                CustomInventories.getPlugin().getLogger().log(Level.SEVERE, "Error while building item in slot: " + entry.getKey(), e);
            }
        }

        return inventory;
    }


    @Override
    public Inventory openInventory(Player player) {
        Inventory inventory = createInventory(player);

        player.openInventory(inventory);

        return inventory;
    }

    @Override
    public void update() {
        Iterator<Player> iterator = this.viewers.iterator();

        while (iterator.hasNext()) {
            Player player = iterator.next();

            if (player.isOnline()) {
                update(player);
            } else {
                iterator.remove();
            }
        }
    }

    @Override
    public Collection<Player> getViewers() {
        return this.viewers;
    }

    public void update(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory == null) return;

        if (inventory.getHolder() instanceof CustomInventoryHolder) {
            CustomInventoryHolder customHolder = (CustomInventoryHolder) inventory.getHolder();

            //Set instance of current CustomInventory
            if (customHolder.getCustomInventory() != this) {
                customHolder.setCustomInventory(this);
            }

            //Not the same size so open another inventory
            if (getSize().getSize() != inventory.getSize() || !Objects.equals(inventory.getTitle(), Utils.fullFormat(this.title, player))) {
                openInventory(player);
            } else {
                Inventory newInventory = createInventory(player);

                ItemStack[] currentContents = inventory.getContents();
                ItemStack[] newContents = newInventory.getContents();

                for (int i = 0; currentContents.length > i; i++) {
                    ItemStack oldStack = currentContents[i];
                    ItemStack newStack = newContents[i];

                    if (oldStack == null && newStack != null) {
                        currentContents[i] = newStack;
                    } else if (oldStack != null && newStack != null) {
                        if (!oldStack.isSimilar(newStack)) {
                            currentContents[i] = newStack;
                        }
                    } else {
                        currentContents[i] = null;
                    }
                }

                inventory.setContents(currentContents);
            }
        }
    }
}
