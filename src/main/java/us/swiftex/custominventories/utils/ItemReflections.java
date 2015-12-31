package us.swiftex.custominventories.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class ItemReflections {

    private ItemReflections() { }

    private static Class<?> craftItemStackClazz;
    private static Class<?> nmsItemStackClazz;
    private static Class<?> nbtTagBaseClazz;
    private static Class<?> nbtTagCompoundClazz;
    private static Class<?> nbtTagListClazz;

    static  {
        craftItemStackClazz = Reflections.getCraftBukkitClass("inventory.CraftItemStack");
        nmsItemStackClazz = Reflections.getMinecraftClass("ItemStack");
        nbtTagCompoundClazz = Reflections.getMinecraftClass("NBTTagCompound");
        nbtTagListClazz = Reflections.getMinecraftClass("NBTTagList");
        nbtTagBaseClazz = Reflections.getMinecraftClass("NBTBase");
    }

    public static Object getCraftItemStack(ItemStack itemStack) {
        Validate.notNull(itemStack, "ItemStack can't be null");

        return Reflections.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class).invoke(null, itemStack);
    }

    private static ItemStack getItemStack(Object item) {
        Validate.notNull(item, "Item can't be null");

        return (ItemStack) Reflections.getMethod(craftItemStackClazz, "asBukkitCopy", item.getClass(), item.getClass()).invoke(craftItemStackClazz, item);
    }

    public static String convertItemStackToJson(ItemStack itemStack) {
        Validate.notNull(itemStack, "ItemStack can't be null");

        Object nmsItemStack = getCraftItemStack(itemStack);
        Object nmsNbtTagCompound = Reflections.newInstance(nbtTagCompoundClazz);

        return Reflections.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz).invoke(nmsItemStack, nmsNbtTagCompound).toString();
    }

    private static Object getNewNBTTag() {
        return Reflections.newInstance(nbtTagCompoundClazz);
    }

    private static Object getNewNBTList() {
        return Reflections.newInstance(nbtTagListClazz);
    }

    private static Object setNBTTag(Object NBTTag, Object NMSItem) {
        Validate.notNull(NBTTag, "NBTTag can't be null");
        Validate.notNull(NMSItem, "NMSItem can't be null");

        Reflections.getMethod(NMSItem.getClass(), "setTag", nbtTagCompoundClazz).invoke(NMSItem, NBTTag);
        return NMSItem;
    }

    private static Object getNBTTagCompound(Object nmsItem) {
        Validate.notNull(nmsItem, "NMSItem can't be null");

        return Reflections.getMethod(nmsItem.getClass(), "getTag").invoke(nmsItem);
    }

    private static ItemStack set(Class<?> clazz, String type, ItemStack itemStack, String key, Object value) {
        Validate.notNull(clazz, "Clazz can't be null");
        Validate.notNull(type, "Type can't be null");
        Validate.notNull(itemStack, "ItemStack can't be null");
        Validate.notNull(key, "Key can't be null");
        Validate.notNull(value, "Value can't be null");

        Object craftItemStack = getCraftItemStack(itemStack);
        if(craftItemStack == null) {
            throw new NullPointerException("CraftItemStack is null");
        }

        Object nbtTag = getNBTTagCompound(craftItemStack);
        if(nbtTag == null) {
            nbtTag = getNewNBTTag();
        }

        Reflections.getMethod(nbtTagCompoundClazz, type, String.class, clazz).invoke(nbtTag, key, value);

        return getItemStack(setNBTTag(nbtTag, craftItemStack));
    }

    public static ItemStack setString(ItemStack itemStack, String key, String value) {
        return set(String.class, "setString", itemStack, key, value);
    }

    public static ItemStack setInt(ItemStack itemStack, String key, int value) {
        return set(int.class, "setInt", itemStack, key, value);
    }

    public static ItemStack setDouble(ItemStack itemStack, String key, double value) {
        return set(double.class, "setDouble", itemStack, key, value);
    }

    public static ItemStack setBoolean(ItemStack itemStack, String key, boolean value) {
        return set(boolean.class, "setBoolean", itemStack, key, value);
    }

    public static ItemStack setLong(ItemStack itemStack, String key, long value) {
        return set(long.class, "setLong", itemStack, key, value);
    }

    private static Object get(String type, ItemStack itemStack, String key) {
        Validate.notNull(type, "Type can't be null");
        Validate.notNull(itemStack, "ItemStack can't be null");
        Validate.notNull(key, "Key can't be null");


        Object craftItemStack = getCraftItemStack(itemStack);
        if(craftItemStack == null) {
            throw new NullPointerException("CraftItemStack is null");
        }

        Object nbtTag = getNBTTagCompound(craftItemStack);
        if(nbtTag == null) {
            nbtTag = getNewNBTTag();
        }

        return Reflections.getMethod(nbtTagCompoundClazz, type, String.class).invoke(nbtTag, key);
    }

    public static String getString(ItemStack itemStack, String key) {
        return (String) get("getString", itemStack, key);
    }

    public static int getInt(ItemStack itemStack, String key) {
        return (int) get("getInt", itemStack, key);
    }

    public static double getDouble(ItemStack itemStack, String key) {
        return (double) get("getDouble", itemStack, key);
    }

    public static boolean getBoolean(ItemStack itemStack, String key) {
        return (boolean) get("getBoolean", itemStack, key);
    }

    public static long getLong(ItemStack itemStack, String key) {
        return (long) get("getLong", itemStack, key);
    }

    public static boolean hasKey(ItemStack itemStack, String key) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        Validate.notNull(key, "Key can't be null");

        Object craftItemStack = getCraftItemStack(itemStack);
        if(craftItemStack == null) {
            throw new NullPointerException("CraftItemStack is null");
        }

        Object nbtTag = getNBTTagCompound(craftItemStack);
        if(nbtTag == null) {
            return false;
        }

        return (boolean) Reflections.getMethod(nbtTagCompoundClazz, "hasKey", String.class).invoke(nbtTag, key);
    }

    public static ItemStack glow(ItemStack itemStack, boolean value) {
        Validate.notNull(itemStack, "ItemStack can't be null");

        if (value) {
            return setGlow(itemStack, getNewNBTList());
        } else {
            return setGlow(itemStack, null);
        }
    }

    private static ItemStack setGlow(ItemStack itemStack, Object value) {
        Validate.notNull(itemStack, "ItemStack can't be null");

        Object craftItemStack = getCraftItemStack(itemStack);
        if(craftItemStack == null) {
            throw new NullPointerException("CraftItemStack is null");
        }

        Object nbtTag = getNBTTagCompound(craftItemStack);
        if(nbtTag == null) {
            nbtTag = getNewNBTTag();
        }

        Reflections.getMethod(nbtTagCompoundClazz, "set", String.class, nbtTagBaseClazz).invoke(nbtTag, "ench", value);

        return getItemStack(craftItemStack);
    }

    public static boolean hasGlow(ItemStack itemStack) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        return hasKey(itemStack, "ench");
    }

    public static ItemStack setContent(ItemStack itemStack, Map<String, Object> content) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        Validate.notNull(content, "Content can't be null");

        for(Map.Entry<String, Object> entry : content.entrySet()) {
            Object object = entry.getValue();

            ValidTypes validType = ValidTypes.byObject(object);

            if(validType == ValidTypes.UNKNOWN) {
                continue;
            }

            itemStack = set(validType.getClass(), validType.getSet(), itemStack, entry.getKey(), entry.getValue());
        }

        return itemStack;
    }

    public static void sendItemMessage(Player player, String message, ItemStack itemStack){
        Validate.notNull(player, "Player can't be null");
        Validate.notNull(message, "Message can't be null");
        Validate.notNull(itemStack, "ItemStack can't be null");

        String itemJson = convertItemStackToJson(itemStack);

        BaseComponent[] hoverEventComponents = new BaseComponent[]{ new TextComponent(itemJson) };
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents);

        TextComponent component = new TextComponent(message);
                      component.setHoverEvent(hoverEvent);

        player.spigot().sendMessage(component);
    }

    public static boolean isValid(Object object) {
        return ValidTypes.byObject(object) != ValidTypes.UNKNOWN;
    }
}
