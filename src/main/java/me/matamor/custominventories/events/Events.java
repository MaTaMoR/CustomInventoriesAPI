package me.matamor.custominventories.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.matamor.custominventories.actions.ActionHandler;
import me.matamor.custominventories.inventories.CustomInventory;
import me.matamor.custominventories.inventories.CustomInventoryHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import me.matamor.custominventories.CustomInventories;
import me.matamor.custominventories.enums.ClickType;
import me.matamor.custominventories.icons.InventoryIcon;
import me.matamor.custominventories.utils.Utils;

import java.util.*;

@AllArgsConstructor
public class Events implements Listener {

    @Getter
    private final CustomInventories plugin;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;

        //So im using a custom holder to hold the custom inventory, so the first thing is check if the holder is our custom one
        if (event.getView().getTopInventory().getHolder() instanceof CustomInventoryHolder) {
            CustomInventory customInventory = ((CustomInventoryHolder) event.getView().getTopInventory().getHolder()).getCustomInventory();

            if (event.getClickedInventory() == null) { //Clicked out of the Inventory (like dropping and item)
                event.setCancelled(true);
            } else {
                if (event.getClickedInventory().getHolder() instanceof CustomInventoryHolder) { //If the click is in our inventory then cancel directly
                    event.setCancelled(true);

                    if (!isValid(event.getCurrentItem())) return; //If the clicked item is null then just return

                    InventoryIcon icon = customInventory.getIcon(event.getRawSlot()); //Check if the clicked slot has a InventoryIcon
                    if (icon == null) return;
         
                    Player player = (Player) event.getWhoClicked();
                    ClickType clickType = ClickType.matchClick(event); //Get the event click type

                    if (icon.getClickType().isValid(clickType)) { //Check if the click type of the InventoryIcon is valid
                        if (!icon.hasRequirement() || icon.getRequirement().hasRequirement(player)) { //Check if player has permissions to use this InventoryIcon
                            for (ActionHandler actionHandler : icon.getClickActions()) { //Loop thought the ActionHandler and call em
                                if (actionHandler == null) return; //The ActionHandler might contain null ?

                                actionHandler.handle(customInventory, player, clickType, event.isShiftClick(), event);
                            }
                        } else {
                            player.sendMessage(Utils.color(icon.getRequirementMessage())); //No permissions to use it
                        }
                    }
                } else if (customInventory.canInteractInventory()) { //This new boolean is to prevent players from interacting with their own inventory
                    if (event.isShiftClick() && isValid(event.getCurrentItem())) { //If the event is shit and the player has a valid item on hand then cancel
                        event.setCancelled(true);
                    } else if (event.getAction() == InventoryAction.COLLECT_TO_CURSOR && isValid(event.getCursor())) { //Check if the collect will affect top inventory
                        if (isValid(event.getCurrentItem())) return; //If the current item is valid then it won't collect top inventory items

                        stack(event.getCursor(), event.getClickedInventory()); //Fix collection only joining bottom inventory items

                        event.setCancelled(true);
                    } else if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) { //Listen for when you use Ctrl + Number
                        if (event.getRawSlot() == event.getSlot()) event.setCancelled(true); //Swap on top inventory
                    } else if (event.getAction() == InventoryAction.UNKNOWN) { //IDK what might cause this, but i cancel it for security
                        event.setCancelled(true);
                    }
                } else { //Cancel event if player can't interact with it's inventory
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrag(final InventoryDragEvent event) {
        if (event.isCancelled()) return;

        final InventoryView view = event.getView();

        //Check if the holder is our custom one
        if (view.getTopInventory().getHolder() instanceof CustomInventoryHolder) {
            final Set<Integer> slots = new HashSet<>();
            for (int rawSlot : event.getRawSlots()) { //Remove all the slots from the top inventory
                if (event.getInventorySlots().contains(rawSlot)) continue;

                slots.add(rawSlot);
            }

            if (slots.size() == event.getRawSlots().size()) return; //If the size is the same it means the drag wasn't on the top inventory, then let minecraft handle it

            event.setCancelled(true);

            if (slots.isEmpty()) return; //If there are no slots on the bottom inventory then just return

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                divide(event.getOldCursor(), view, slots, event.getType()); //Divide the item but only on the slots from the bottom inventory
            }, 1); //It has to be set 1 tick after
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOpen(InventoryOpenEvent event) {
        if (event.isCancelled()) return;

        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();

            if (event.getInventory().getHolder() instanceof CustomInventoryHolder) { //Call the void for when the inventory is open
                CustomInventory customInventory = ((CustomInventoryHolder) event.getInventory().getHolder()).getCustomInventory();

                customInventory.onOpen(player);

                customInventory.getViewers().add(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();

            if (event.getInventory().getHolder() instanceof CustomInventoryHolder) { //Call the void for when the inventory is closed
                CustomInventory customInventory = ((CustomInventoryHolder) event.getInventory().getHolder()).getCustomInventory();

                customInventory.getViewers().remove(player);

                customInventory.onClose(player);
            }
        }
    }

    //A comparator so when stacking the items it will take fist the similar items with less amount so it will stack first items with for example 1 amount instead of stacking first big stacks
    private final Comparator<Pair<ItemStack, Integer>> comparator = (pairA, pairB) -> {
        ItemStack a = pairA.getKey();
        ItemStack b = pairB.getKey();

        return (isValid(a, b) ? (a.getAmount() == b.getAmount() ? (pairA.getValue() < pairB.getValue() ? 1 : -1) : a.getAmount() > b.getAmount() ? 1 : -1) :
                (!isValid(a) && !isValid(b) ? 0 : (isValid(a) && !isValid(b) ? 1 : -1)));
    };

    private boolean isValid(ItemStack... itemStacks) { //A simple validation for the item
        for (ItemStack itemStack : itemStacks)
            if (itemStack == null || itemStack.getType() == Material.AIR) return false;

        return true;
    }

    //Stack the item using the items from the inventory
    private void stack(ItemStack itemStack, Inventory inventory) {
        if (itemStack.getAmount() == itemStack.getMaxStackSize()) return;

        ItemStack[] contents = inventory.getContents();

        List<Pair<ItemStack, Integer>> filtered = filter(itemStack, contents); //Get only similar items
        filtered.sort(this.comparator); //Use the comparator

        for (Pair<ItemStack, Integer> entry : filtered) { //The pair is needed to know the position of the item in the inventory
            ItemStack content = entry.getKey();
            int needed = itemStack.getMaxStackSize() - itemStack.getAmount();

            if (content.getAmount() > needed) { //If the amount is higher than the needed one then just set it
                itemStack.setAmount(itemStack.getAmount() + needed);
                content.setAmount(content.getAmount() - needed);
                return;
            } else if (content.getAmount() == needed) { //If the amount is the same set it and remove the item
                itemStack.setAmount(itemStack.getAmount() + needed);
                inventory.setItem(entry.getValue(), null);
                return;
            } else if (content.getAmount() < needed) { //If the amount is less add it and remove the item
                itemStack.setAmount(itemStack.getAmount() + content.getAmount());
                inventory.setItem(entry.getValue(), null);
            }
        }
    }

    //Simply just well all the similar items and keep it's position
    private List<Pair<ItemStack, Integer>> filter(ItemStack itemStack, ItemStack[] contents) {
        List<Pair<ItemStack, Integer>> list = new ArrayList<>();

        for (int i = 0; contents.length > i; i++) {
            ItemStack content = contents[i];
            if (itemStack == content) continue;

            if (itemStack.isSimilar(content)) list.add(new Pair<>(content, i));
        }

        return list;
    }

    //This is basically meant to when a player tries to drag an item into top inventory just divide it on the slots from the bottom inventory
    private void divide(ItemStack itemStack, InventoryView view, Set<Integer> slots, DragType dragType) {
        int amount = (dragType == DragType.SINGLE ? 1 : itemStack.getAmount() / slots.size()); //Get the amount that will be divided for each item
        int rest = (dragType == DragType.SINGLE ? itemStack.getAmount() - slots.size() : amount % slots.size()); //Get the amount that is left to set to the item divided

        for (int slot : slots) {
            ItemStack content = view.getItem(slot);
            if (isValid(content)) { //Check if there's already an similar item there
                if (amount + content.getAmount() > content.getMaxStackSize()) { //If the amount is higher than the set item amount then just add what we can add the rest to the divided item
                    rest += (amount + content.getAmount()) - content.getMaxStackSize();
                    content.setAmount(content.getMaxStackSize());
                } else { //If the amount fits right then just set it
                    content.setAmount(content.getAmount() + amount);
                }
            } else { //If there's no item then just set a similar one
                content = itemStack.clone();
                content.setAmount(amount);

                view.setItem(slot, content);
            }
        }

        itemStack.setAmount(rest); //Set the rest
        view.setCursor((itemStack.getAmount() == 0 ? null : itemStack)); //If the amount is zero then just remove the item if not set it
    }

    private class Pair<K, V> { //A simply object pair, like a map Entry

        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
