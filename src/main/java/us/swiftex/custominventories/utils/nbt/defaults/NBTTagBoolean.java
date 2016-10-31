package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagBoolean extends NBTTag<Boolean> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "f");

    public NBTTagBoolean(Boolean value) {
        super(NBTType.BOOLEAN, value);
    }

    public NBTTagBoolean(Object object) throws RuntimeException {
        super(NBTType.BOOLEAN, (((byte) GETTER.invoke(object)) != 0));
    }
}
