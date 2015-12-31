package us.swiftex.custominventories.icons;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.enums.ClickType;
import us.swiftex.custominventories.utils.CustomItem;
import us.swiftex.custominventories.utils.Validate;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NormalIcon extends VariableIcon {

    private final Set<ClickAction> actions = new HashSet<>();

    private CustomItem customItem;

    public NormalIcon(CustomItem customItem) {
        this(customItem, ClickType.BOTH_CLICKS);
    }

    public NormalIcon(CustomItem customItem, ClickType clickType) {
        Validate.notNull(clickType, "CustomItem can't be null");

        this.customItem = customItem;
    }

    public void setCustomItem(CustomItem customItem) {
        Validate.notNull(customItem, "CustomItem can't be null");

        this.customItem = customItem;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

    @Override
    public CustomItem getCustomItem(Player player) {
        return customItem;
    }

    @Override
    public NormalIcon addClickAction(ClickAction clickAction) {
        Validate.notNull(clickAction, "ClickAction can't be null");

        actions.add(clickAction);
        return this;
    }

    @Override
    public NormalIcon removeClickAction(ClickAction clickAction) {
        Validate.notNull(clickAction, "ClickAction can't be null");

        actions.remove(clickAction);
        return this;
    }

    @Override
    public Set<ClickAction> getClickActions() {
        return Collections.unmodifiableSet(actions);
    }
}
