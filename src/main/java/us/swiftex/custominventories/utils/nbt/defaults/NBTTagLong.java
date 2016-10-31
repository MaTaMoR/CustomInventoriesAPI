package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagLong extends NBTTag<Long> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "c");

    public NBTTagLong(Long value) {
        super(NBTType.LONG, value);
    }

    public NBTTagLong(Object object) throws RuntimeException {
        super(NBTType.LONG, (long) GETTER.invoke(object));
    }
}
