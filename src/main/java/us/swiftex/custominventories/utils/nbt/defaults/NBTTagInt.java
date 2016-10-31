package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagInt extends NBTTag<Integer> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "d");

    public NBTTagInt(Integer value) {
        super(NBTType.INT, value);
    }

    public NBTTagInt(Object object) throws RuntimeException {
        super(NBTType.INT, (int) GETTER.invoke(object));
    }
}
