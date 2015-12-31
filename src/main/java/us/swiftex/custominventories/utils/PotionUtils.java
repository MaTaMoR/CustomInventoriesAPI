package us.swiftex.custominventories.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionUtils {

    private static class Node {
        private static final String
                AMPLIFIER_SPLIT = ":",
                DURATION_SPLIT = "-",
                EFFECT_SPLIT = ",";
    }

    private static String toString(PotionEffect... effects) {
        if(effects == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for(PotionEffect effect : effects) {
            stringBuilder.append(effect.getType().getName()).append(Node.AMPLIFIER_SPLIT).
                append(effect.getAmplifier()).append(Node.DURATION_SPLIT).
                append(effect.getDuration()).append(Node.EFFECT_SPLIT);
        }

        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
    }

    private static PotionEffect fromString(String string) {
        int duration = 0;
        int amplifier = 0;

        if (string.contains(Node.DURATION_SPLIT)) {
            String[] args = string.split(Node.DURATION_SPLIT);

            try {
                duration = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {

            }

            string = args[0];
        }

        if (string.contains(Node.AMPLIFIER_SPLIT)) {
            String[] args = string.split(Node.AMPLIFIER_SPLIT);

            try {
                amplifier = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {

            }

            string = args[0];
        }

        PotionEffectType effectType = PotionEffectType.getByName(string);
        if (effectType == null) {
            return null;
        }

        return new PotionEffect(effectType, duration, amplifier);
    }

    public static ItemStack apply(ItemStack itemStack, PotionEffect... effects) {
        if(itemStack == null || itemStack.getType() != Material.POTION) {
            return itemStack;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = (itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<String>());

        if(lore.size() > 0) {
            lore.add("");
        }

        for(PotionEffect effect : effects) {
            lore.add(getDescription(effect));
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return ItemReflections.setString(itemStack, "Effects", toString(effects));
    }

    public static boolean hasEffects(ItemStack itemStack) {
        return !(itemStack == null || itemStack.getType() != Material.POTION) && ItemReflections.hasKey(itemStack, "Effects");
    }

    public static List<PotionEffect> getEffects(ItemStack itemStack) {
        if(itemStack == null || itemStack.getType() != Material.POTION) {
            return new ArrayList<>();
        }

        List<PotionEffect> effects = new ArrayList<>();

        if(ItemReflections.hasKey(itemStack, "Effects")) {
            String string = ItemReflections.getString(itemStack, "Effects");

            if(string.contains(Node.EFFECT_SPLIT)) {
                for (String arg : string.split(Node.EFFECT_SPLIT)) {
                    PotionEffect effect = fromString(arg);

                    if (effect == null) {
                        continue;
                    }

                    effects.add(effect);
                }
            } else {
                PotionEffect effect = fromString(string);

                if(effect == null) {
                    return effects;
                }

                effects.add(effect);
            }
        }

        return effects;
    }

    private static String getDescription(PotionEffect effect) {
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
