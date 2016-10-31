package us.swiftex.custominventories.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.swiftex.custominventories.utils.CastUtils.FormatException;
import us.swiftex.custominventories.utils.amount.Amount;
import us.swiftex.custominventories.utils.amount.AmountUtil;
import us.swiftex.custominventories.utils.amount.SimpleAmount;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSerializer {

    private static final Pattern MATERIAL_PATTERN = Pattern.compile("material:([^:\\s]*)(?::([^:\\s]*))?(?::([^:\\s]*))?");
    private static final Pattern NAME_PATTERN = Pattern.compile("name:\"([^\"]*)");
    private static final Pattern LORE_PATTER = Pattern.compile("lore:\"([^\"]*)");
    private static final Pattern ENCHANTMENTS_PATTERN = Pattern.compile("enchant:([^:\\s]*):([^:\\s]*)");
    private static final Pattern EFFECTS_PATTERN = Pattern.compile("effect:([^:\\s]*):([^:\\s]*):([^:\\s]*)");

    public static String getName(String text) {
        Matcher matcher = NAME_PATTERN.matcher(text);
        if(matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public static List<String> getLore(String text) {
        List<String> lore = new ArrayList<>();

        Matcher matcher = LORE_PATTER.matcher(text);
        if(matcher.find()) {
            lore.addAll(Arrays.asList(matcher.group(1).split("\\|")));
        }

        return lore;
    }

    public static MaterialEntry getMaterial(String text) throws SerializerException {
        Matcher matcher = MATERIAL_PATTERN.matcher(text);
        if(matcher.find()) {
            Material material;

            try {
                material = Material.getMaterial(CastUtils.asInt(matcher.group(1)));
            } catch (FormatException e) {
                material = Material.matchMaterial(matcher.group(1));
            }

            if(material == null) throw new SerializerException("The material is not found : " + matcher.group(1));

            Amount amount = new SimpleAmount(1);
            if(matcher.group(2) != null) {
                try {
                    amount = AmountUtil.deserialize(matcher.group(2));
                } catch (FormatException e) {
                    throw new SerializerException("Invalid amount : " + matcher.group(2));
                }
            }

            short dataValue = 0;

            if(matcher.group(3) != null) {
                try {
                    dataValue = (short) CastUtils.asInt(matcher.group(3));
                } catch (FormatException e) {
                    throw new SerializerException("Invalid data value : " + matcher.group(2));
                }
            }

            return new MaterialEntry(material, amount, dataValue);
        }

        throw new SerializerException("Invalid format Material");
    }

    public static Map<Enchantment, Integer> getEnchantments(String text) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        Matcher matcher = ENCHANTMENTS_PATTERN.matcher(text);
        while (matcher.find()) {
            Enchantment enchantment = MinecraftEnchantments.getEnchantment(matcher.group(1));
            if(enchantment == null) throw new SerializerException("The enchantment " + matcher.group(1) + " was not found");

            int level = CastUtils.asInt(matcher.group(2));

            enchantments.put(enchantment, level);
        }

        return enchantments;
    }

    public static List<PotionEffect> getEffects(String text) {
        List<PotionEffect> effects = new ArrayList<>();

        Matcher matcher = EFFECTS_PATTERN.matcher(text);
        while (matcher.find()) {
            PotionEffectType type = PotionEffectType.getByName(matcher.group(1));
            if(type == null) throw new SerializerException("The potion effect " + matcher.group(1) + " was not found");

            int amplifier = CastUtils.asInt(matcher.group(2));
            int duration = CastUtils.asInt(matcher.group(3));

            effects.add(new PotionEffect(type, duration * 20, amplifier));
        }

        return effects;
    }

    public static CustomItem getItem(String text) {
        MaterialEntry material = getMaterial(text);
        if(material == null) {
            return null;
        }

        String name = getName(text);
        List<String> lore = getLore(text);
        Map<Enchantment, Integer> enchantments = getEnchantments(text);
        List<PotionEffect> effects = getEffects(text);

        return CustomItem.builder(material.getMaterial(), material.getAmount(), material.getDataValue()).setName(name).setLore(lore)
             .setEnchantments(enchantments).setPotionEffects(effects).build();
    }

    public static String toString(CustomItem customItem) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("material:").append(customItem.getMaterial().name()).append(":").append(customItem.getAmount()).append(":").append(customItem.getDataValue());

        if(customItem.getName() != null) {
            stringBuilder.append(" ").append("name:").append('"').append(customItem.getName()).append('"');
        }

        if(customItem.getLore().size() > 0) {
            StringBuilder loreBuilder = new StringBuilder();
            loreBuilder.append("lore:").append('"');

            Iterator<String> lore = customItem.getLore().iterator();
            while (lore.hasNext()) {
                loreBuilder.append(lore.next());

                if(lore.hasNext()) {
                    loreBuilder.append('|');
                } else {
                    loreBuilder.append('"');
                }
            }

            stringBuilder.append(" ").append(loreBuilder.toString());
        }

        if(customItem.getEnchantments().size() > 0) {
            StringBuilder enchantmentsBuilder = new StringBuilder();

            Iterator<Entry<Enchantment, Integer>> iterator = customItem.getEnchantments().entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<Enchantment, Integer> entry = iterator.next();

                enchantmentsBuilder.append("enchant:").append(MinecraftEnchantments.getName(entry.getKey())).append(":").append(entry.getValue());

                if(iterator.hasNext()) {
                    stringBuilder.append(" ");
                }
            }

            stringBuilder.append(" ").append(enchantmentsBuilder.toString());
        }

        if(customItem.getPotionEffects().size() > 0) {
            StringBuilder effectsBuilder = new StringBuilder();

            Iterator<PotionEffect> iterator = customItem.getPotionEffects().iterator();
            while (iterator.hasNext()) {
                PotionEffect entry = iterator.next();

                effectsBuilder.append("effect:").append(entry.getType().getName()).append(":").append(entry.getAmplifier()).append(":").append(entry.getDuration());

                if(iterator.hasNext()) {
                    stringBuilder.append(" ");
                }
            }

            stringBuilder.append(" ").append(effectsBuilder.toString());
        }

        return stringBuilder.toString();
    }

    public static class SerializerException extends RuntimeException {

        public SerializerException(String message) {
            super(message);
        }
    }
}