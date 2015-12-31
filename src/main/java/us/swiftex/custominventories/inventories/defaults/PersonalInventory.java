package us.swiftex.custominventories.inventories.defaults;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import us.swiftex.custominventories.icons.Icon;
import us.swiftex.custominventories.icons.NormalIcon;
import us.swiftex.custominventories.icons.VariableIcon;
import us.swiftex.custominventories.inventories.CustomInventory;
import us.swiftex.custominventories.inventories.NormalInventory;
import us.swiftex.custominventories.inventories.PlayerInventory;
import us.swiftex.custominventories.utils.Utils;
import us.swiftex.custominventories.utils.Validate;
import us.swiftex.custominventories.utils.Variable;

import java.util.Map.Entry;

public class PersonalInventory extends PlayerInventory<VariableIcon> {

    public PersonalInventory(String title, int size) {
        super(title, size);
    }

    @Override
    public VariableIcon setIcon(int position, VariableIcon icon) {
        Validate.isFalse(position > size().getSize(), "Position can't be higher than Inventory size");
        Validate.notNull(icon, "Icon can't be null");

        getIcons().put(position, icon);

        return icon;
    }

    @Override
    public void removeIcon(int position) {
        Validate.isFalse(position >= size().getSize(), "Position can't be higher than Inventory size");

        getIcons().remove(position);
    }

    public Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(getCustomHolder(), size().getSize(), Utils.colorize(Variable.replace(getTitle(), player)));

        for(Entry<Integer, VariableIcon> entry : getIcons().entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().getCustomItem(player).build(player));
        }

        return inventory;
    }
}
