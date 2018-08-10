package me.matamor.custominventories.inventories;

import me.matamor.custominventories.utils.BasicTaskHandler;

public interface CustomUpdatingInventory {

    BasicTaskHandler getTaskHandler();

    long getTicks();

    default boolean shouldUpdate() {
        return true;
    }

    default void onUpdate() {

    }

}
