package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagByteArray extends NBTTag<Byte[]> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTTagByteArray", "c");

    public NBTTagByteArray(byte[] value) {
        super(NBTType.BYTE_ARRAY, fromPrimitive(value));
    }

    public NBTTagByteArray(Object object) throws RuntimeException {
        super(NBTType.BYTE_ARRAY, fromPrimitive((byte[]) GETTER.invoke(object)));
    }

    private static Byte[] fromPrimitive(byte[] bytes) {
        Byte[] casted = new Byte[bytes.length];

        for(int i = 0; bytes.length > i; i++) casted[i] = bytes[i];

        return casted;
    }
}
