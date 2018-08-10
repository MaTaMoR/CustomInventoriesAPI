package me.matamor.custominventories.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class SkullData {

    public enum SkullDataType {

        TEXTURE,
        URL,
        NAME;

        public static SkullDataType getByName(String name) {
            for (SkullDataType type : values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }

            return null;
        }
    }

    @Getter
    private final SkullDataType type;

    @Getter
    private final String value;

    @Deprecated
    public SkullData(String value, SkullDataType type) {
        this(type, value);
    }

    public void apply(ItemStack itemStack) {
        ItemReflections.setTexture(itemStack, this);
    }
}
