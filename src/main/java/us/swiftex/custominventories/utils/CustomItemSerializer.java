package us.swiftex.custominventories.utils;

import com.google.gson.*;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.swiftex.custominventories.utils.SkullData.SkullDataType;

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
                BANNER_PATTERNS = "BannerPatterns";
    }

    @Override
    public JsonElement serialize(CustomItem customItem, Type type, JsonSerializationContext context) {
        JsonObject data = new JsonObject();

        StringBuilder material = new StringBuilder();

        if(customItem.getMaterial() == null) {
            material.append(Material.BEDROCK.name());
        } else {
            material.append(customItem.getMaterial().name());
        }

        if(customItem.getDataValue() > 0) material.append(":").append(customItem.getDataValue());

        data.add(Node.MATERIAL, new JsonPrimitive(material.toString()));
        data.add(Node.AMOUNT, new JsonPrimitive(customItem.getAmount()));
        if(customItem.hasName()) data.add(Node.NAME, new JsonPrimitive((customItem.getName() == null ? "" : customItem.getName())));

        if(customItem.hasLore()) {
            JsonArray lore = new JsonArray();

            for (String row : customItem.getLore()) lore.add(new JsonPrimitive(row));

            data.add(Node.LORE, lore);
        }

        if(customItem.hasEnchantments()) {
            JsonObject enchantments = new JsonObject();
            for (Map.Entry<Enchantment, Integer> entry : customItem.getEnchantments().entrySet()) {
                enchantments.add(entry.getKey().getName(), new JsonPrimitive(entry.getValue()));
            }

            data.add(Node.ENCHANT, enchantments);
        }

        if(customItem.hasColor()) {
            JsonObject color = new JsonObject();

            color.add("RED", new JsonPrimitive(customItem.getColor().getRed()));
            color.add("GREEN", new JsonPrimitive(customItem.getColor().getGreen()));
            color.add("BLUE", new JsonPrimitive(customItem.getColor().getBlue()));

            data.add(Node.COLOR, color);
        }

        if(customItem.hasSkullData()) {
            JsonObject skullData = new JsonObject();

            skullData.add("Value", new JsonPrimitive(customItem.getSkullData().getValue()));
            skullData.add("Type", new JsonPrimitive(customItem.getSkullData().getType().name()));

            data.add(Node.SKULL_OWNER, skullData);
        }

        if(customItem.hasPotionEffects()) {
            data.add(Node.SPLASH, new JsonPrimitive(customItem.isSplash()));

            JsonObject potions = new JsonObject();

            for(PotionEffect entry : customItem.getPotionEffects()) {
                JsonObject potion = new JsonObject();

                potion.add("AMPLIFIER", new JsonPrimitive(entry.getAmplifier()));
                potion.add("DURATION", new JsonPrimitive(entry.getDuration()));

                potions.add(entry.getType().getName(), potion);
            }

            data.add(Node.EFFECTS, potions);
        }

        if(customItem.hasBannerBaseColor()) data.add(Node.BANNER_BASE_COLOR, new JsonPrimitive(customItem.getBannerBaseColor().toString()));

        if(customItem.hasBannerPatterns()) {
            JsonArray list = new JsonArray();

            for(Pattern pattern : customItem.getBannerPatterns()) {
                list.add(new JsonPrimitive(pattern.getColor().toString() + ":" + pattern.getPattern().getIdentifier()));
            }

            data.add(Node.BANNER_PATTERNS, list);
        }

        if(customItem.isItemGlow()) data.add(Node.GLOW, new JsonPrimitive(true));
        if(customItem.isRemoveAttributes()) data.add(Node.REMOVE_ATTRIBUTES, new JsonPrimitive(true));

        return data;
    }

    @Override
    public CustomItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject data = jsonElement.getAsJsonObject();

        Material material = null;
        short itemValue = 0;

        if(data.has(Node.MATERIAL)) {
            String materialData = data.get(Node.MATERIAL).getAsString();

            if(materialData.contains(":")) {
                String[] args = materialData.split(":");

                try {
                    itemValue = Short.parseShort(args[1]);
                } catch (NumberFormatException ignored) {

                }

                materialData = args[0];
            }

            material = Material.matchMaterial(materialData);
        }

        int amount = 1;

        if(data.has(Node.AMOUNT)) {
            amount = data.get(Node.AMOUNT).getAsInt();

            if(amount <= 0) {
                amount = 1;
            }
        }

        String name = null;
        if(data.has(Node.NAME)) name = data.get(Node.NAME).getAsString();

        List<String> lore = new ArrayList<>();
        if(data.has(Node.LORE)) {
            JsonArray array = data.get(Node.LORE).getAsJsonArray();

            for(JsonElement object : array) {
                lore.add(object.getAsString());
            }
        }

        Map<Enchantment, Integer> enchantments = new HashMap<>();

        if(data.has(Node.ENCHANT)) {
            JsonObject object = data.get(Node.ENCHANT).getAsJsonObject();

            for(Map.Entry<String, JsonElement> entry : object.entrySet()) {
                Enchantment enchantment = Enchantment.getByName(entry.getKey());

                if(enchantment == null) continue;

                enchantments.put(enchantment, entry.getValue().getAsInt());
            }
        }

        Color color = null;

        if(data.has(Node.COLOR)) {
            JsonObject object = data.get(Node.COLOR).getAsJsonObject();

            color = Color.fromRGB(object.get("RED").getAsInt(), object.get("GREEN").getAsInt(), object.get("BLUE").getAsInt());
        }

        SkullData skullData = null;

        if (data.has(Node.SKULL_OWNER)) {
            JsonObject map = data.get(Node.SKULL_OWNER).getAsJsonObject();

            if(map.has("Value") && map.has("Type")) {
                skullData = new SkullData(map.get("Value").getAsString(), SkullDataType.getByName(map.get("Type").getAsString()));
            }
        }

        boolean glow = false;
        if(data.has(Node.GLOW)) glow = data.get(Node.GLOW).getAsBoolean();


        boolean removeAttributes = false;
        if(data.has(Node.REMOVE_ATTRIBUTES)) removeAttributes = data.get(Node.REMOVE_ATTRIBUTES).getAsBoolean();

        boolean splash = false;
        if(data.has(Node.SPLASH)) splash = data.get(Node.SPLASH).getAsBoolean();

        List<PotionEffect> effects = new ArrayList<>();

        if(data.has(Node.EFFECTS)) {
            JsonObject object = data.get(Node.EFFECTS).getAsJsonObject();

            for(Map.Entry<String, JsonElement> entry : object.entrySet()) {
                PotionEffectType potionType = PotionEffectType.getByName(entry.getKey());

                if(potionType == null) continue;

                JsonObject potionData = entry.getValue().getAsJsonObject();

                int amplifier = potionData.get("AMPLIFIER").getAsInt();
                int duration = potionData.get("DURATION").getAsInt();

                effects.add(new PotionEffect(potionType, duration, amplifier));
            }
        }

        DyeColor bannerBaseColor = null;
        if(data.has(Node.BANNER_BASE_COLOR)) bannerBaseColor = DyeColor.valueOf(data.get(Node.BANNER_BASE_COLOR).getAsString());

        List<Pattern> bannerPatterns = null;
        if(data.has(Node.BANNER_PATTERNS)) {
            bannerPatterns = new ArrayList<>();

            JsonArray list = data.get(Node.BANNER_PATTERNS).getAsJsonArray();
            for(int i = 0; list.size() > i; i++) {
                String[] split = list.get(i).getAsString().split(":");

                if(split.length == 2) {
                    DyeColor patternColor = DyeColor.valueOf(split[0]);
                    PatternType patternType = PatternType.getByIdentifier(split[1]);

                    bannerPatterns.add(new Pattern(patternColor, patternType));
                }
            }
        }

        return CustomItem.builder(material, amount, itemValue).setName(name).setLore(lore).setEnchantments(enchantments)
                .setColor(color).setSkullData(skullData).setItemGlow(glow).setRemoveAttributes(removeAttributes).setSplash(splash)
                    .setPotionEffects(effects).setBannerBaseColor(bannerBaseColor).setBannerPatterns(bannerPatterns).build();
    }
}
