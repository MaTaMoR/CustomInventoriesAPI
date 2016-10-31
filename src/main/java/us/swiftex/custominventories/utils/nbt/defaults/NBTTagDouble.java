package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagDouble extends NBTTag<Double> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "g");

    public NBTTagDouble(Double value) {
        super(NBTType.DOUBLE, value);
    }

    public NBTTagDouble(Object object) throws RuntimeException  {
        super(NBTType.DOUBLE, (double) GETTER.invoke(object));
    }
}
