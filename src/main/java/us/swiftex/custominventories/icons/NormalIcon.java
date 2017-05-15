package us.swiftex.custominventories.icons;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.swiftex.custominventories.enums.ClickType;
import us.swiftex.custominventories.permissions.IPermission;
import us.swiftex.custominventories.utils.CustomItem;

public class NormalIcon extends Icon {

    @Getter
    private final CustomItem item;

    public NormalIcon(CustomItem item) {
        this(item, ClickType.BOTH_CLICKS);
    }

    public NormalIcon(CustomItem item, ClickType clickType) {
        this(item, clickType, null);
    }

    public NormalIcon(CustomItem item, ClickType clickType, IPermission permission) {
        this(item, clickType, permission, false);
    }

    public NormalIcon(CustomItem item, ClickType clickType, IPermission permission, boolean permissionVisibility) {
        super(clickType, permission, permissionVisibility);

        this.item = item;
    }

    @Override
    public CustomItem getItem(Player player) {
        return this.item;
    }
}
