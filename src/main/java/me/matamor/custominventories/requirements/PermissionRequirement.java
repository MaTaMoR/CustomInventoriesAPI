package me.matamor.custominventories.requirements;

import lombok.Getter;
import me.matamor.custominventories.permissions.IPermission;
import org.bukkit.entity.Player;

public class PermissionRequirement extends SimpleRequirement {

    @Getter
    private final IPermission permission;

    public PermissionRequirement(String message, IPermission permission) {
        super(message);

        this.permission = permission;
    }

    @Override
    public boolean hasRequirement(Player player) {
        return this.permission.hasPermission(player);
    }
}
