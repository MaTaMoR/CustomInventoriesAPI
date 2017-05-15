package us.swiftex.custominventories.utils;

import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import us.swiftex.custominventories.utils.CalculateEvent.CalculateType;
import us.swiftex.custominventories.utils.SkullData.SkullDataType;
import us.swiftex.custominventories.utils.amount.Amount;
import us.swiftex.custominventories.utils.amount.AmountUtil;
import us.swiftex.custominventories.utils.amount.SimpleAmount;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;
import us.swiftex.custominventories.utils.server.ServerVariable;

import java.util.*;

public class CustomItem implements Cloneable {

    private Material material;
    private short dataValue;
    private Amount amount;

    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private boolean itemGlow;
    private boolean removeAttributes;
    private boolean splash;

    private Color color;
    private SkullData skullData;
    private List<PotionEffect> effects;

    private Map<String, NBTTag> nbtData;

    private Map<String, LocalVariable> localVariables;

    private List<CalculateEvent> calculateEvents;

    private DyeColor bannerBaseColor;
    private List<Pattern> bannerPatterns;


    public CustomItem(Material material) {
        this(material, new SimpleAmount(1), (short) 0);
    }

    @Deprecated
    public CustomItem(Material material, int amount) {
        this(material, new SimpleAmount(1), (short) 0);
    }

    @Deprecated
    public CustomItem(Material material, Amount amount) {
        this(material, amount, (short) 0);
    }

    @Deprecated
    public CustomItem(Material material, int amount, short dataValue) {
        this(material, new SimpleAmount(amount), dataValue);
    }

    public CustomItem(Material material, Amount amount, short dataValue) {
        this.material = material;
        this.amount = (amount == null ? new SimpleAmount(0) : amount);
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

    @Deprecated
    public void setAmount(int amount) {
        if(0 > amount) {
            amount = 1;
        } else if(amount > 127) {
            amount = 127;
        }

        this.amount = new SimpleAmount(amount);
    }

    public void setAmount(Amount amount) {
        if(amount == null) return;
        this.amount = amount;
    }

    @Deprecated
    public int getAmount() {
        return amount.getAmount();
    }

    public Amount getActualAmount() {
        return amount;
    }

    public void setDataValue(short dataValue) {
        if(0 > dataValue) dataValue = 0;
        this.dataValue = dataValue;
    }

    public short getDataValue() {
        return dataValue;
    }

    public void registerLocalVariable(LocalVariable variable) {
        if(localVariables == null) localVariables = new HashMap<>();
        localVariables.put(variable.getText(), variable);
    }

    public void unregisterLocalVariable(String name) {
        if(localVariables == null) return;
        localVariables.remove(name);
    }


    public Collection<LocalVariable> getLocalVariables() {
        if(localVariables == null) return new ArrayList<>();
        return localVariables.values();
    }

    public boolean hasLocalVariables() {
        return localVariables != null && localVariables.size() > 0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return name != null;
    }

    public String getName() {
        return name;
    }

    public void setLore(String... lore) {
        if(lore != null) setLore(Arrays.asList(lore));
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public boolean hasLore() {
        return lore != null && !lore.isEmpty();
    }

    public List<String> getLore() {
        if(lore == null) return new ArrayList<>();
        return lore;
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        if(enchantments == null) {
            clearEnchantments();
            return;
        }

        this.enchantments = enchantments;
    }

    public boolean hasEnchantments() {
        return !this.enchantments.isEmpty();
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

    public int getEnchantment(Enchantment enchantment) {
        if(enchantments.containsKey(enchantment)) return enchantments.get(enchantment);

        return -1;
    }

    public void clearEnchantments() {
        enchantments.clear();
    }

    public boolean hasColor() {
        return color != null;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean hasSkullData() {
        return skullData != null;
    }

    public SkullData getSkullData() {
        return skullData;
    }

    @Deprecated
    public void setSkullOwner(String skullOwner) {
        if(skullOwner == null) {
            this.skullData = null;
        } else {
            this.skullData = new SkullData(skullOwner, SkullDataType.NAME);
        }
    }

    public void setSkullData(SkullData skullData) {
        this.skullData = skullData;
    }

    public boolean getItemGlow() {
        return itemGlow;
    }

    public void setItemGlow(boolean itemGlow) {
        this.itemGlow = itemGlow;
    }

    public boolean getRemoveAttributes() {
        return removeAttributes;
    }

    public void setRemoveAttributes(boolean removeAttributes) {
        this.removeAttributes = removeAttributes;
    }

    @Deprecated
    public void setNBTData(String key, Object value) {
        Validate.notNull(key, "The key can't be null");
        Validate.notNull(value, "The value can't be null");

        NBTType type = NBTType.byObject(value);
        if(type == null) throw new RuntimeException("Invalid object");

        setNBTData(key, type.newInstance(value));
    }

    public void setNBTData(String key, NBTTag value) {
        Validate.notNull(key, "The key can't be null");
        Validate.notNull(value, "The value can't be null");

        nbtData.put(key, value);
    }

    public void setNBTContent(Map<String, NBTTag> content) {
        Validate.notNull(content, "The content can't be null");

        nbtData.putAll(content);
    }

    public boolean hasNBTKey(String key) {
        return nbtData.containsKey(key);
    }

    public NBTTag getNBTData(String key) {
        return nbtData.get(key);
    }

    public void removeNBTData(String key) {
        nbtData.remove(key);
    }

    public Map<String, NBTTag> getNBTContent() {
        return Collections.unmodifiableMap(nbtData);
    }

    public void setPotionEffects(PotionEffect... effects) {
        if(effects != null) setPotionEffects(Arrays.asList(effects));
    }

    public void setPotionEffects(List<PotionEffect> effects) {
        this.effects = effects;
    }

    public void addPotionEffect(PotionEffect effect) {
        if(effect == null) return;

        if(effects == null) effects = new ArrayList<>();
        effects.add(effect);
    }

    public void removePotionEffect(PotionEffect effect) {
        if(effect == null || effects == null) return;
        effects.remove(effect);
    }

    public boolean hasPotionEffects() {
        return effects != null && effects.size() > 0;
    }

    public List<PotionEffect> getPotionEffects() {
        if(effects == null) return new ArrayList<>();

        return effects;
    }

    public boolean isSplash() {
        return splash;
    }

    public void setSplash(boolean splash) {
        this.splash = splash;
    }

    public void setBannerBaseColor(DyeColor bannerBaseColor) {
        this.bannerBaseColor = bannerBaseColor;
    }

    public boolean hasBannerBaseColor() {
        return this.bannerBaseColor != null;
    }

    public DyeColor getBannerBaseColor() {
        return this.bannerBaseColor;
    }

    public void addBannerPattern(Pattern pattern) {
        if(pattern == null) return;
        if(this.bannerPatterns == null) this.bannerPatterns = new ArrayList<>();

        this.bannerPatterns.add(pattern);
    }

    public boolean hasBannerPatterns() {
        return this.bannerPatterns != null && !this.bannerPatterns.isEmpty();
    }

    public boolean hasBannerPattern(Pattern pattern) {
        return this.bannerPatterns != null && this.bannerPatterns.contains(pattern);
    }

    public void setBannerPatterns(List<Pattern> bannerPatterns) {
        if(bannerPatterns == null) {
            this.bannerPatterns = null;
        } else {
            this.bannerPatterns = bannerPatterns;
        }
    }

    public List<Pattern> getBannerPatterns() {
        if(this.bannerPatterns == null) return new ArrayList<>();
        return this.bannerPatterns;
    }

    public void registerCalculateEvent(CalculateEvent event) {
        if(calculateEvents == null) calculateEvents = new ArrayList<>();
        calculateEvents.add(event);
    }

    public void unregisterCalculateEvent(CalculateEvent event) {
        if(calculateEvents == null) return;
        calculateEvents.remove(event);
    }

    public List<CalculateEvent> getCalculateEvents() {
        if(calculateEvents == null) {
            return new ArrayList<>();
        }

        return calculateEvents;
    }

    public boolean hasCalculateEvents() {
        return calculateEvents != null && calculateEvents.size() > 0;
    }

    protected String calculateName(Player player) {
        if(hasName()) {
            String name = this.name;

            name = ServerVariable.replace(name);
            if (player != null) {
                for(LocalVariable variable : getLocalVariables()) {
                    name = name.replace(variable.getText(), variable.getReplacement(player));
                }

                name = Variable.replace(name, player);
            }

            if(hasCalculateEvents()) {
                for (CalculateEvent event : calculateEvents) {
                    name = event.calculate(CalculateType.NAME, name);
                }
            }

            if (name == null || name.isEmpty()) {
                return ChatColor.WHITE.toString();
            } else {
                return name;
            }
        }

        return null;
    }

    protected List<String> calculateLore(Player player) {
        List<String> lore = new ArrayList<>();
        List<String> output = new ArrayList<>();

        if (hasLore()) {
            for(String line : this.lore) {
                lore.add(ServerVariable.replace(line));
            }

            if (player != null) {
                for (String line : lore) {
                    for (LocalVariable lineVariable : getLocalVariables()) {
                        line = line.replace(lineVariable.getText(), lineVariable.getReplacement(player));
                    }

                    line = Variable.replace(line, player);

                    if(hasCalculateEvents()) {
                        for(CalculateEvent event : calculateEvents) {
                            line = event.calculate(CalculateType.LORE, line);
                        }
                    }

                    if(line != null) {
                        output.add(line);
                    }
                }
            } else {
                for(String line : lore) {
                    if(hasCalculateEvents()) {
                        for(CalculateEvent event : calculateEvents) {
                            line = event.calculate(CalculateType.LORE, line);
                        }
                    }

                    if(line != null) {
                        output.add(line);
                    }
                }
            }
        }

        return output;
    }

    public Map<String, Object> serialize() {
        return serialize(this);
    }

    public Builder builder() {
        return new Builder(this);
    }

    public ItemStack build() {
        return build(null);
    }

    public ItemStack build(Player player) {
        ItemStack itemStack = (material == null ? new ItemStack(Material.BEDROCK) : new ItemStack(material, amount.getAmount(), dataValue));

        ItemMeta itemMeta = itemStack.getItemMeta();

        if(hasName()) itemMeta.setDisplayName(Utils.color(calculateName(player)));

        if(hasLore()) itemMeta.setLore(Utils.color(calculateLore(player)));

        if(hasColor() && itemMeta instanceof LeatherArmorMeta) ((LeatherArmorMeta) itemMeta).setColor(color);

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

        if(hasPotionEffects() && itemMeta instanceof PotionMeta) {
            PotionType potionType = PotionType.getByEffect(getPotionEffects().get(0).getType());

            Potion potion = new Potion(potionType);
            potion.setSplash(splash);

            potion.apply(itemStack);

            PotionMeta potionMeta = (PotionMeta) itemMeta;

            for(PotionEffect effect : getPotionEffects()) potionMeta.addCustomEffect(effect, true);

            if(!getRemoveAttributes() && potionType == null) {
                List<String> lore = (itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<String>());
                for(PotionEffect effect : effects) lore.add(PotionUtils.getDescription(effect));

                itemMeta.setLore(lore);
            }
        }

        if(itemMeta instanceof BannerMeta) {
            BannerMeta bannerMeta = (BannerMeta) itemMeta;

            bannerMeta.setBaseColor(this.bannerBaseColor);

            if(hasBannerPatterns()) for(Pattern pattern : this.bannerPatterns) bannerMeta.addPattern(pattern);
        }

        itemStack.setItemMeta(itemMeta);

        if(hasSkullData() && itemMeta instanceof SkullMeta) skullData.apply(itemStack);

        if(nbtData.size() > 0) itemStack = ItemReflections.setContent(itemStack, nbtData);

        if(itemGlow && enchantments.isEmpty()) itemStack = ItemReflections.setItemGlow(itemStack);

        if(removeAttributes) itemStack = AttributeRemover.hideAttributes(itemStack);

        return itemStack;
    }

    @Override
    public CustomItem clone() {
        try {
            return (CustomItem) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder {

        private CustomItem customItem;

        private Builder(CustomItem customItem) {
            this.customItem = customItem;
        }

        public Builder setMaterial(Material material) {
            this.customItem.setMaterial(material);
            return this;
        }

        @Deprecated
        public Builder setAmount(int amount) {
            this.customItem.setAmount(amount);
            return this;
        }

        public Builder setAmount(Amount amount) {
            this.customItem.setAmount(amount);
            return this;
        }

        public Builder setDataValue(short dataValue) {
            this.customItem.setDataValue(dataValue);
            return this;
        }

        public Builder setName(String name) {
            this.customItem.setName(name);
            return this;
        }

        public Builder setLore(String... lore) {
            this.customItem.setLore(lore);
            return this;
        }

        public Builder setLore(List<String> lore) {
            this.customItem.setLore(lore);
            return this;
        }

        public Builder setEnchantments(Map<Enchantment, Integer> enchantments) {
            this.customItem.setEnchantments(enchantments);
            return this;
        }

        public Builder addEnchantment(Enchantment enchantment) {
            this.customItem.addEnchantment(enchantment);
            return this;
        }

        public Builder addEnchantment(Enchantment enchantment, int level) {
            this.customItem.addEnchantment(enchantment, level);
            return this;
        }

        public Builder setColor(Color color) {
            this.customItem.setColor(color);
            return this;
        }

        @Deprecated
        public Builder setSkullOwner(String skullOwner) {
            this.customItem.setSkullOwner(skullOwner);
            return this;
        }

        public Builder setSkullData(SkullData skullData) {
            this.customItem.setSkullData(skullData);
            return this;
        }

        public Builder setItemGlow(boolean itemGlow) {
            this.customItem.setItemGlow(itemGlow);
            return this;
        }

        public Builder setRemoveAttributes(boolean removeAttributes) {
            this.customItem.setRemoveAttributes(removeAttributes);
            return this;
        }

        @Deprecated
        public Builder setNbtData(String key, Object nbtData) {
            this.customItem.setNBTData(key, nbtData);
            return this;
        }

        public Builder setNbtData(String key, NBTTag nbtData) {
            this.customItem.setNBTData(key, nbtData);
            return this;
        }

        public Builder setNbtContent(Map<String, NBTTag> content) {
            this.customItem.setNBTContent(content);
            return this;
        }

        public Builder setSplash(boolean splash) {
            this.customItem.setSplash(splash);
            return this;
        }

        public Builder setPotionEffects(PotionEffect... effects) {
            this.customItem.setPotionEffects(effects);
            return this;
        }

        public Builder setPotionEffects(List<PotionEffect> effects) {
            this.customItem.setPotionEffects(effects);
            return this;
        }

        @Deprecated
        public Builder addPotionEffects(PotionEffect effect) {
            this.customItem.addPotionEffect(effect);
            return this;
        }

        @Deprecated
        public Builder addPotionEffect(PotionEffect effect) {
            this.customItem.addPotionEffect(effect);
            return this;
        }

        public Builder registerLocalVariable(LocalVariable variable) {
            this.customItem.registerLocalVariable(variable);
            return this;
        }

        public Builder setBannerBaseColor(DyeColor bannerBaseColor) {
            this.customItem.setBannerBaseColor(bannerBaseColor);
            return this;
        }

        public Builder addBannerPattern(Pattern pattern) {
            this.customItem.addBannerPattern(pattern);
            return this;
        }

        public Builder setBannerPatterns(List<Pattern> pattern) {
            this.customItem.setBannerPatterns(pattern);
            return this;
        }

        public CustomItem build() {
            return this.customItem;
        }

        public ItemStack toItemStack() {
            return this.customItem.build();
        }

        public ItemStack toItemStack(Player player) {
            return this.customItem.build(player);
        }
    }

    public static Builder builder(Material material) {
        return builder(material, 1);
    }

    public static Builder builder(Material material, int amount) {
        return builder(material, amount, (short) 0);
    }

    public static Builder builder(Material material, int amount, short dataValue) {
        return builder(material, new SimpleAmount(amount), dataValue);
    }

    public static Builder builder(Material material, Amount amount, short dataValue) {
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
                GLOW = "Glow",
                REMOVE_ATTRIBUTES = "RemoveAttributes",
                BANNER_BASE_COLOR = "BannerBaseColor",
                BANNER_PATTERNS = "BannerPatterns";
    }

    public static Map<String, Object> serialize(CustomItem customItem) {
        Validate.notNull(customItem, "CustomItem can't be null");

        Map<String, Object> serialized = new LinkedHashMap<>();

        StringBuilder material = new StringBuilder();

        if(customItem.getMaterial() == null) {
            material.append(Material.BEDROCK.name());
        } else {
            material.append(customItem.getMaterial().name());
        }

        if(customItem.getDataValue() > 0) material.append(":").append(customItem.getDataValue());

        serialized.put(Node.MATERIAL, material.toString());
        serialized.put(Node.AMOUNT, AmountUtil.serialize(customItem.getActualAmount()));

        if(customItem.hasName()) serialized.put(Node.NAME, (customItem.getName() == null ? "" : customItem.getName()));
        if(customItem.hasLore()) serialized.put(Node.LORE, customItem.getLore());
        if(customItem.hasEnchantments()) serialized.put(Node.ENCHANT, serializeEnchantments(customItem.getEnchantments()));

        if(customItem.hasColor()) serialized.put(Node.COLOR, serializeColor(customItem.getColor()));

        if(customItem.hasSkullData()) {
            Map<String, Object> skullData = new HashMap<>();

            skullData.put("Value", customItem.getSkullData().getValue());
            skullData.put("Type", customItem.getSkullData().getType().name());

            serialized.put(Node.SKULL_OWNER, skullData);
        }

        if(customItem.hasPotionEffects()) {
            serialized.put(Node.SPLASH, customItem.isSplash());
            serialized.put(Node.EFFECTS, serializeEffects(customItem.getPotionEffects()));
        }

        if(customItem.hasBannerBaseColor()) serialized.put(Node.BANNER_BASE_COLOR, customItem.getBannerBaseColor().name());
        if(customItem.hasBannerPatterns()) serialized.put(Node.BANNER_PATTERNS, serializePatterns(customItem.getBannerPatterns()));

        if(customItem.getItemGlow()) serialized.put(Node.GLOW, customItem.getItemGlow());
        if(customItem.getRemoveAttributes()) serialized.put(Node.REMOVE_ATTRIBUTES, customItem.getRemoveAttributes());

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

        Amount amount = new SimpleAmount(1);
        if(section.isSet(Node.AMOUNT)) {
            amount = AmountUtil.deserialize(CastUtils.asString(section.get(Node.AMOUNT)));
        }

        String name = section.getString(Node.NAME);
        List<String> lore = section.getStringList(Node.LORE);

        Map<Enchantment, Integer> enchantments = deserializeEnchantment(section.getStringList(Node.ENCHANT));

        Color color = section.getColor(Node.COLOR);

        SkullData skullData = null;

        if(section.isSet(Node.SKULL_OWNER)) {
            Map<String, Object> map = ((MemorySection) section.get(Node.SKULL_OWNER)).getValues(true);

            if(map.containsKey("Value") && map.containsKey("Type")) {
                skullData = new SkullData((String) map.get("Value"), SkullDataType.getByName((String) map.get("Type")));
            }
        }

        boolean glow = section.getBoolean(Node.GLOW);
        boolean removeAttributes = section.getBoolean(Node.REMOVE_ATTRIBUTES);
        boolean splash = section.getBoolean(Node.SPLASH);

        List<PotionEffect> effects = null;

        if(section.isSet(Node.EFFECTS)) effects = deserializeEffects(section.getStringList(Node.EFFECTS));

        DyeColor bannerBaseColor = null;

        if(section.isSet(Node.BANNER_BASE_COLOR)) bannerBaseColor = DyeColor.valueOf(section.getString(Node.BANNER_BASE_COLOR));

        List<Pattern> bannerPatterns = null;

        if(section.isSet(Node.BANNER_PATTERNS)) bannerPatterns = deserialize(section.getStringList(Node.BANNER_PATTERNS));

        return CustomItem.builder(material, amount, itemValue).setName(name).setLore(lore).setEnchantments(enchantments)
                .setColor(color).setSkullData(skullData).setItemGlow(glow).setRemoveAttributes(removeAttributes).setSplash(splash)
                    .setPotionEffects(effects).setBannerBaseColor(bannerBaseColor).setBannerPatterns(bannerPatterns).build();
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

        Amount amount = new SimpleAmount(1);
        if(section.containsKey(Node.AMOUNT)) amount = AmountUtil.deserialize(CastUtils.asString(section.get(Node.AMOUNT)));

        String name = null;
        if (section.containsKey(Node.NAME)) {
            name = CastUtils.asString(section.get(Node.NAME));
        }

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

        SkullData skullData = null;

        if(section.containsKey(Node.SKULL_OWNER)) {
            Map<String, Object> map = ((MemorySection) section.get(Node.SKULL_OWNER)).getValues(true);

            if(map.containsKey("Value") && map.containsKey("Type")) {
                skullData = new SkullData((String) map.get("Value"), SkullDataType.getByName((String) map.get("Type")));
            }
        }

        boolean glow = false;

        if(section.containsKey(Node.GLOW)) {
            glow = CastUtils.asBoolean(section.get(Node.GLOW));
        }

        boolean removeAttributes = false;

        if(section.containsKey(Node.REMOVE_ATTRIBUTES)) {
            removeAttributes = CastUtils.asBoolean(section.get(Node.REMOVE_ATTRIBUTES));
        }

        boolean splash = false;

        if(section.containsKey(Node.SPLASH)) {
            splash = CastUtils.asBoolean(section.get(Node.SPLASH));
        }

        List<PotionEffect> effects = null;

        if(section.containsKey(Node.EFFECTS)) {
            effects = deserializeEffects((List<String>) section.get(Node.EFFECTS));
        }

        if(section.containsKey(Node.EFFECTS)) effects = deserializeEffects((List<String>) section.get(Node.EFFECTS));

        DyeColor bannerBaseColor = null;

        if(section.containsKey(Node.BANNER_BASE_COLOR)) bannerBaseColor = DyeColor.valueOf(CastUtils.asString(section.get(Node.BANNER_BASE_COLOR)));

        List<Pattern> bannerPatterns = null;

        if(section.containsKey(Node.BANNER_PATTERNS)) bannerPatterns = deserialize((List<String>) section.get(Node.BANNER_PATTERNS));

        return CustomItem.builder(material, amount, itemValue).setName(name).setLore(lore).setEnchantments(enchantments)
                .setColor(color).setSkullData(skullData).setItemGlow(glow).setRemoveAttributes(removeAttributes).setSplash(splash)
                    .setPotionEffects(effects).setBannerBaseColor(bannerBaseColor).setBannerPatterns(bannerPatterns).build();
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
            if(enchantment == null) continue;

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

    private static List<PotionEffect> deserializeEffects(List<String> serialized) {
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
            if(effectType == null) continue;

            deserialized.add(new PotionEffect(effectType, seconds, amplifier));
        }

        return deserialized;
    }

    private static List<String> serializePatterns(List<Pattern> patterns) {
        List<String> serialized = new ArrayList<>();

        for(Pattern pattern : patterns) {
            serialized.add(pattern.getColor().toString() + ":" + pattern.getPattern().getIdentifier());
        }

        return serialized;
    }

    private static List<Pattern> deserialize(List<String> serialized) {
        List<Pattern> patterns = new ArrayList<>();

        for(String string : serialized) {
            String[] split = string.split(":");

            if(split.length == 2) {
                DyeColor color = DyeColor.valueOf(split[0]);
                PatternType type = PatternType.getByIdentifier(split[1]);

                patterns.add(new Pattern(color, type));
            }
        }

        return patterns;
    }

    private static final String[] RESERVED_KEYS = { "ench", "CustomPotionEffects" };

    public static CustomItem toCustomItem(ItemStack itemStack) {
        if(itemStack == null) return null;

        CustomItem customItem = new CustomItem(itemStack.getType(), new SimpleAmount(itemStack.getAmount()), itemStack.getDurability());

        ItemMeta itemMeta = itemStack.getItemMeta();

        customItem.setName(itemMeta.getDisplayName());
        customItem.setLore(itemMeta.getLore());

        if(itemMeta instanceof LeatherArmorMeta) customItem.setColor(((LeatherArmorMeta) itemMeta).getColor());

        if(itemMeta instanceof SkullMeta) {
            String texture = ItemReflections.getTexture(itemStack);
            if(texture == null) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;

                if(skullMeta.hasOwner()) customItem.setSkullData(new SkullData(skullMeta.getOwner(), SkullDataType.NAME));
            } else {
                customItem.setSkullData(new SkullData(texture, SkullDataType.TEXTURE));
            }
        }

        if(itemMeta instanceof EnchantmentStorageMeta) {
            customItem.setEnchantments(((EnchantmentStorageMeta) itemMeta).getStoredEnchants());
        } else {
            customItem.setEnchantments(itemStack.getEnchantments());
        }

        if(itemMeta instanceof PotionMeta) {
            Potion potion = Potion.fromItemStack(itemStack);
            customItem.setSplash(potion.isSplash());

            PotionMeta potionMeta = (PotionMeta) itemMeta;

            for(PotionEffect effect : potionMeta.getCustomEffects()) customItem.addPotionEffect(effect);
        }

        if(itemMeta instanceof BannerMeta) {
            BannerMeta bannerMeta = (BannerMeta) itemMeta;

            customItem.setBannerBaseColor(bannerMeta.getBaseColor());
            for(Pattern pattern : bannerMeta.getPatterns()) customItem.addBannerPattern(pattern);
        }

        customItem.setItemGlow(ItemReflections.hasGlow(itemStack));

        Map<String, NBTTag> content = ItemReflections.getContent(itemStack);
        for(String key : RESERVED_KEYS) content.remove(key);

        customItem.setNBTContent(content);

        return customItem;
    }
}
