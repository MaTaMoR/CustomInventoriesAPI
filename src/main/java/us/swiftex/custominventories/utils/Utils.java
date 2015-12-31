package us.swiftex.custominventories.utils;

import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String colorize(String text) {
        if(text == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> colorize(List<String> list) {
        List<String> coloredList = new ArrayList<>();

        if(list == null) {
            return coloredList;
        }

        for(String row : list) {
            coloredList.add(colorize(row));
        }

        return coloredList;
    }

    private static final DecimalFormat format = new DecimalFormat("##");

    public static String format(double value) {
        return format.format(value);
    }
}
