package me.matamor.custominventories.utils.viewer;

import lombok.Getter;
import me.matamor.custominventories.inventories.SimpleCustomInventory;
import me.matamor.custominventories.utils.*;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import me.matamor.custominventories.actions.ClickAction;
import me.matamor.custominventories.icons.InventoryIcon;
import me.matamor.custominventories.icons.NormalInventoryIcon;
import me.matamor.custominventories.inventories.CustomInventory;

import java.util.Collection;
import java.util.Iterator;

public class ItemViewer extends Pagination<InventoryIcon> {

    @Getter
    private final Size size;

    @Getter
    private final String title;

    public ItemViewer(Size size, String title, Collection<InventoryIcon> InventoryIcons) {
        super((size = Size.fit(size.getSize() + Size.ONE_LINE.getSize())).getSize() - Size.ONE_LINE.getSize(), InventoryIcons);

        this.size = size;
        this.title = title;
    }

    private InventoryIcon getBefore(final int page, InventoryProvider<CustomInventory> provider) {
        boolean exists = exists(page);

        InventoryIcon icon = new NormalInventoryIcon(CustomItem.builder(Material.STAINED_CLAY, 1, (short) (exists ? DyeColor.LIME.getData() : DyeColor.RED.getData()))
            .setName((exists ? "&a<<" : "&4<<")).build());

        if (exists) {
            icon.addClickAction((ClickAction) player -> getView(page, provider).openInventory(player));
        }

        return icon;
    }

    private InventoryIcon getNext(final int page, InventoryProvider<CustomInventory> provider) {
        boolean exists = exists(page);

        InventoryIcon icon = new NormalInventoryIcon(CustomItem.builder(Material.STAINED_CLAY, 1, (short) (exists ? DyeColor.LIME.getData() : DyeColor.RED.getData()))
                .setName((exists ? "&a>>" : "&4>>")).build());

        if (exists) {
            icon.addClickAction((ClickAction) player -> getView(page, provider).openInventory(player));
        }

        return icon;
    }

    public CustomInventory getView(int page) {
        return getView(0, SimpleCustomInventory::new);
    }

    public CustomInventory getView(int page, InventoryProvider<CustomInventory> provider) {
        if (!exists(page)) {
            throw new IndexOutOfBoundsException("Index: " + page + ", Size: " + getSize().getPosition());
        }

        String replacedTitle = this.title.replace("{page}", String.valueOf((page + 1))).replace("{total_pages}", String.valueOf(totalPages()));

        InventoryTemplate template = new InventoryTemplate(this.size, replacedTitle);
        Iterator<InventoryIcon> inventoryIcons = getPage(page).iterator();

        //Fill the inventory with all the icons
        for (InventoryLine inventoryLine : template.getInventoryLines()) {
            int position = 0;

            while (inventoryIcons.hasNext() && position < 9) {
                inventoryLine.setIcon(position++, inventoryIcons.next());
            }
        }

        //Set the next and previous buttons
        InventoryLine lastLine = template.getLastLine();

        lastLine.setIcon(0, getBefore(page - 1, provider));
        lastLine.setIcon(8, getNext(page + 1, provider));

        //Fill the rest of the line
        for (int i = 1; 8 > i; i++) {
            lastLine.setIcon(i, new NormalInventoryIcon(CustomItem.builder(Material.STAINED_GLASS_PANE, 1, (short) DyeColor.BLACK.getData()).setName("").build()));
        }

        return template.createInventory(provider.newInstance());
    }
}