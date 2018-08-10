package me.matamor.custominventories.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionUtils {

    public static String getDescription(PotionEffect effect) {
        return ChatColor.GRAY + getName(effect.getType()) + " " + new RomanNumeral(effect.getAmplifier() + 1).toString() + " (" + secondsToString((effect.getDuration() / 20)) + ")";
    }

    private static String getName(PotionEffectType effectType) {
        StringBuilder stringBuilder = new StringBuilder();

        for(String args : effectType.getName().split("_")) {
            stringBuilder.append(args.substring(0, 1).toUpperCase()).append(args.substring(1, args.length()).toLowerCase()).append(" ");
        }

        return stringBuilder.toString().trim();
    }

    private static String secondsToString(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}
