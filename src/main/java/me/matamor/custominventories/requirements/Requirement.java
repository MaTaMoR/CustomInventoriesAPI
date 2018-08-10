package me.matamor.custominventories.requirements;

import org.bukkit.entity.Player;

public interface Requirement {

    String getRequirementMessage();

    boolean hasRequirement(Player player);

}
