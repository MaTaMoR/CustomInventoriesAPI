package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagByte extends NBTTag<Byte> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "f");

    public NBTTagByte(Byte value) {
        super(NBTType.BYTE, value);
    }

    public NBTTagByte(Object object) throws RuntimeException {
        super(NBTType.BYTE, (byte) GETTER.invoke(object));
    }
}
