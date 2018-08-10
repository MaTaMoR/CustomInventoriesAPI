package me.matamor.custominventories.utils;

import org.bukkit.entity.Player;
import me.matamor.custominventories.CustomInventories;

import java.util.*;
import java.util.logging.Level;

public abstract class InventoryVariables {

    private static final Map<String, InventoryVariable> INVENTORY_VARIABLES = new HashMap<>();

    public static InventoryVariable register(InventoryVariable variable) {
        Validate.notNull(variable, "InventoryVariable can't be null!");
        Validate.notNull(variable.getVariable(), "InventoryVariable's variable can't be null!");

        INVENTORY_VARIABLES.put(variable.getVariable(), variable);

        return variable;
    }

    public static void unregister(InventoryVariable variable) {
        Validate.notNull(variable, "InventoryVariable can't be null");

        INVENTORY_VARIABLES.remove(variable.getVariable());
    }

    public static String replace(String text) {
        return replace(text, null);
    }

    public static String replace(String text, Player player) {
        Validate.notNull(text, "Text can't be null");

        for (Map.Entry<String, InventoryVariable> entry : INVENTORY_VARIABLES.entrySet()) {
            if (!text.contains(entry.getKey())) continue;

            try {
                String replacement = entry.getValue().getReplacement(player);
                if (replacement != null) {
                    text = text.replace(entry.getKey(), replacement);
                }
            } catch (Exception e) {
                CustomInventories.getPlugin().getLogger().log(Level.SEVERE, "Error while getting replacement for variable: " + entry.getKey(), e);
            }
        }

        return text;
    }

    public static Collection<InventoryVariable> getVariables() {
        return Collections.unmodifiableCollection(INVENTORY_VARIABLES.values());
    }
}
