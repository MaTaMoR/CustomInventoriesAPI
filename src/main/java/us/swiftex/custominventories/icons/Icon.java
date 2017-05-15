package us.swiftex.custominventories.icons;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ActionHandler;
import us.swiftex.custominventories.enums.ClickType;
import us.swiftex.custominventories.permissions.IPermission;
import us.swiftex.custominventories.utils.CustomItem;

import java.util.*;

public abstract class Icon {

    public abstract CustomItem getItem(Player player);

    private final List<ActionHandler> actions = new ArrayList<>();

    @Getter
    private final ClickType type;

    @Getter @Setter
    private IPermission permission;

    @Getter @Setter
    private boolean permissionVisibility;

    @Getter @Setter
    private String noPermissionMessage = "&cYou don't have permissions to do that!";

    public Icon() {
        this(ClickType.BOTH_CLICKS);
    }

    public Icon(ClickType type) {
        this(type, null);
    }

    public Icon(ClickType type, IPermission permission) {
        this(type, permission, false);
    }

    public Icon(ClickType type, IPermission permission, boolean permissionVisibility) {
        this.type = type;
        this.permission = permission;
        this.permissionVisibility = permissionVisibility;
    }
    public boolean havePermission() {
        return this.permission != null;
    }

    public boolean canSee(Player player) {
        return !this.permissionVisibility || this.permission == null || this.permission.hasPermission(player);
    }

    public Icon addClickAction(ActionHandler... actionHandlers) {
        Collections.addAll(this.actions, actionHandlers);
        return this;
    }

    public List<ActionHandler> getClickActions() {
        return Collections.unmodifiableList(this.actions);
    }
}
