package me.matamor.custominventories.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Messages<T> {

    private static final List<Messages<?>> items = new ArrayList<>();

    public static final Message NO_REQUIREMENTS = new Message("Messages.NoRequirements", "&cYou don't have permissions to do that!");
    public static final Message ONLINE = new Message("Messages.Online", "&2Online&f");
    public static final Message OFFLINE = new Message("Messages.Offline", "&4Offline&f");

    private static FileConfiguration config;

    public abstract T load(FileConfiguration config);

    private final T defaultValue;
    private final String valuePath;

    private T loadedValue;

    private Messages(String valuePath, T defaultValue) {
        this.valuePath = valuePath;
        this.defaultValue = defaultValue;

        items.add(this);
    }

    public T getDefault() {
        return defaultValue;
    }

    public String getPath() {
        return valuePath;
    }

    public T get() {
        return loadedValue;
    }

    public void set(T set) {
        if (set == null) {
            return;
        }

        loadedValue = set;
        config.set(getPath(), set);
    }

    public static void load(Plugin plugin) {
        plugin.getDataFolder().mkdirs();

        File file = new File(plugin.getDataFolder(), "messages.yml");
        config = YamlConfiguration.loadConfiguration(file);

        for (Messages value : values()) {
            if (config.get(value.getPath()) == null) {
                config.set(value.getPath(), value.getDefault());
            }

            value.loadedValue = value.load(config);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Messages[] values() {
        return items.toArray(new Messages[items.size()]);
    }

    public static class Message extends Messages<String> {

        private Message(String valuePath, String defaultValue) {
            super(valuePath, defaultValue);
        }

        @Override
        public String get() {
            return Utils.color(super.get());
        }

        @Override
        public String load(FileConfiguration config) {
            return config.getString(getPath());
        }

        public String replace(String toReplace, String replacement) {
            return Utils.color(get().replace(toReplace, replacement));
        }

    }

    public static class ListMessage extends Messages<List<String>> {

        private ListMessage(String valuePath, List<String> defaultValue) {
            super(valuePath, defaultValue);
        }

        @Override
        public List<String> get() {
            return Utils.color(super.get());
        }

        @Override
        public List<String> load(FileConfiguration config) {
            return config.getStringList(getPath());
        }

        public List<String> replace(String toReplace, String replacement) {
            List<String> replaced = new ArrayList<>();

            for(String string : get()) {
                replaced.add(string.replace(toReplace, replacement));
            }

            return Utils.color(replaced);
        }
    }
}
