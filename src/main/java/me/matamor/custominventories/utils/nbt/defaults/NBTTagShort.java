package me.matamor.custominventories.utils.nbt.defaults;

import me.matamor.custominventories.utils.Reflections;
import me.matamor.custominventories.utils.nbt.NBTTag;
import me.matamor.custominventories.utils.nbt.NBTType;

public class NBTTagShort extends NBTTag<Short> {

    private static final Reflections.MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "e");

    public NBTTagShort(Short value) {
        super(NBTType.SHORT, value);
    }

    public NBTTagShort(Object object) throws RuntimeException {
        super(NBTType.SHORT, (short) GETTER.invoke(object));
    }
}
