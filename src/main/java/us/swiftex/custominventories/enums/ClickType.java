package us.swiftex.custominventories.enums;

import org.bukkit.event.inventory.InventoryClickEvent;

public enum ClickType {

    RIGHT_CLICK,
    LEFT_CLICK,
    BOTH_CLICKS,
    UNKNOWN;

    public boolean isValid() {
        return this != UNKNOWN;
    }

    public boolean isValid(ClickType clickType) {
        if(this == UNKNOWN || clickType == UNKNOWN) return false;

        if(this == BOTH_CLICKS) {
            return true;
        } else if(this == RIGHT_CLICK && clickType == RIGHT_CLICK) {
            return true;
        } else if(this == LEFT_CLICK && clickType == LEFT_CLICK) {
            return true;
        }

        return false;
    }

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
        return (event.isRightClick() ? RIGHT_CLICK : (event.isLeftClick() ? LEFT_CLICK : UNKNOWN));
    }
}
