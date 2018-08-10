package me.matamor.custominventories.utils.nbt.defaults;

import me.matamor.custominventories.utils.Reflections;
import me.matamor.custominventories.utils.nbt.NBTTag;
import me.matamor.custominventories.utils.nbt.NBTType;

public class NBTTagDouble extends NBTTag<Double> {

    private static final Reflections.MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "g");

    public NBTTagDouble(Double value) {
        super(NBTType.DOUBLE, value);
    }

    public NBTTagDouble(Object object) throws RuntimeException  {
        super(NBTType.DOUBLE, (double) GETTER.invoke(object));
    }
}
