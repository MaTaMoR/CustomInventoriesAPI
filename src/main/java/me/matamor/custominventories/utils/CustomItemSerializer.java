package me.matamor.custominventories.utils;

import com.google.gson.*;
import me.matamor.custominventories.utils.amount.Amount;
import me.matamor.custominventories.utils.amount.AmountUtil;
import me.matamor.custominventories.utils.amount.SimpleAmount;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;
import java.util.*;

public class CustomItemSerializer implements JsonSerializer<CustomItem>, JsonDeserializer<CustomItem> {

    private static class Node {

        public static final String
                MATERIAL = "Material",
                AMOUNT = "Amount",
                NAME = "DisplayName",
                LORE = "Lore",
                ENCHANT = "Enchantments",
                COLOR = "Color",
                SKULL_OWNER = "Skull-Owner",
                SPLASH = "Potion.Splash",
                EFFECTS = "Potion.Effects",
                GLOW = "Glow",
                REMOVE_ATTRIBUTES = "RemoveAttributes",
                BANNER_BASE_COLOR = "BannerBaseColor",
                BANNER_PATTERNS = "BannerPatterns",
                ITEM_FLAGS = "ItemFlags",
                FIREWORK_EFFECT = "FireworkEffect";
    }

    @Override
    public JsonElement serialize(CustomItem customItem, Type type, JsonSerializationContext context) {
        JsonObject data = new JsonObject();

        StringBuilder material = new StringBuilder();

        if (customItem.getMaterial() == null) {
            material.append(Material.BEDROCK.name());
        } else {
            material.append(customItem.getMaterial().name());
        }

        if (customItem.getDataValue() > 0) material.append(":").append(customItem.getDataValue());

        data.add(Node.MATERIAL, new JsonPrimitive(material.toString()));
        data.add(Node.AMOUNT, new JsonPrimitive(AmountUtil.serialize(customItem.getAmount())));

        if (customItem.hasName()) data.add(Node.NAME, new JsonPrimitive((customItem.getName() == null ? "" : customItem.getName())));

        if (customItem.hasLore()) {
            JsonArray lore = new JsonArray();

            for (String row : customItem.getLore()) lore.add(new JsonPrimitive(row));

            data.add(Node.LORE, lore);
        }

        if (customItem.hasEnchantments()) {
            JsonObject enchantments = new JsonObject();
            for (Map.Entry<Enchantment, Integer> entry : customItem.getEnchantments().entrySet()) {
                enchantments.add(entry.getKey().getName(), new JsonPrimitive(entry.getValue()));
            }

            data.add(Node.ENCHANT, enchantments);
        }

        if (customItem.hasColor()) {
            JsonObject color = new JsonObject();

            color.add("RED", new JsonPrimitive(customItem.getColor().getRed()));
            color.add("GREEN", new JsonPrimitive(customItem.getColor().getGreen()));
            color.add("BLUE", new JsonPrimitive(customItem.getColor().getBlue()));

            data.add(Node.COLOR, color);
        }

        if (customItem.hasSkullData()) {
            JsonObject skullData = new JsonObject();

            skullData.add("Value", new JsonPrimitive(customItem.getSkullData().getValue()));
            skullData.add("Type", new JsonPrimitive(customItem.getSkullData().getType().name()));

            data.add(Node.SKULL_OWNER, skullData);
        }

        if (customItem.hasFireworkEffect()) {
            JsonObject fireworkData = new JsonObject();
            //return ImmutableMap.of("flicker", this.flicker, "trail", this.trail, "colors", this.colors, "fade-colors", this.fadeColors, "type", this.type.name());

            fireworkData.add("Flicker", new JsonPrimitive(customItem.getFireworkEffect().hasFlicker()));
            fireworkData.add("Trail", new JsonPrimitive(customItem.getFireworkEffect().hasTrail()));

            JsonArray colors = new JsonArray();

            for (Color color : customItem.getFireworkEffect().getColors()) {
                colors.add(serializeColor(color));
            }

            fireworkData.add("Colors", colors);

            JsonArray fadeColors = new JsonArray();

            for (Color color : customItem.getFireworkEffect().getFadeColors()) {
                fadeColors.add(serializeColor(color));
            }

            fireworkData.add("FadeColors", fadeColors);

            fireworkData.add("Type", new JsonPrimitive(customItem.getFireworkEffect().getType().name()));

            data.add(Node.FIREWORK_EFFECT, fireworkData);
        }

        if (customItem.hasPotionEffects()) {
            data.add(Node.SPLASH, new JsonPrimitive(customItem.isSplash()));

            JsonObject potions = new JsonObject();

            for (PotionEffect entry : customItem.getPotionEffects()) {
                JsonObject potion = new JsonObject();

                potion.add("Amplifier", new JsonPrimitive(entry.getAmplifier()));
                potion.add("Duration", new JsonPrimitive(entry.getDuration()));

                potions.add(entry.getType().getName(), potion);
            }

            data.add(Node.EFFECTS, potions);
        }

        if (customItem.hasBannerBaseColor()) data.add(Node.BANNER_BASE_COLOR, new JsonPrimitive(customItem.getBannerBaseColor().toString()));

        if (customItem.hasBannerPatterns()) {
            JsonArray list = new JsonArray();

            for (Pattern pattern : customItem.getBannerPatterns()) {
                list.add(new JsonPrimitive(pattern.getColor().toString() + ":" + pattern.getPattern().getIdentifier()));
            }

            data.add(Node.BANNER_PATTERNS, list);
        }

        if (customItem.isItemGlow()) data.add(Node.GLOW, new JsonPrimitive(true));

        if (customItem.isRemoveAttributes()) data.add(Node.REMOVE_ATTRIBUTES, new JsonPrimitive(true));

        if (customItem.hasItemFlags()) {
            JsonArray flags = new JsonArray();

            for (ItemFlag itemFlag : customItem.getItemFlags()) {
                JsonObject flag = new JsonObject();

                flag.add("Flag", new JsonPrimitive(flag.toString()));

                flags.add(flag);
            }

            data.add(Node.ITEM_FLAGS, flags);
        }

        return data;
    }

    private JsonObject serializeColor(Color color) {
        JsonObject colorObject = new JsonObject();

        colorObject.add("RED", new JsonPrimitive(color.getRed()));
        colorObject.add("BLUE", new JsonPrimitive(color.getBlue()));
        colorObject.add("GREEN", new JsonPrimitive(color.getGreen()));

        return colorObject;
    }

    @Override
    public CustomItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject data = jsonElement.getAsJsonObject();

        Material material = null;
        short itemValue = 0;

        if (data.has(Node.MATERIAL)) {
            String materialData = data.get(Node.MATERIAL).getAsString();

            if (materialData.contains(":")) {
                String[] args = materialData.split(":");

                try {
                    itemValue = Short.parseShort(args[1]);
                } catch (NumberFormatException ignored) {

                }

                materialData = args[0];
            }

            material = Material.matchMaterial(materialData);
        }

        Amount amount = new SimpleAmount(1);

        if (data.has(Node.AMOUNT)) {
            amount = AmountUtil.deserialize(data.get(Node.AMOUNT).getAsString());
        }

        String name = null;
        if (data.has(Node.NAME)) {
            name = data.get(Node.NAME).getAsString();
        }

        List<String> lore = new ArrayList<>();
        if (data.has(Node.LORE)) {
            JsonArray array = data.get(Node.LORE).getAsJsonArray();

            for(JsonElement object : array) {
                lore.add(object.getAsString());
            }
        }

        Map<Enchantment, Integer> enchantments = new HashMap<>();

        if (data.has(Node.ENCHANT)) {
            JsonObject object = data.get(Node.ENCHANT).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                Enchantment enchantment = Enchantment.getByName(entry.getKey());
                if (enchantment == null) continue;

                enchantments.put(enchantment, entry.getValue().getAsInt());
            }
        }

        Color color = null;

        if (data.has(Node.COLOR)) {
            JsonObject object = data.get(Node.COLOR).getAsJsonObject();

            color = Color.fromRGB(object.get("RED").getAsInt(), object.get("GREEN").getAsInt(), object.get("BLUE").getAsInt());
        }

        SkullData skullData = null;

        if (data.has(Node.SKULL_OWNER)) {
            JsonObject map = data.get(Node.SKULL_OWNER).getAsJsonObject();

            if (map.has("Value") && map.has("Type")) {
                skullData = new SkullData(map.get("Value").getAsString(), SkullData.SkullDataType.getByName(map.get("Type").getAsString()));
            }
        }

        FireworkEffect fireworkEffect = null;

        if (data.has(Node.FIREWORK_EFFECT)) {
            JsonObject map = data.get(Node.FIREWORK_EFFECT).getAsJsonObject();

            if (map.has("Flicker") && map.has("Trail") && map.has("Colors") && map.has("FadeColors") && map.has("Type")) {
                boolean flicker = map.get("Flicker").getAsBoolean();
                boolean trail = map.get("Trail").getAsBoolean();

                List<Color> colors = new ArrayList<>();
                JsonArray colorsArray = map.get("Colors").getAsJsonArray();

                for (JsonElement elementSerialized : colorsArray) {
                    JsonObject colorSerialized = elementSerialized.getAsJsonObject();

                    if (colorSerialized.has("RED") && colorSerialized.has("BLUE") && colorSerialized.has("GREEN")) {
                        colors.add(Color.fromRGB(colorSerialized.get("RED").getAsInt(), colorSerialized.get("BLUE").getAsInt(), colorSerialized.get("GREEN").getAsInt()));
                    }
                }

                List<Color> fadeColors = new ArrayList<>();
                JsonArray fadeColorsArray = map.get("Colors").getAsJsonArray();

                for (JsonElement elementSerialized : fadeColorsArray) {
                    JsonObject colorSerialized = elementSerialized.getAsJsonObject();

                    if (colorSerialized.has("RED") && colorSerialized.has("BLUE") && colorSerialized.has("GREEN")) {
                        fadeColors.add(Color.fromRGB(colorSerialized.get("RED").getAsInt(), colorSerialized.get("BLUE").getAsInt(), colorSerialized.get("GREEN").getAsInt()));
                    }
                }

                FireworkEffect.Type fireworkType = FireworkEffect.Type.valueOf(map.get("Type").getAsString());

                fireworkEffect = FireworkEffect.builder().flicker(flicker).trail(trail).withColor(colors).withFade(fadeColors).with(fireworkType).build();
            }
        }

        boolean glow = false;
        if (data.has(Node.GLOW)) glow = data.get(Node.GLOW).getAsBoolean();


        boolean removeAttributes = false;
        if (data.has(Node.REMOVE_ATTRIBUTES)) removeAttributes = data.get(Node.REMOVE_ATTRIBUTES).getAsBoolean();

        boolean splash = false;
        if (data.has(Node.SPLASH)) splash = data.get(Node.SPLASH).getAsBoolean();

        List<PotionEffect> effects = new ArrayList<>();

        if (data.has(Node.EFFECTS)) {
            JsonObject object = data.get(Node.EFFECTS).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                PotionEffectType potionType = PotionEffectType.getByName(entry.getKey());

                if (potionType == null) continue;

                JsonObject potionData = entry.getValue().getAsJsonObject();

                int amplifier = potionData.get("AMPLIFIER").getAsInt();
                int duration = potionData.get("DURATION").getAsInt();

                effects.add(new PotionEffect(potionType, duration, amplifier));
            }
        }

        DyeColor bannerBaseColor = null;
        if (data.has(Node.BANNER_BASE_COLOR)) bannerBaseColor = DyeColor.valueOf(data.get(Node.BANNER_BASE_COLOR).getAsString());

        List<Pattern> bannerPatterns = null;
        if (data.has(Node.BANNER_PATTERNS)) {
            bannerPatterns = new ArrayList<>();

            JsonArray list = data.get(Node.BANNER_PATTERNS).getAsJsonArray();
            for (int i = 0; list.size() > i; i++) {
                String[] split = list.get(i).getAsString().split(":");

                if (split.length == 2) {
                    DyeColor patternColor = DyeColor.valueOf(split[0]);
                    PatternType patternType = PatternType.getByIdentifier(split[1]);

                    bannerPatterns.add(new Pattern(patternColor, patternType));
                }
            }
        }

        List<ItemFlag> itemFlags = null;
        if (data.has(Node.ITEM_FLAGS)) {
            itemFlags = new ArrayList<>();

            JsonArray list = data.get(Node.ITEM_FLAGS).getAsJsonArray();
            for (int i = 0; list.size() > i; i++) {
                String flag = list.get(i).getAsString();

                try {
                    itemFlags.add(ItemFlag.valueOf(flag));
                } catch (IllegalArgumentException ignored) {

                }
            }
        }

        CustomItem customItem = new CustomItem(material, amount, itemValue);

        if (name != null) {
            customItem.setName(name);
        }

        if (lore != null) {
            customItem.setLore(lore);
        }

        if (enchantments != null) {
            customItem.setEnchantments(enchantments);
        }

        if (color != null) {
            customItem.setColor(color);
        }

        if (skullData != null) {
            customItem.setSkullData(skullData);
        }

        if (fireworkEffect != null) {
            customItem.setFireworkEffect(fireworkEffect);
        }

        customItem.setItemGlow(glow);
        customItem.setRemoveAttributes(removeAttributes);
        customItem.setSplash(splash);

        if (effects != null) {
            customItem.setPotionEffects(effects);
        }

        if (bannerBaseColor != null) {
            customItem.setBannerBaseColor(bannerBaseColor);
        }

        if (bannerPatterns != null) {
            customItem.setBannerPatterns(bannerPatterns);
        }

        if (itemFlags != null) {
            customItem.setItemFlags(itemFlags);
        }

        return customItem;
    }
}
