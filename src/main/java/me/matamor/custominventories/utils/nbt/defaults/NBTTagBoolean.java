package me.matamor.custominventories.utils.nbt.defaults;

import me.matamor.custominventories.utils.Reflections;
import me.matamor.custominventories.utils.nbt.NBTTag;
import me.matamor.custominventories.utils.nbt.NBTType;

public class NBTTagBoolean extends NBTTag<Boolean> {

    private static final Reflections.MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "f");

    public NBTTagBoolean(Boolean value) {
        super(NBTType.BOOLEAN, value);
    }

    public NBTTagBoolean(Object object) throws RuntimeException {
        super(NBTType.BOOLEAN, (((byte) GETTER.invoke(object)) != 0));
    }
}
