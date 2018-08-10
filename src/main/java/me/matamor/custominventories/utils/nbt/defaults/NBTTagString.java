package me.matamor.custominventories.utils.nbt.defaults;

import me.matamor.custominventories.utils.Reflections;
import me.matamor.custominventories.utils.nbt.NBTTag;
import me.matamor.custominventories.utils.nbt.NBTType;

public class NBTTagString extends NBTTag<String> {

    private static final Reflections.MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTTagString", "a_");

    public NBTTagString(String value) {
        super(NBTType.STRING, value);
    }

    public NBTTagString(Object value) throws RuntimeException {
        super(NBTType.STRING, (String) GETTER.invoke(value));
    }
}
