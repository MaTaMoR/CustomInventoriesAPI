package us.swiftex.custominventories.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import us.swiftex.custominventories.actions.BuildAction;

import java.util.*;

public class CustomItem {

    private Material material;
    private short dataValue;
    private int amount;

    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private boolean itemGlow;

    private Color color;
    private String skullOwner;
    private List<PotionEffect> effects;

    private Map<String, Object> nbtData;

    private Map<Integer, Set<Variable>>  variables;
    private ItemStack cachedItem;

    private boolean splash;

    private Set<BuildAction> buildActions;

    public CustomItem(Material material) {
        this(material, 1);
    }

    public CustomItem(Material material, int amount) {
        this(material, amount, (short) 0);
    }

    public CustomItem(Material material, int amount, short dataValue) {
        this.material = material;
        this.amount = amount;
        this.dataValue = dataValue;

        this.enchantments = new HashMap<>();

        this.nbtData = new HashMap<>();
    }

    public void setMaterial(Material material) {
        if(material == Material.AIR) material = null;
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public void setAmount(int amount) {
        if(1 > amount) {
            amount = 1;
        } else if(amount > 127) {
            amount = 127;
        }

        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setDataValue(short dataValue) {
        if(0 > dataValue) {
            dataValue = 0;
        }

        this.dataValue = dataValue;
    }

    public short getDataValue() {
        return dataValue;
    }

    public void setName(String name) {
        this.name = name;

        if(name == null) {
            return;
        }

        for(Variable variable : Variable.values()) {
            if(name.contains(variable.getText())) {

                if(variables == null) {
                    variables = new HashMap<>();
                }

                Set<Variable> nameVariables = variables.get(-1);

                if(nameVariables == null) {
                    nameVariables = new HashSet<>();
                    variables.put(-1, nameVariables);
                }

                nameVariables.add(variable);
            }
        }
    }

    public boolean hasName() {
        return name != null;
    }

    public String getName() {
        return name;
    }

    public void setLore(String... lore) {
        if(lore != null) {
            setLore(Arrays.asList(lore));
        }
    }

    public void setLore(List<String> lore) {
        this.lore = lore;

        if(lore == null) {
            return;
        }

        for(int i = 0; lore.size() > i; i++) {
            for(Variable variable : Variable.values()) {
                if(lore.get(i).contains(variable.getText())) {

                    if(variables == null) {
                        variables = new HashMap<>();
                    }

                    Set<Variable> lineVariables  = variables.get(i);

                    if(lineVariables  ==  null) {
                        lineVariables  = new HashSet<>();
                        variables.put(i, lineVariables);
                    }

                    lineVariables.add(variable);
                }
            }
        }
    }

    public boolean hasLore() {
        return lore != null && lore.size() > 0;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        if(enchantments == null) {
            clearEnchantments();
            return;
        }

        this.enchantments = enchantments;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void addEnchantment(Enchantment enchantment) {
        addEnchantment(enchantment, 1);
    }

    public void addEnchantment(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
    }

    public void removeEnchantment(Enchantment enchantment) {
        enchantments.remove(enchantment);
    }

    public void clearEnchantments() {
        enchantments.clear();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getSkullOwner() {
        return skullOwner;
    }

    public void setSkullOwner(String skullOwner) {
        this.skullOwner = skullOwner;
    }

    public boolean getItemGlow() {
        return itemGlow;
    }

    public void setItemGlow(boolean itemGlow) {
        this.itemGlow = itemGlow;
    }

    public void setNbtData(String key, Object value) {
        if(key == null) {
            return;
        }

        nbtData.put(key, value);
    }

    public boolean hasKey(String key) {
        return nbtData.containsKey(key);
    }

    public Object getNbtData(String key) {
        return nbtData.get(key);
    }


    protected String calculateName(Player player) {
        if(hasName()) {
            String name = this.name;

            if (player != null && variables != null) {

                Set<Variable> nameVariables = variables.get(-1);
                if (nameVariables != null) {
                    for (Variable nameVariable : nameVariables) {
                        name = name.replace(nameVariable.getText(), nameVariable.getReplacement(player));
                    }
                }
            }

            if (name.isEmpty()) {
                return ChatColor.WHITE.toString();
            } else {
                return name;
            }
        }

        return null;
    }

    protected List<String> calculateLore(Player player) {
        List<String> output = null;

        if (hasLore()) {

            output = new ArrayList<>();

            if (player != null && variables != null) {
                for (int i = 0; i < lore.size(); i++) {
                    String line = lore.get(i);

                    Set<Variable> lineVariables = variables.get(i);
                    if (lineVariables != null) {
                        for (Variable lineVariable : lineVariables) {
                            line = line.replace(lineVariable.getText(), lineVariable.getReplacement(player));
                        }
                    }

                    output.add(line);
                }
            } else {
                output.addAll(lore);
            }
        }

        return output;
    }

    public void setPotionEffects(PotionEffect... effects) {
        if(effects != null) {
            setPotionEffects(Arrays.asList(effects));
        }
    }

    public void setPotionEffects(List<PotionEffect> effects) {
        this.effects = effects;
    }

    public void addPotionEffect(PotionEffect effect) {
        if(effect == null) {
            return;
        }

        if(material == Material.POTION) {
            if(effects == null) {
                effects = new ArrayList<>();
            }

            effects.add(effect);
        }
    }

    public void removePotionEffect(PotionEffect effect) {
        if(effect == null || effects == null) {
            return;
        }

        effects.remove(effect);
    }

    public boolean hasPotionEffects() {
        return effects != null && effects.size() > 0;
    }

    public List<PotionEffect> getPotionEffects() {
        return effects;
    }

    public boolean isSplash() {
        return splash;
    }

    public void setSplash(boolean splash) {
        this.splash = splash;
    }

    public void addBuildAction(BuildAction... actions) {
        if(buildActions == null) {
            buildActions = new HashSet<>();
        }

        Collections.addAll(buildActions, actions);
    }

    public void removeBuildAction(BuildAction buildAction) {
        if(buildAction == null) {
            return;
        }

        if(buildActions.isEmpty()) {
            buildActions = null;
        }
    }

    public Set<BuildAction> getBuildActions() {
        return Collections.unmodifiableSet(buildActions);
    }

    public Builder builder() {
        return builder(this);
    }

    public ItemStack build() {
        return build(null);
    }

    public ItemStack build(Player player) {
        if(variables == null && cachedItem != null) {
            return cachedItem;
        } else if(variables != null && cachedItem != null) {
            cachedItem = null;
        }

        ItemStack itemStack = (material == null ? new ItemStack(Material.BEDROCK, amount) : new ItemStack(material, amount, dataValue));

        if(itemGlow) {
            itemStack = ItemReflections.glow(itemStack, true);
        }

        if(nbtData.size() > 0) {
            itemStack = ItemReflections.setContent(itemStack, nbtData);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utils.colorize(calculateName(player)));
        itemMeta.setLore(Utils.colorize(calculateLore(player)));

        if(color != null && itemMeta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) itemMeta).setColor(color);
        }

        if(skullOwner != null && itemMeta instanceof SkullMeta) {
            ((SkullMeta) itemMeta).setOwner(skullOwner);
        }

        if(enchantments.size() > 0) {
            if (itemMeta instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) itemMeta;

                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    storageMeta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
                }
            } else {
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    itemMeta.addEnchant(entry.getKey(), entry.getValue(), true);
                }
            }
        }

        itemStack.setItemMeta(itemMeta);

        if(hasPotionEffects() && material == Material.POTION) {
            Potion potion = new Potion(PotionType.getByEffect(effects.get(0).getType()));
            potion.setSplash(splash);

            potion.apply(itemStack);
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

            itemStack.setItemMeta(itemMeta);
            itemStack = PotionUtils.apply(itemStack, effects.toArray(new PotionEffect[effects.size()]));
        }

        if(buildActions != null) {
            for(BuildAction buildAction : buildActions) {
                itemStack = buildAction.onBuild(itemStack);
            }
        }

        if(variables == null) {
            cachedItem = itemStack;
        }

        return itemStack;
    }

    public static class Builder {

        private CustomItem customItem;

        private Builder(CustomItem customItem) {
            this.customItem = customItem;
        }

        public Builder setMaterial(Material material) {
            customItem.setMaterial(material);
            return this;
        }

        public Builder setAmount(int amount) {
            customItem.setAmount(amount);
            return this;
        }

        public Builder setDataValue(short dataValue) {
            customItem.setDataValue(dataValue);
            return this;
        }

        public Builder setName(String name) {
            customItem.setName(name);
            return this;
        }

        public Builder setLore(String... lore) {
            customItem.setLore(lore);
            return this;
        }

        public Builder setLore(List<String> lore) {
            customItem.setLore(lore);
            return this;
        }

        public Builder setEnchantments(Map<Enchantment, Integer> enchantments) {
            customItem.setEnchantments(enchantments);
            return this;
        }

        public Builder addEnchantment(Enchantment enchantment) {
            customItem.addEnchantment(enchantment);
            return this;
        }

        public Builder addEnchantment(Enchantment enchantment, int level) {
            customItem.addEnchantment(enchantment, level);
            return this;
        }

        public Builder setColor(Color color) {
            customItem.setColor(color);
            return this;
        }

        public Builder setSkullOwner(String skullOwner) {
            customItem.setSkullOwner(skullOwner);
            return this;
        }

        public Builder setItemGlow(boolean itemGlow) {
            customItem.setItemGlow(itemGlow);
            return this;
        }

        public Builder setNbtData(String key, Object nbtData) {
            customItem.setNbtData(key, nbtData);
            return this;
        }

        public Builder setSplash(boolean splash) {
            customItem.setSplash(splash);
            return this;
        }

        public Builder setPotionEffects(PotionEffect... effects) {
            customItem.setPotionEffects(effects);
            return this;
        }

        public Builder setPotionEffects(List<PotionEffect> effects) {
            customItem.setPotionEffects(effects);
            return this;
        }

        public Builder addPotionEffects(PotionEffect effect) {
            customItem.addPotionEffect(effect);
            return this;
        }

        public Builder addBuldAction(BuildAction... actions) {
            customItem.addBuildAction(actions);
            return this;
        }

        public CustomItem build() {
            return customItem;
        }
    }

    public static Builder builder(Material material) {
        return builder(material, 1);
    }

    public static Builder builder(Material material, int amount) {
        return builder(material, amount, (short) 0);
    }

    public static Builder builder(Material material, int amount, short dataValue) {
        return new Builder(new CustomItem(material, amount, dataValue));
    }

    public static Builder builder(CustomItem customItem) {
        return new Builder(customItem);
    }

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
                GLOW = "Glow";
    }

    public static Map<String, Object> serialize(CustomItem customItem) {
        Validate.notNull(customItem, "CustomItem mustn't be null");

        Map<String, Object> serialized = new LinkedHashMap<>();


        StringBuilder material = new StringBuilder();

        if(customItem.getMaterial() == null) {
            material.append(Material.BEDROCK.name());
        } else {
            material.append(customItem.getMaterial().name());
        }

        if(customItem.getDataValue() > 0) {
            material.append(":").append(customItem.getDataValue());
        }

        serialized.put(Node.MATERIAL, material.toString());
        serialized.put(Node.AMOUNT, customItem.getAmount());
        serialized.put(Node.NAME, (customItem.getName() == null ? "" : customItem.getName()));
        serialized.put(Node.LORE, customItem.getLore());
        serialized.put(Node.ENCHANT, serializeEnchantments(customItem.getEnchantments()));

        if(customItem.getColor() != null) {
            serialized.put(Node.COLOR, serializeColor(customItem.getColor()));
        }

        if(customItem.getSkullOwner() != null) {
            serialized.put(Node.SKULL_OWNER, customItem.getSkullOwner());
        }

        if(customItem.hasPotionEffects() && customItem.getMaterial() == Material.POTION) {
            serialized.put(Node.SPLASH, customItem.isSplash());
            serialized.put(Node.EFFECTS, serializeEffects(customItem.getPotionEffects()));
        }

        serialized.put(Node.GLOW, customItem.getItemGlow());

        return serialized;
    }

    public static CustomItem deserialize(ConfigurationSection section) {
        Material material = null;
        short itemValue = 0;

        if(section.isSet(Node.MATERIAL)) {
            String materialData = section.getString(Node.MATERIAL);

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

        if(section.isSet(Node.AMOUNT)) {
            try {
                amount = Integer.parseInt(section.getString(Node.AMOUNT));
            } catch (NumberFormatException ignored) {
            }
        }

        String name = section.getString(Node.NAME);
        List<String> lore = section.getStringList(Node.LORE);

        Map<Enchantment, Integer> enchantments = deserializeEnchantment(section.getStringList(Node.ENCHANT));

        Color color = section.getColor(Node.COLOR);
        String skullOwner = section.getString(Node.SKULL_OWNER);

        boolean glow = section.getBoolean(Node.GLOW);

        List<PotionEffect> effects = null;

        if(section.isSet(Node.EFFECTS)) {
            effects = deserializedEffects(section.getStringList(Node.EFFECTS));
        }

        CustomItem customItem = CustomItem.builder(material, amount, itemValue).setName(name).setLore(lore).setEnchantments(enchantments)
                .setColor(color).setSkullOwner(skullOwner).setItemGlow(glow).build();

        if(effects == null) {
            return customItem;
        }

        customItem.setSplash(section.getBoolean(Node.SPLASH));
        customItem.setPotionEffects(effects);

        return customItem;
    }

    @SuppressWarnings("Unchecked")
    public static CustomItem deserialize(Map<String, Object> section) {
        Material material = null;
        short itemValue = 0;

        if(section.containsKey(Node.MATERIAL)) {
            String materialData = CastUtils.asString(section.get(Node.MATERIAL));

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

        if(section.containsKey(Node.AMOUNT)) {
            amount = CastUtils.asInt(section.get(Node.AMOUNT));
        }

        String name = CastUtils.asString(section.get(Node.NAME));

        List<String> lore = null;

        if(section.containsKey(Node.LORE)) {
            lore = (List<String>) section.get(Node.LORE);
        }

        Map<Enchantment, Integer> enchantments = null;

        if(section.containsKey(Node.ENCHANT)) {
            enchantments = deserializeEnchantment((List<String>) section.get(Node.ENCHANT));
        }

        Color color = null;

        if(section.containsKey(Node.COLOR)) {
            color = Color.deserialize((Map<String, Object>) section.get(Node.COLOR));
        }

        String skullOwner = null;

        if(section.containsKey(Node.SKULL_OWNER)) {
            skullOwner = CastUtils.asString(section.get(Node.SKULL_OWNER));
        }

        boolean glow = false;

        if(section.containsKey(Node.GLOW)) {
            glow = CastUtils.asBoolean(section.get(Node.GLOW));
        }

        List<PotionEffect> effects = null;

        if(section.containsKey(Node.EFFECTS)) {
            effects = deserializedEffects((List<String>) section.get(Node.EFFECTS));
        }

        CustomItem customItem = CustomItem.builder(material, amount, itemValue).setName(name).setLore(lore).setEnchantments(enchantments)
                .setColor(color).setSkullOwner(skullOwner).setItemGlow(glow).build();

        if(effects == null) {
            return customItem;
        }

        customItem.setSplash(CastUtils.asBoolean(section.get(Node.SPLASH)));
        customItem.setPotionEffects(effects);

        return customItem;
    }

    private static Map<String, Object> serializeColor(Color color) {
        Map<String, Object> serialized = new LinkedHashMap<>();

        serialized.put("RED", color.getRed());
        serialized.put("BLUE", color.getBlue());
        serialized.put("GREEN", color.getGreen());

        return serialized;
    }

    private static List<String> serializeEnchantments(Map<Enchantment, Integer> enchants) {
        List<String> serialized = new ArrayList<>();

        for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            serialized.add(entry.getKey().getName() + ":" + entry.getValue());
        }

        return serialized;
    }

    private static Map<Enchantment, Integer> deserializeEnchantment(List<String> serialized) {
        Map<Enchantment, Integer> deserialized = new HashMap<>();

        for(String entry : serialized) {

            int level = 1;

            if(entry.contains(":")) {
                String[] args = entry.split(":");

                try {
                    level = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {

                }

                entry = args[0];
            }

            Enchantment enchantment = Enchantment.getByName(entry);
            if(enchantment == null) {
                continue;
            }

            deserialized.put(enchantment, level);
        }


        return deserialized;
    }

    private static List<String> serializeEffects(List<PotionEffect> potionEffects) {
        List<String> serialized = new ArrayList<>();

        for(PotionEffect potionEffect : potionEffects) {
            serialized.add(potionEffect.getType().getName() + ":" + potionEffect.getAmplifier() + "-" + potionEffect.getDuration());
        }

        return serialized;
    }

    private static List<PotionEffect> deserializedEffects(List<String> serialized) {
        List<PotionEffect> deserialized = new ArrayList<>();

        for(String entry : serialized) {

            int amplifier = 0;
            int seconds = 0;

            if(entry.contains("-")) {
                String[] args = entry.split("-");

                try {
                    seconds = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {

                }

                entry = args[0];
            }

            if(entry.contains(":")) {
                String[] args = entry.split(":");

                try {
                    amplifier = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {

                }

                entry = args[0];
            }

            PotionEffectType effectType = PotionEffectType.getByName(entry);
            if(effectType == null) {
                continue;
            }

            deserialized.add(new PotionEffect(effectType, seconds, amplifier));
        }

        return deserialized;
    }

    public static CustomItem toCustomItem(ItemStack itemStack) {
        if(itemStack == null) {
            return null;
        }

        CustomItem customItem = new CustomItem(itemStack.getType(), itemStack.getAmount(), itemStack.getDurability());

        ItemMeta itemMeta = itemStack.getItemMeta();

        customItem.setName(itemMeta.getDisplayName());
        customItem.setLore(itemMeta.getLore());

        if(itemMeta instanceof LeatherArmorMeta) {
            customItem.setColor(((LeatherArmorMeta) itemMeta).getColor());
        }

        if(itemMeta instanceof SkullMeta) {
            customItem.setSkullOwner(((SkullMeta) itemMeta).getOwner());
        }

        if(itemMeta instanceof EnchantmentStorageMeta) {
            customItem.setEnchantments(((EnchantmentStorageMeta) itemMeta).getStoredEnchants());
        } else {
            customItem.setEnchantments(itemStack.getEnchantments());
        }

        if(itemStack.getType() == Material.POTION) {
            if(PotionUtils.hasEffects(itemStack)) {
                for(PotionEffect potionEffect : PotionUtils.getEffects(itemStack)) {
                    customItem.addPotionEffect(potionEffect);
                }
            }
        }

        return customItem;
    }
}
