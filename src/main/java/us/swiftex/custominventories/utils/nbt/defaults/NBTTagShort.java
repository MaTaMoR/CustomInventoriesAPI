package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagShort extends NBTTag<Short> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "e");

    public NBTTagShort(Short value) {
        super(NBTType.SHORT, value);
    }

    public NBTTagShort(Object object) throws RuntimeException {
        super(NBTType.SHORT, (short) GETTER.invoke(object));
    }
}
