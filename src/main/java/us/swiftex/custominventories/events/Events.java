package us.swiftex.custominventories.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.enums.ClickType;
import us.swiftex.custominventories.icons.Icon;
import us.swiftex.custominventories.inventories.CustomHolder;
import us.swiftex.custominventories.inventories.CustomInventory;
import us.swiftex.custominventories.utils.CustomItem;

import java.util.List;

public class Events implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        if(event.getInventory().getHolder() instanceof CustomHolder) {
            event.setCancelled(true);

            CustomHolder customHolder = (CustomHolder) event.getInventory().getHolder();
            CustomInventory customInventory = customHolder.getCustomInventory();

            Icon icon = customInventory.getIcon(event.getRawSlot());

            if(icon == null) {
                return;
            }

            Player player = (Player) event.getWhoClicked();


            if(icon.getClickType() == ClickType.BOTH_CLICKS || (icon.getClickType() == ClickType.LEFT_CLICK && event.isLeftClick())
                    || (icon.getClickType() == ClickType.RIGHT_CLICK && event.isRightClick())) {

                for(ClickAction clickAction : icon.getClickActions()) {
                    clickAction.execute(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if(event.getInventory().getHolder() instanceof CustomHolder) {
            CustomHolder customHolder = (CustomHolder) event.getInventory().getHolder();
                         customHolder.getCustomInventory().onOpen(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if(event.getInventory().getHolder() instanceof CustomHolder) {
            CustomHolder customHolder = (CustomHolder) event.getInventory().getHolder();
            customHolder.getCustomInventory().onClose(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void itemConsumeEvent(PlayerItemConsumeEvent event) {
        if(event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        CustomItem customItem = CustomItem.toCustomItem(event.getItem());

        if(customItem.hasPotionEffects()) {
            event.setCancelled(true);

            removeOne(player);
            player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE));

            for(PotionEffect effect : customItem.getPotionEffects()) {
                player.addPotionEffect(effect, true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLaunch(ProjectileLaunchEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(event.getEntity() instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion) event.getEntity();

            CustomItem customItem = CustomItem.toCustomItem(potion.getItem());

            if(customItem.hasPotionEffects()) {
                potion.setItem(createPotion(customItem.getPotionEffects(), true));
            }
        }
    }

    public ItemStack createPotion(List<PotionEffect> effects, boolean splash) {
        ItemStack potion = new ItemStack(Material.POTION);

        if(effects.size() == 0) {
            return potion;
        }

        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

        for(PotionEffect effect : effects) {
            potionMeta.addCustomEffect(effect, true);
        }

        potion.setItemMeta(potionMeta);
        setPotion(potion, PotionType.getByEffect(effects.get(0).getType()), true);

        return potion;
    }

    public static void setPotion(ItemStack itemStack, PotionType potionType, boolean splash) {
        if(itemStack == null) {
            return;
        }

        Potion potion = new Potion(potionType);
        potion.setSplash(splash);

        potion.apply(itemStack);
    }

    private void removeOne(Player player) {
        if(player.getItemInHand() == null) {
            return;
        }

        ItemStack itemStack = player.getItemInHand();

        if(itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        } else {
            itemStack = null;
        }

        player.setItemInHand(itemStack);
    }
}
