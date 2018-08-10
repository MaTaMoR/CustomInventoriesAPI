package me.matamor.custominventories.utils;

import me.matamor.custominventories.inventories.CustomInventory;

public interface InventoryProvider<T extends CustomInventory> {

    T newInstance();

}
