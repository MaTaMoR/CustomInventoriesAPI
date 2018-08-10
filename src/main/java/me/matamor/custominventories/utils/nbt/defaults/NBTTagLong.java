package me.matamor.custominventories.utils.nbt.defaults;

import me.matamor.custominventories.utils.Reflections;
import me.matamor.custominventories.utils.nbt.NBTTag;
import me.matamor.custominventories.utils.nbt.NBTType;

public class NBTTagLong extends NBTTag<Long> {

    private static final Reflections.MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "c");

    public NBTTagLong(Long value) {
        super(NBTType.LONG, value);
    }

    public NBTTagLong(Object object) throws RuntimeException {
        super(NBTType.LONG, (long) GETTER.invoke(object));
    }
}
