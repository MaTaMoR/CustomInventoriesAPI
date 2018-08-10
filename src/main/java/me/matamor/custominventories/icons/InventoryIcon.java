package me.matamor.custominventories.icons;

import me.matamor.custominventories.actions.ActionHandler;
import me.matamor.custominventories.enums.ClickType;
import me.matamor.custominventories.utils.CustomItem;
import me.matamor.custominventories.requirements.Requirement;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface InventoryIcon {

    ClickType getClickType();

    void setRequirement(Requirement requirement);

    boolean hasRequirement();

    Requirement getRequirement();

    String getRequirementMessage();

    void setRequirementMessage(String requirementMessage);

    void setVisibilityRestricted(boolean visibilityRestricted);

    boolean hasVisibilityRestricted();

    CustomItem getItem(Player player);

    InventoryIcon addClickAction(ActionHandler... actionHandlers);

    void removeClickAction(ActionHandler... actionHandlers);

    Collection<ActionHandler> getClickActions();

}
