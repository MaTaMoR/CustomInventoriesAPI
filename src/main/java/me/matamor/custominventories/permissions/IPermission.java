package me.matamor.custominventories.permissions;

import lombok.Getter;
import org.bukkit.permissions.Permissible;
import me.matamor.custominventories.utils.Validate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class IPermission {

    private final Set<IPermission> children = new HashSet<>();

    @Getter
    private final String permission;

    @Getter
    private IPermission parent;

    public IPermission(String permission) {
        this(permission, null);
    }

    public IPermission(String permission, IPermission parent) {
        Validate.notNull(permission, "Permission can't be null!");

        this.permission = permission;

        setParent(parent);
    }

    public void setParent(IPermission parent) {
        if (this.parent != null) {
            this.parent.removeChild(this);
        }

        this.parent = parent;

        if (this.parent != null) {
            this.parent.children.add(this);
        }
    }

    public void addChild(IPermission... children) {
        Validate.notNull(children, "IPermission can't be null!");

        for (IPermission child : children) {
            child.setParent(this);
        }
    }

    public void removeChild(IPermission... children) {
        Validate.notNull(children, "IPermission can't be null!");

        for (IPermission child : children) {
            if (child.getParent() == this) {
                child.setParent(null);
            }
        }
    }

    public Set<IPermission> getChildren() {
        return Collections.unmodifiableSet(this.children);
    }

    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(this.permission) || this.parent != null && this.parent.hasPermission(permissible);
    }
}
