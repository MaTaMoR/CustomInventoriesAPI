package me.matamor.custominventories.utils.parent;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public class PluginParent extends Parent<JavaPlugin> {

    public PluginParent(JavaPlugin object) {
        super(object);
    }

    @Override
    public File getDataFolder() {
        return get().getDataFolder();
    }

    @Override
    public InputStream getResource(String name) {
        return get().getResource(name);
    }

    @Override
    public void saveResource(String name, boolean value) {
        get().saveResource(name, value);
    }

    @Override
    public Logger getLogger() {
        return get().getLogger();
    }
}
