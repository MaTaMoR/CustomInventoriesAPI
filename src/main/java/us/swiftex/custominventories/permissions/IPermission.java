package us.swiftex.custominventories.permissions;

import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class IPermission {

    private final Set<IPermission> children = new HashSet<>();

    private final String permission;

    private IPermission parent;

    public IPermission(String permission) {
        this(permission, null);
    }

    public IPermission(String permission, IPermission parent) {
        this.permission = permission;
        this.parent = parent;
    }

    public String getPermission() {
        return permission;
    }

    public void setParent(IPermission parent) {
        if(this.parent != null) {
            this.parent.removeChild(this);
        }

        this.parent = parent;
        if(this.parent != null) {
            this.parent.children.add(this);
        }
    }

    public IPermission getParent() {
        return parent;
    }

    public Set<IPermission> getChilden() {
        return Collections.unmodifiableSet(children);
    }

    public void addChild(IPermission... children) {
        for(IPermission child : children) {
            setParent(child);
        }
    }

    public boolean removeChild(IPermission parent) {
        return children.remove(parent);
    }

    public boolean hasPermission(Permissible permissible) {
        return permissible.hasPermission(permission) || this.parent != null && this.parent.hasPermission(permissible);
    }
}
