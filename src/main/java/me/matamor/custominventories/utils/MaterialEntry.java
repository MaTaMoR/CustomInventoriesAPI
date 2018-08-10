package me.matamor.custominventories.utils;

import me.matamor.custominventories.utils.amount.Amount;
import me.matamor.custominventories.utils.amount.SimpleAmount;
import org.bukkit.Material;

public class MaterialEntry {

    private final Material material;
    private final Amount amount;
    private final short dataValue;

    public MaterialEntry(Material material, int amount, short dataValue) {
        this(material, new SimpleAmount(amount), dataValue);
    }

    public MaterialEntry(Material material, Amount amount, short dataValue) {
        this.material = material;
        this.amount = amount;
        this.dataValue = dataValue;
    }

    public Material getMaterial() {
        return material;
    }

    public Amount getAmount() {
        return amount;
    }

    public short getDataValue() {
        return dataValue;
    }
}
