package us.swiftex.custominventories.icons;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ActionHandler;
import us.swiftex.custominventories.enums.ClickType;
import us.swiftex.custominventories.permissions.IPermission;
import us.swiftex.custominventories.utils.CustomItem;

import java.util.*;

public abstract class Icon {

    public abstract CustomItem getItem(Player player);

    private final Set<ActionHandler> actions = new HashSet<>();

    private final ClickType clickType;
    private IPermission permission;
    private boolean permissionVisibility;
    private String noPermissionMessage = "&cYou don't have permissions to do that";

    public Icon() {
        this(ClickType.BOTH_CLICKS);
    }

    public Icon(ClickType clickType) {
        this(clickType, null);
    }

    public Icon(ClickType clickType, IPermission permission) {
        this(clickType, permission, false);
    }

    public Icon(ClickType clickType, IPermission permission, boolean permissionVisibility) {
        this.clickType = clickType;
        this.permission = permission;
        this.permissionVisibility = permissionVisibility;
    }

    public ClickType getType() {
        return clickType;
    }

    public boolean havePermission() {
        return permission != null;
    }

    public IPermission getPermission() {
        return permission;
    }

    public void setPermission(IPermission permission) {
        this.permission = permission;
    }

    public boolean isPermissionVisibility() {
        return permissionVisibility;
    }

    public void setPermissionVisibility(boolean permissionVisibility) {
        this.permissionVisibility = permissionVisibility;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public void setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = noPermissionMessage;
    }

    public boolean canSee(Player player) {
        return !permissionVisibility || permission == null || permission.hasPermission(player);
    }

    public Icon addClickAction(ActionHandler... actionHandlers) {
        Collections.addAll(actions, actionHandlers);

        return this;
    }

    public Set<ActionHandler> getClickActions() {
        return Collections.unmodifiableSet(actions);
    }
}
