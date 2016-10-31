package us.swiftex.custominventories.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Messages<T> {

    private static final List<Messages<?>> items = new ArrayList<>();

    public static final SMessage ONLINE = new SMessage("Messages.Online", "&2Online&f");
    public static final SMessage OFFLINE = new SMessage("Messages.Offline", "&4Offline&f");

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
        if(set == null) {
            return;
        }

        loadedValue = set;
        config.set(getPath(), set);
    }

    public static void load(Plugin plugin) {
        plugin.getDataFolder().mkdirs();

        File file = new File(plugin.getDataFolder(), "messages.yml");
        config = YamlConfiguration.loadConfiguration(file);

        for(Messages value : values()) {
            if(config.get(value.getPath()) == null) {
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

    public static class SMessage extends Messages<String> {

        private SMessage(String valuePath, String defaultValue) {
            super(valuePath, defaultValue);
        }

        @Override
        public String get() {
            return Utils.colorize(super.get());
        }

        @Override
        public String load(FileConfiguration config) {
            return config.getString(getPath());
        }

        public String replace(String toReplace, String replacement) {
            return Utils.colorize(get().replace(toReplace, replacement));
        }

    }

    public static class LMessage extends Messages<List<String>> {

        private LMessage(String valuePath, List<String> defaultValue) {
            super(valuePath, defaultValue);
        }

        @Override
        public List<String> get() {
            return Utils.colorize(super.get());
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

            return Utils.colorize(replaced);
        }
    }
}
