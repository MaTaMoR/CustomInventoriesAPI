package me.matamor.custominventories.utils;

import org.bukkit.enchantments.Enchantment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MinecraftEnchantments {

    private static Map<String, Enchantment> enchantments;
    private static Map<Enchantment, String> names;

    static {
        enchantments = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        names = new LinkedHashMap<>();

        enchantments.put("efficiency", Enchantment.DIG_SPEED);
        enchantments.put("silk_touch", Enchantment.SILK_TOUCH);
        enchantments.put("unbreaking", Enchantment.DURABILITY);
        enchantments.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchantments.put("luck_of_the_sea", Enchantment.LURE);
        enchantments.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantments.put("fire_protection", Enchantment.PROTECTION_FIRE);
        enchantments.put("feather_falling", Enchantment.PROTECTION_FALL);
        enchantments.put("blast_protection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.put("projectile_protection", Enchantment.PROTECTION_PROJECTILE);
        enchantments.put("respiration", Enchantment.OXYGEN);
        enchantments.put("aqua_affinity", Enchantment.WATER_WORKER);
        enchantments.put("thorns", Enchantment.THORNS);
        enchantments.put("depth_strider", Enchantment.DEPTH_STRIDER);
        enchantments.put("sharpness", Enchantment.DAMAGE_ALL);
        enchantments.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchantments.put("bane_of_arthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchantments.put("knockback", Enchantment.KNOCKBACK);
        enchantments.put("fire_aspect", Enchantment.FIRE_ASPECT);
        enchantments.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchantments.put("power", Enchantment.ARROW_DAMAGE);
        enchantments.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchantments.put("flame", Enchantment.ARROW_FIRE);
        enchantments.put("infinity", Enchantment.ARROW_INFINITE);

        for (Entry<String, Enchantment> entry : enchantments.entrySet()) {
            names.put(entry.getValue(), entry.getKey());
        }
    }

    public static Enchantment getEnchantment(String name) {
        Enchantment enchantment = enchantments.get(name);

        if (enchantment == null) {
            enchantment = Enchantment.getByName(name);
        }

        return enchantment;
    }

    public static String getName(Enchantment enchantment) {
        String name = names.get(enchantment);

        if (name == null) {
            name = enchantment.getName();
        }

        return name;
    }
}
