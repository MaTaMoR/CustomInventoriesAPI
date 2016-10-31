package us.swiftex.custominventories.utils;

import us.swiftex.custominventories.icons.Icon;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InventoryLine {

    private final Map<Integer, Icon> icons;

    public InventoryLine() {
        this.icons = new HashMap<>();
    }

    public void setIcon(int position, Icon icon) {
        if(position < 0 || position > 8) {
            throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + 9);
        }

        icons.put(position, icon);
    }

    public Icon getIcon(int position) {
        if(position < 0 || position > icons.size()) {
            throw new IndexOutOfBoundsException("Index: " + position + ", Size: " + icons.size());
        }

        return icons.get(position);
    }

    public Map<Integer, Icon> getIcons() {
        return Collections.unmodifiableMap(icons);
    }

    public int size() {
        return icons.size();
    }
}
