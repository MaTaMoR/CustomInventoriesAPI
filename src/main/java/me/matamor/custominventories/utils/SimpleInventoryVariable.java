package me.matamor.custominventories.utils;

import lombok.Getter;

public abstract class SimpleInventoryVariable implements InventoryVariable {

    @Getter
    private final String variable;

    public SimpleInventoryVariable(String variable) {
        Validate.notNull(variable, "InventoryVariable can't be null!");

        this.variable = variable;
    }
}
