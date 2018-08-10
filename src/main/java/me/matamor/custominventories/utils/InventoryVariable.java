package me.matamor.custominventories.utils;

import org.bukkit.entity.Player;

public interface InventoryVariable {

    String getVariable();

    String getReplacement(Player player);

}
