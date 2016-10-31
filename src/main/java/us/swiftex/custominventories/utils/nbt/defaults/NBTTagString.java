package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagString extends NBTTag<String> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTTagString", "a_");

    public NBTTagString(String value) {
        super(NBTType.STRING, value);
    }

    public NBTTagString(Object value) throws RuntimeException {
        super(NBTType.STRING, (String) GETTER.invoke(value));
    }
}
