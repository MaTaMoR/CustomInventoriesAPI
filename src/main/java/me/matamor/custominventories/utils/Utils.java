package me.matamor.custominventories.utils;

import me.matamor.custominventories.utils.server.ServerVariables;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> color(List<String> list) {
        if (list == null) {
            return Collections.emptyList();
        }

        return list.stream().map(Utils::color).collect(Collectors.toList());
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
        if (object instanceof ConfigurationSection) {
            return ((ConfigurationSection) object).getValues(false);
        } else if(object instanceof Map) {
            return ((Map<String, Object>) object);
        }

        return null;
    }

    public static String fullFormat(String text, Player player) {
        text = Utils.color(text);
        text = InventoryVariables.replace(text, player);
        text = ServerVariables.replace(text);

        return text;
    }
}
