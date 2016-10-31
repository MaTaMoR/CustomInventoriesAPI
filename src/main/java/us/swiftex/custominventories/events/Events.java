package us.swiftex.custominventories.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import us.swiftex.custominventories.actions.ActionHandler;
import us.swiftex.custominventories.enums.ClickType;
import us.swiftex.custominventories.icons.Icon;
import us.swiftex.custominventories.inventories.CustomHolder;
import us.swiftex.custominventories.inventories.CustomInventory;
import us.swiftex.custominventories.utils.Utils;

public class Events implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if(event.getInventory().getHolder() instanceof CustomHolder) {
            event.setCancelled(true);

            if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

            CustomHolder customHolder = (CustomHolder) event.getInventory().getHolder();
            CustomInventory customInventory = customHolder.getCustomInventory();

            Icon icon = customInventory.getIcon(event.getRawSlot());
            if(icon == null) return;

            Player player = (Player) event.getWhoClicked();

            if(icon.getType() == ClickType.BOTH_CLICKS || (icon.getType() == ClickType.LEFT_CLICK && event.isLeftClick())
                    || (icon.getType() == ClickType.RIGHT_CLICK && event.isRightClick())) {
                if(!icon.havePermission() || icon.getPermission().hasPermission(player)) {
                    for (ActionHandler actionHandler : icon.getClickActions()) {
                        actionHandler.handle(customInventory, player, (event.isRightClick() ? ClickType.RIGHT_CLICK : ClickType.LEFT_CLICK), event.isShiftClick(), event);
                    }
                } else {
                    player.sendMessage(Utils.colorize(icon.getNoPermissionMessage()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if(event.getInventory().getHolder() instanceof CustomHolder) {
            ((CustomHolder) event.getInventory().getHolder()).getCustomInventory().onOpen(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if(event.getInventory().getHolder() instanceof CustomHolder) {
            ((CustomHolder) event.getInventory().getHolder()).getCustomInventory().onClose(player);
        }
    }
}
