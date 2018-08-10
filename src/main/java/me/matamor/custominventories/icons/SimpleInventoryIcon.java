package me.matamor.custominventories.icons;

import lombok.Getter;
import lombok.Setter;
import me.matamor.custominventories.actions.ActionHandler;
import me.matamor.custominventories.enums.ClickType;
import me.matamor.custominventories.utils.Messages;
import me.matamor.custominventories.requirements.Requirement;
import me.matamor.custominventories.utils.Validate;

import java.util.*;


public abstract class SimpleInventoryIcon implements InventoryIcon {

    private final Set<ActionHandler> actions = new HashSet<>();

    @Getter
    private final ClickType clickType;

    @Getter @Setter
    private Requirement requirement;

    @Getter
    private String requirementMessage = Messages.NO_REQUIREMENTS.get();

    @Setter
    private boolean visibilityRestricted;

    public SimpleInventoryIcon() {
        this(ClickType.BOTH_CLICKS);
    }

    public SimpleInventoryIcon(ClickType clickType) {
        Validate.notNull(clickType, "ClickType can't be null!");

        this.clickType = clickType;
    }

    @Override
    public boolean hasRequirement() {
        return this.requirement != null;
    }

    @Override
    public void setRequirementMessage(String requirementMessage) {
        Validate.notNull(requirementMessage, "Requirement message can't be null!");

        this.requirementMessage = requirementMessage;
    }

    @Override
    public boolean hasVisibilityRestricted() {
        return this.visibilityRestricted;
    }

    @Override
    public InventoryIcon addClickAction(ActionHandler... actionHandlers) {
        Validate.notNull(actionHandlers, "ActionHandlers can't be null!");

        this.actions.addAll(Arrays.asList(actionHandlers));

        return this;
    }

    @Override
    public void removeClickAction(ActionHandler... actionHandlers) {
        Validate.notNull(actionHandlers, "ActionHandler can't be null!");

        this.actions.removeAll(Arrays.asList(actionHandlers));
    }

    @Override
    public Collection<ActionHandler> getClickActions() {
        return Collections.unmodifiableSet(this.actions);
    }
}
