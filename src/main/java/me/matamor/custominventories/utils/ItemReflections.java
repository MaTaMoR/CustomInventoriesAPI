package me.matamor.custominventories.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.matamor.custominventories.utils.nbt.defaults.*;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import me.matamor.custominventories.utils.nbt.NBTTag;
import me.matamor.custominventories.utils.nbt.NBTType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ItemReflections {

    private ItemReflections() { }

    private static final Class<?> CRAFT_ITEM_STACK_CLASS = Reflections.getCraftBukkitClass("inventory.CraftItemStack");
    private static final Class<?> NMS_CLASS = Reflections.getMinecraftClass("ItemStack");
    private static final Class<?> NBT_BASE_CLASS = Reflections.getMinecraftClass("NBTBase");
    private static final Class<?> NBT_COMPOUND_CLASS = Reflections.getMinecraftClass("NBTTagCompound");
    private static final Class<?> NBT_LIST_CLASS = Reflections.getMinecraftClass("NBTTagList");

    private static final Reflections.MethodInvoker TO_METHOD = Reflections.getMethod(CRAFT_ITEM_STACK_CLASS, "asNMSCopy", ItemStack.class);
    private static final Reflections.MethodInvoker FROM_METHOD = Reflections.getMethod(CRAFT_ITEM_STACK_CLASS, "asCraftMirror", CRAFT_ITEM_STACK_CLASS);

    private static final Reflections.MethodInvoker SET_COMPOUND_METHOD = Reflections.getMethod(NMS_CLASS, "setTag", NBT_COMPOUND_CLASS);
    private static final Reflections.MethodInvoker GET_COMPOUND_METHOD = Reflections.getMethod(NMS_CLASS, "getTag");

    private static final Reflections.MethodInvoker SET_METHOD = Reflections.getMethod(NBT_COMPOUND_CLASS, "set", String.class, NBT_BASE_CLASS);
    private static final Reflections.MethodInvoker GET_METHOD = Reflections.getMethod(NBT_COMPOUND_CLASS, "get", String.class);
    private static final Reflections.MethodInvoker NBT_HAS_KEY_METHOD = Reflections.getMethod(NBT_COMPOUND_CLASS, "hasKey", String.class);
    private static final Reflections.MethodInvoker NBT_KEYS_METHOD = Reflections.getMethod(NBT_COMPOUND_CLASS, "c");

    private static final Reflections.FieldAccessor<GameProfile> PROFILE_FIELD = Reflections.getField(Reflections.getCraftBukkitClass("inventory.CraftMetaSkull"), "profile", GameProfile.class);

    public static Object asNMSItem(ItemStack itemStack) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        return TO_METHOD.invoke(null, itemStack);
    }

    private static ItemStack getItemStack(Object nmsItem) {
        Validate.notNull(nmsItem, "NMSItem can't be null");
        return (ItemStack) FROM_METHOD.invoke(null, nmsItem);
    }

    public static Object newNBTTagCompound() {
        return Reflections.newInstance(NBT_COMPOUND_CLASS);
    }

    public static Object getNewNBTList() {
        return Reflections.newInstance(NBT_LIST_CLASS);
    }

    private static Object setNBTTagCompound(Object compound, Object nmsItem) {
        Validate.notNull(compound, "TagCompound can't be null");
        Validate.notNull(nmsItem, "NMSItem can't be null");

        SET_COMPOUND_METHOD.invoke(nmsItem, compound);
        return nmsItem;
    }

    private static Object getNBTTagCompound(Object nmsItem) {
        Validate.notNull(nmsItem, "NMSItem can't be null");
        return GET_COMPOUND_METHOD.invoke(nmsItem);
    }

    public static ItemStack setNBTTag(ItemStack itemStack, String key, NBTTag value) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        Validate.notNull(key, "Key can't be null");
        Validate.notNull(value, "Value can't be null");

        Object nmsItem = asNMSItem(itemStack);
        if (nmsItem == null) {
            throw new NullPointerException("NMSItem is null");
        }

        Object tagCompound = getNBTTagCompound(nmsItem);
        if (tagCompound == null) {
            tagCompound = newNBTTagCompound();
        }

        SET_METHOD.invoke(tagCompound, key, (value == null ? null : value.toNBT()));
        return getItemStack(setNBTTagCompound(tagCompound, nmsItem));
    }

    public static NBTTag getNBTTag(ItemStack itemStack, String key) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        Validate.notNull(key, "Key can't be null");

        Object craftItemStack = asNMSItem(itemStack);
        if (craftItemStack == null) {
            throw new NullPointerException("NMSItem is null");
        }

        Object nbtTag = getNBTTagCompound(craftItemStack);
        if (nbtTag == null) {
            return null;
        }

        Object object = GET_METHOD.invoke(nbtTag, key);

        NBTType type = NBTType.byNBT(object);
        if (type == null) {
            return null;
        }

        return type.fromNBT(object);
    }

    public static NBTTagDouble getDouble(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagDouble ? (NBTTagDouble) tag : null;
    }

    public static NBTTagBoolean getBoolean(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagBoolean ? (NBTTagBoolean) tag : null;
    }

    public static NBTTagByte getByte(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagByte ? (NBTTagByte) tag : null;
    }

    public static NBTTagByteArray getByteArray(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagByteArray ? (NBTTagByteArray) tag : null;
    }

    public static NBTTagInt getInt(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagInt ? (NBTTagInt) tag : null;
    }

    public static NBTTagIntArray getIntArray(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagIntArray ? (NBTTagIntArray) tag : null;
    }

    private static NBTTagList getList(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagList ? (NBTTagList) tag : null;
    }

    public static NBTTagLong getLong(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagLong ? (NBTTagLong) tag : null;
    }

    public static NBTTagShort getShort(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagShort ? (NBTTagShort) tag : null;
    }

    public static NBTTagString getString(ItemStack itemStack, String key) {
        NBTTag tag = getNBTTag(itemStack, key);
        return tag == null ? null : tag instanceof NBTTagString ? (NBTTagString) tag : null;
    }

    public static boolean hasKey(ItemStack itemStack, String key) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        Validate.notNull(key, "Key can't be null");

        Object nmsItem = asNMSItem(itemStack);
        if (nmsItem == null) {
            throw new NullPointerException("NMSItem is null");
        }

        Object tagCompound = getNBTTagCompound(nmsItem);
        return tagCompound != null && (boolean) NBT_HAS_KEY_METHOD.invoke(tagCompound, key);
    }

    public static ItemStack setItemGlow(ItemStack itemStack) {
        Validate.notNull(itemStack, "ItemStack can't be null");

        Object nmsItem = asNMSItem(itemStack);
        if (nmsItem == null) throw new NullPointerException("NMSItem is null");

        Object tagCompound = getNBTTagCompound(nmsItem);
        if (tagCompound == null) {
            tagCompound = newNBTTagCompound();
        }

        SET_METHOD.invoke(tagCompound, "ench", getNewNBTList());
        return getItemStack(setNBTTagCompound(tagCompound, nmsItem));
    }

    public static boolean hasGlow(ItemStack itemStack) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        return hasKey(itemStack, "ench");
    }

    public static ItemStack setContent(ItemStack itemStack, Map<String, NBTTag> content) {
        Validate.notNull(itemStack, "ItemStack can't be null");
        Validate.notNull(content, "Content can't be null");

        Object craftItemStack = asNMSItem(itemStack);
        if (craftItemStack == null) {
            throw new NullPointerException("NMSItem is null");
        }

        Object nbtTag = getNBTTagCompound(craftItemStack);
        if (nbtTag == null) {
            nbtTag = newNBTTagCompound();
        }

        for (Map.Entry<String, NBTTag> entry : content.entrySet()) {
            SET_METHOD.invoke(nbtTag, entry.getKey(), entry.getValue().toNBT());
        }

        return getItemStack(setNBTTagCompound(nbtTag, craftItemStack));
    }

    public static Map<String, NBTTag> getContent(ItemStack itemStack) {
        Validate.notNull(itemStack, "ItemStack can't be null");

        Map<String, NBTTag> content = new HashMap<>();

        Object craftItemStack = asNMSItem(itemStack);
        if (craftItemStack == null) {
            throw new NullPointerException("NMSItem is null");
        }

        Object nbtTag = getNBTTagCompound(craftItemStack);
        if (nbtTag == null) {
            return content;
        }

        for (String key : (Set<String>) NBT_KEYS_METHOD.invoke(nbtTag)) {
            Object object = GET_METHOD.invoke(nbtTag, key);
            NBTType type = NBTType.byNBT(object);

            if (type == null) continue;

            content.put(key, type.fromNBT(object));
        }

        return content;
    }

    public static void setTexture(ItemStack itemStack, SkullData data) {
        if (itemStack.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

            if (data.getType() == SkullData.SkullDataType.NAME) {
                meta.setOwner(data.getValue());
            } else if (data.getType() == SkullData.SkullDataType.URL) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);

                byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", data.getValue()).getBytes());
                profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

                PROFILE_FIELD.set(meta, profile);
            } else if (data.getType() == SkullData.SkullDataType.TEXTURE) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);

                profile.getProperties().put("textures", new Property("textures", data.getValue()));

                PROFILE_FIELD.set(meta, profile);
            }

            itemStack.setItemMeta(meta);
        }
    }

    public static String getTexture(ItemStack itemStack) {
        if (itemStack.getItemMeta() instanceof SkullMeta) {
            GameProfile profile = PROFILE_FIELD.get(itemStack.getItemMeta());

            if (profile == null || profile.getProperties().get("textures") == null) {
                return null;
            }

            for (Property property : profile.getProperties().get("textures")) {
                if (property.getName().equals("textures")) {
                    return property.getValue();
                }
            }
        }

        return null;
    }
}
