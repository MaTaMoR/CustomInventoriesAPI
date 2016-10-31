package us.swiftex.custominventories.utils.nbt.defaults;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.nbt.NBTTag;
import us.swiftex.custominventories.utils.nbt.NBTType;

public class NBTTagIntArray extends NBTTag<Integer[]> {

    private static final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTTagIntArray", "c");

    public NBTTagIntArray(int[] value) {
        super(NBTType.INT_ARRAY, fromPrimitive(value));
    }

    public NBTTagIntArray(Object object) throws RuntimeException {
        super(NBTType.INT_ARRAY, fromPrimitive((int[]) GETTER.invoke(object)));
    }

    private static Integer[] fromPrimitive(int[] ints) {
        Integer[] casted = new Integer[ints.length];

        for(int i = 0; ints.length > i; i++) {
            casted[i] = ints[i];
        }

        return casted;
    }
}
