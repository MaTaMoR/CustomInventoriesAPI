package us.swiftex.custominventories.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> color(List<String> list) {
        List<String> coloredList = new ArrayList<>();

        if(list == null) {
            return coloredList;
        }

        for(String row : list) {
            coloredList.add(color(row));
        }

        return coloredList;
    }

    private static final DecimalFormat format = new DecimalFormat("##");

    public static String format(double value) {
        return format.format(value);
    }

    public static boolean isClassLoaded(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Map<String, Object> asMap(Object object) {
        if(object instanceof ConfigurationSection) {
            return ((ConfigurationSection) object).getValues(false);
        } else if(object instanceof Map) {
            return ((Map<String, Object>) object);
        }

        return null;
    }
}
