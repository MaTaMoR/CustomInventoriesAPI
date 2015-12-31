package us.swiftex.custominventories.inventories.defaults;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.swiftex.custominventories.icons.NormalIcon;
import us.swiftex.custominventories.inventories.NormalInventory;
import us.swiftex.custominventories.utils.Utils;
import us.swiftex.custominventories.utils.Validate;
import us.swiftex.custominventories.utils.Variable;

public class GlobalInventory extends NormalInventory<NormalIcon> {

    private Inventory inventory;

    public GlobalInventory(String title, int size) {
        super(title, size);

        inventory = Bukkit.createInventory(getCustomHolder(), size().getSize(), Utils.colorize(Variable.replace(getTitle())));
    }

    @Override
    public NormalIcon setIcon(int position, NormalIcon icon) {
        Validate.isFalse(position >= size().getSize(), "Position can't be higher than Inventory size");
        Validate.notNull(icon, "Icon can't be null");

        getIcons().put(position, icon);
        inventory.setItem(position, icon.getCustomItem().build());

        return icon;
    }

    @Override
    public void removeIcon(int position) {
        Validate.isFalse(position >= size().getSize(), "Position can't be higher than Inventory size");

        getIcons().remove(position);
        inventory.setItem(position, new ItemStack(Material.AIR));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onOpen(Player player) { }

    @Override
    public void onClose(Player player) { }
}
