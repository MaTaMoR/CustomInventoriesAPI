package us.swiftex.custominventories.utils;

import org.bukkit.inventory.ItemStack;

public class SkullData {

    public enum SkullDataType {

        TEXTURE,
        URL,
        NAME;

        public static SkullDataType getByName(String name) {
            for(SkullDataType type : values()) {
                if(type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }

            return null;
        }
    }

    private final String value;
    private final SkullDataType type;

    public SkullData(String value, SkullDataType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public SkullDataType getType() {
        return type;
    }

    public void apply(ItemStack itemStack) {
        ItemReflections.setTexture(itemStack, this);
    }
}
