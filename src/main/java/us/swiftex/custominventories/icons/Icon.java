package us.swiftex.custominventories.icons;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.CustomInventoriesMain;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.enums.ClickType;
import us.swiftex.custominventories.permissions.IPermission;
import us.swiftex.custominventories.utils.CustomItem;
import us.swiftex.custominventories.utils.Validate;

import java.util.*;

public abstract class Icon {

    private final Set<ClickAction> actions = new HashSet<>();

    private ClickType clickType;
    private IPermission permission;
    private boolean visibilityRestricted;
    private String noPermissionMessage;

    public Icon() {
        this(ClickType.BOTH_CLICKS);
    }

    public Icon(ClickType clickType) {
        this.clickType = clickType;
        this.permission = null;
        this.visibilityRestricted = false;
        this.noPermissionMessage = "&cYou don't have permission for use this Icon";
    }

    public void setClickType(ClickType clickType) {
        Validate.notNull(clickType, "ClickType can't be null");

        this.clickType = clickType;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public void setPermission(IPermission permission) {
        Validate.notNull(permission, "Permission can't be null");

        this.permission = permission;
    }

    public IPermission getPermission() {
        return permission;
    }

    public boolean hasPermission(Player player) {
        return permission != null && permission.hasPermission(player);
    }

    public void setVisibilityRestricted(boolean visibilityRestricted) {
        this.visibilityRestricted = visibilityRestricted;
    }

    public boolean isVisibilityRestricted() {
        return visibilityRestricted;
    }

    public void setNoPermissionMessage(String noPermissionMessage) {
        Validate.notNull(noPermissionMessage, "NoPermissionMessage can't be null");

        this.noPermissionMessage = noPermissionMessage;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public boolean canSee(Player player) {
        return !visibilityRestricted || permission.hasPermission(player);
    }

    public Icon addClickAction(ClickAction clickAction) {
        Validate.notNull(clickAction, "ClickAction can't be null");

        actions.add(clickAction);
        return this;
    }

    public Icon removeClickAction(ClickAction clickAction) {
        Validate.notNull(clickAction, "ClickAction can't be null");

        actions.remove(clickAction);
        return this;
    }

    public Set<ClickAction> getClickActions() {
        return Collections.unmodifiableSet(actions);
    }
}
