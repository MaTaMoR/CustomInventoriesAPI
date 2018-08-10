package me.matamor.custominventories.utils.nbt.defaults;

import me.matamor.custominventories.utils.Reflections;
import me.matamor.custominventories.utils.nbt.NBTTag;
import me.matamor.custominventories.utils.nbt.NBTType;

public class NBTTagInt extends NBTTag<Integer> {

    private static final Reflections.MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "d");

    public NBTTagInt(Integer value) {
        super(NBTType.INT, value);
    }

    public NBTTagInt(Object object) throws RuntimeException {
        super(NBTType.INT, (int) GETTER.invoke(object));
    }
}
