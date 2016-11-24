package us.swiftex.custominventories.enums;

import org.bukkit.event.inventory.InventoryClickEvent;

public enum ClickType {

    RIGHT_CLICK,
    LEFT_CLICK,
    BOTH_CLICKS;

    public boolean isValid(InventoryClickEvent event) {
        switch (this) {
            case RIGHT_CLICK:
                return event.isRightClick();
            case LEFT_CLICK:
                return event.isLeftClick();
            case BOTH_CLICKS:
                return true;
            default:
                return false;
        }
    }

    public static ClickType matchClick(InventoryClickEvent event) {
        return (event.isRightClick() ? RIGHT_CLICK : LEFT_CLICK);
    }
}
