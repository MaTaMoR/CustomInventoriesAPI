package us.swiftex.custominventories.utils;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.swiftex.custominventories.CustomInventories;

import java.lang.reflect.Method;
import java.util.Collection;

//This class isn't main, it's from ChestCommands, all credits go to the author, ill make main in the future, now im just too lazy.

public class AttributeRemover {

    private static boolean useItemFlags;
    private static boolean useReflection;

    // Reflection stuff
    private static Class<?> nbtTagCompoundClass;
    private static Class<?> nbtTagListClass;
    private static Method asNmsCopyMethod; // static
    private static Method asCraftMirrorMethod; // static
    private static Method hasTagMethod;
    private static Method getTagMethod;
    private static Method setTagMethod;
    private static Method nbtSetMethod;


    static {
        if (Utils.isClassLoaded("org.bukkit.inventory.ItemFlag")) {
            // We can use the new Bukkit API (1.8.3+)
            useItemFlags = true;

        } else {

            try {
                // Try to get the NMS methods and classes
                nbtTagCompoundClass = Reflections.getMinecraftClass("NBTTagCompound");
                nbtTagListClass = Reflections.getMinecraftClass("NBTTagList");
                Class<?> nmsItemStackClass = Reflections.getMinecraftClass("ItemStack");

                asNmsCopyMethod = Reflections.getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class);
                asCraftMirrorMethod = Reflections.getMinecraftClass("inventory.CraftItemStack").getMethod("asCraftMirror", nmsItemStackClass);

                hasTagMethod = nmsItemStackClass.getMethod("hasTag");
                getTagMethod = nmsItemStackClass.getMethod("getTag");
                setTagMethod = nmsItemStackClass.getMethod("setTag", nbtTagCompoundClass);

                nbtSetMethod = nbtTagCompoundClass.getMethod("set", String.class, Reflections.getMinecraftClass("NBTBase"));

                useReflection = true;

            } catch (Exception e) {
                CustomInventories.getPlugin().getLogger().info("Could not enable the attribute remover for this version (" + e + "). Attributes will show up on items.");
            }
        }
    }

    public static ItemStack hideAttributes(ItemStack item) {
        if (item == null) {
            return null;
        }

        if (useItemFlags) {
            ItemMeta meta = item.getItemMeta();

            if (isNullOrEmpty(meta.getItemFlags())) {
                meta.addItemFlags(ItemFlag.values());
                item.setItemMeta(meta);
            }

            return item;

        } else if (useReflection) {
            try {

                Object craftItemStack = asNmsCopyMethod.invoke(null, item);
                if (craftItemStack == null) {
                    return item;
                }

                Object nbtCompound;
                if ((Boolean) hasTagMethod.invoke(craftItemStack)) {
                    nbtCompound = getTagMethod.invoke(craftItemStack);
                } else {
                    nbtCompound = nbtTagCompoundClass.newInstance();
                    setTagMethod.invoke(craftItemStack, nbtCompound);
                }

                if (nbtCompound == null) {
                    return item;
                }

                Object nbtList = nbtTagListClass.newInstance();
                nbtSetMethod.invoke(nbtCompound, "AttributeModifiers", nbtList);
                return (ItemStack) asCraftMirrorMethod.invoke(null, craftItemStack);

            } catch (Exception ignored) {	}
        }

        return item;
    }

    private static boolean isNullOrEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

}