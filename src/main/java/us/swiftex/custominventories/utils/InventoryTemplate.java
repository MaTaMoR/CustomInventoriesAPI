package us.swiftex.custominventories.utils;

import us.swiftex.custominventories.icons.Icon;
import us.swiftex.custominventories.inventories.CustomInventory;

import java.util.*;
import java.util.Map.Entry;

public class InventoryTemplate {

    private final String name;
    private final Size size;
    private final HashMap<Size, InventoryLine> lines;

    public InventoryTemplate(String name, Size size) {
        this.name = name;
        this.size = size;
        this.lines = new LinkedHashMap<>();

        for(int i = 0; size.getPosition() > i; i++) {
            this.lines.put(Size.fit((i + 1) * 9), new InventoryLine());
        }

        this.lines.put(size, new InventoryLine());
    }

    public String getName() {
        return name;
    }

    public InventoryLine getLine(Size size) {
        return lines.get(size);
    }

    public InventoryLine getFirstLine() {
        return lines.get(Size.ONE_LINE);
    }

    public InventoryLine getLastLine() {
        return lines.get(size);
    }

    public Collection<InventoryLine> getLines() {
        return Collections.unmodifiableCollection(lines.values());
    }

    public CustomInventory createInventory() {
        CustomInventory inventory = new CustomInventory(size, name);

        for(Entry<Size, InventoryLine> entry : lines.entrySet()) {
            for(Entry<Integer, Icon> iconEntry : entry.getValue().getIcons().entrySet()) {
                inventory.setIcon((entry.getKey().getLine() * 9) + iconEntry.getKey(), iconEntry.getValue());
            }
        }

        return inventory;
    }
}
