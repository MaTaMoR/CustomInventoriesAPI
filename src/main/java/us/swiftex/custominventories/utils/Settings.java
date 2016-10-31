package us.swiftex.custominventories.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Settings<T> {

    private static final List<Settings> settings = new ArrayList<>();
    private static FileConfiguration config;

    public static final Settings<Boolean> LOG_ON_SERVER_DOWN = new BooleanSetting("Configuration.LogOnServerDown", false);

    public abstract T load(FileConfiguration config);

    private final String path;
    private final T def;
    private T loaded;

    private Settings(String path, T def) {
        this.path = path;
        this.def = def;

        settings.add(this);
    }

    public String getPath() {
        return path;
    }

    public T getDefault() {
        return def;
    }

    public T get() {
        if(loaded == null) {
            loaded = load(config);
        }

        return loaded;
    }

    public static void load(Plugin plugin) {
        plugin.getDataFolder().mkdirs();

        File file = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(file);

        for(Settings value : settings) {
            if(config.get(value.getPath()) == null) {
                config.set(value.getPath(), value.getDefault());
            }

            value.loaded = value.load(config);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static class BooleanSetting extends Settings<Boolean> {

        private BooleanSetting(String path, Boolean def) {
            super(path, def);
        }

        @Override
        public Boolean load(FileConfiguration config) {
            return config.getBoolean(getPath());
        }
    }

    public static class StringSetting extends Settings<String> {

        private StringSetting(String path, String def) {
            super(path, def);
        }

        @Override
        public String load(FileConfiguration config) {
            return config.getString(getPath());
        }
    }

    public static class IntegerSetting extends Settings<Integer> {

        private IntegerSetting(String path, Integer def) {
            super(path, def);
        }

        @Override
        public Integer load(FileConfiguration config) {
            return config.getInt(getPath());
        }
    }

    public static class ListSetting extends Settings<List<String>> {

        private ListSetting(String path, List<String> def) {
            super(path, def);
        }

        @Override
        public List<String> load(FileConfiguration config) {
            return config.getStringList(getPath());
        }
    }
}
