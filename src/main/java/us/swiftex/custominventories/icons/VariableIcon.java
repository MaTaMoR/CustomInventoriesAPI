package us.swiftex.custominventories.icons;

import org.bukkit.entity.Player;
import us.swiftex.custominventories.actions.ClickAction;
import us.swiftex.custominventories.utils.CustomItem;
import us.swiftex.custominventories.utils.Validate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class VariableIcon extends Icon {

    private final Set<ClickAction> actions = new HashSet<>();

    public abstract CustomItem getCustomItem(Player player);

    @Override
    public VariableIcon addClickAction(ClickAction clickAction) {
        Validate.notNull(clickAction, "ClickAction can't be null");

        actions.add(clickAction);
        return this;
    }

    @Override
    public VariableIcon removeClickAction(ClickAction clickAction) {
        Validate.notNull(clickAction, "ClickAction can't be null");

        actions.remove(clickAction);
        return this;
    }

    @Override
    public Set<ClickAction> getClickActions() {
        return Collections.unmodifiableSet(actions);
    }
}
