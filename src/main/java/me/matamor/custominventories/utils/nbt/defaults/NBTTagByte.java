package me.matamor.custominventories.utils.nbt.defaults;

import me.matamor.custominventories.utils.Reflections;
import me.matamor.custominventories.utils.nbt.NBTTag;
import me.matamor.custominventories.utils.nbt.NBTType;

public class NBTTagByte extends NBTTag<Byte> {

    private static final Reflections.MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "f");

    public NBTTagByte(Byte value) {
        super(NBTType.BYTE, value);
    }

    public NBTTagByte(Object object) throws RuntimeException {
        super(NBTType.BYTE, (byte) GETTER.invoke(object));
    }
}
