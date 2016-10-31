package us.swiftex.custominventories.utils.nbt;

import us.swiftex.custominventories.utils.Reflections;
import us.swiftex.custominventories.utils.Reflections.ConstructorInvoker;
import us.swiftex.custominventories.utils.Reflections.MethodInvoker;
import us.swiftex.custominventories.utils.Validate;
import us.swiftex.custominventories.utils.nbt.defaults.*;

import java.util.List;

public abstract class NBTType<V, T extends NBTTag<V>> {

    public static final NBTType<Double, NBTTagDouble> DOUBLE = new NBTType<Double, NBTTagDouble>(double.class, Double.class, "NBTTagDouble") {
        @Override
        public NBTTagDouble newInstance(Double value) {
            return new NBTTagDouble(value);
        }

        @Override
        public NBTTagDouble fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagDouble(object);
        }
    };

    public static final NBTType<Byte, NBTTagByte> BYTE = new NBTType<Byte, NBTTagByte>(byte.class, Byte.class, "NBTTagByte") {
        @Override
        public NBTTagByte newInstance(Byte value) {
            return new NBTTagByte(value);
        }

        @Override
        public NBTTagByte fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagByte(object);
        }
    };

    public static final NBTType<Byte[], NBTTagByteArray> BYTE_ARRAY = new NBTType<Byte[], NBTTagByteArray>(byte[].class, Byte[].class, "NBTTagByteArray") {
        @Override
        public NBTTagByteArray newInstance(Byte[] value) {
            return new NBTTagByteArray(value);
        }

        @Override
        public NBTTagByteArray fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagByteArray(object);
        }
    };

    public static final NBTType<Boolean, NBTTagBoolean> BOOLEAN = new NBTType<Boolean, NBTTagBoolean>(boolean.class, Boolean.class, "NBTTagByte", Reflections.getConstructor("{nms}.NBTTagByte", byte.class)) {

        private final MethodInvoker GETTER = Reflections.getMethod("{nms}.NBTBase$NBTNumber", "f");

        @Override
        public NBTTagBoolean newInstance(Boolean value) {
            return new NBTTagBoolean(value);
        }

        @Override
        public NBTTagBoolean fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagBoolean(object);
        }

        @Override
        public Object toNBT(Boolean object) {
            return constructor.invoke((byte) (object ? 1 : 0));
        }

        @Override
        public boolean isValidNBT(Object object) {
            if(object != null && object.getClass().equals(minecraftClass)) {
                byte value = (byte) GETTER.invoke(object);
                return value == 1 || value == 0;
            }

            return false;
        }
    };

    public static final NBTType<Integer, NBTTagInt> INT = new NBTType<Integer, NBTTagInt>(int.class, Integer.class, "NBTTagInt") {
        @Override
        public NBTTagInt newInstance(Integer value) {
            return new NBTTagInt(value);
        }

        @Override
        public NBTTagInt fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagInt(object);
        }
    };

    public static final NBTType<Integer[], NBTTagIntArray> INT_ARRAY = new NBTType<Integer[], NBTTagIntArray>(int[].class, Integer[].class, "NBTTagIntArray") {
        @Override
        public NBTTagIntArray newInstance(Integer[] value) {
            return new NBTTagIntArray(value);
        }

        @Override
        public NBTTagIntArray fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagIntArray(object);
        }
    };

    public static final NBTType<List<NBTTag>, NBTTagList> LIST = new NBTType<List<NBTTag>, NBTTagList>(null, null, "NBTTagList", Reflections.getConstructor("{nms}.NBTTagList")) {

        private final MethodInvoker ADD = Reflections.getMethod(minecraftClass, "add", Reflections.getClass("{nms}.NBTBase"));

        @Override
        public NBTTagList newInstance(List<NBTTag> value) {
            return new NBTTagList(value);
        }

        @Override
        public NBTTagList fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagList(object);
        }

        @Override
        public Object toNBT(List<NBTTag> value) {
            Object object = constructor.invoke();

            for(NBTTag tag : value) ADD.invoke(object, tag.toNBT());

            return object;
        }

        @Override
        public boolean isValid(Object object) {
            if (object == null) return false;

            try {
                ((List<NBTTag>) object).size(); //Just cast it and use the method .size() to don't have to assign it to a variable
                return true;
            } catch (ClassCastException e) {
                return false;
            }
        }
    };

    public static final NBTType<Long, NBTTagLong> LONG = new NBTType<Long, NBTTagLong>(long.class, Long.class, "NBTTagLong") {
        @Override
        public NBTTagLong newInstance(Long value) {
            return new NBTTagLong(value);
        }

        @Override
        public NBTTagLong fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagLong(object);
        }
    };

    public static final NBTType<Short, NBTTagShort> SHORT = new NBTType<Short, NBTTagShort>(short.class, Short.class, "NBTTagShort") {
        @Override
        public NBTTagShort newInstance(Short value) {
            return new NBTTagShort(value);
        }

        @Override
        public NBTTagShort fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagShort(object);
        }
    };

    public static final NBTType<String, NBTTagString> STRING = new NBTType<String, NBTTagString>(String.class, String.class, "NBTTagString") {
        @Override
        public NBTTagString newInstance(String value) {
            return new NBTTagString(value);
        }

        @Override
        public NBTTagString fromNBT(Object object) {
            Validate.isTrue(isValidNBT(object), "Not valid NBT");
            return new NBTTagString(object);
        }
    };

    protected final Class<?> primitiveClass;
    protected final Class<V> wrapperClass;

    protected final Class<?> minecraftClass;
    protected final ConstructorInvoker constructor;

    private NBTType(Class<?> primitiveClass, Class<V> wrapperClass, String minecraftClass) {
        this(primitiveClass, wrapperClass, minecraftClass, Reflections.getConstructor("{nms}." + minecraftClass, primitiveClass));
    }

    private NBTType(Class<?> primitiveClass, Class<V> wrapperClass, String minecraftClass, ConstructorInvoker constructor) {
        this.primitiveClass = primitiveClass;
        this.wrapperClass = wrapperClass;

        this.minecraftClass = Reflections.getMinecraftClass(minecraftClass);
        this.constructor = constructor;
    }

    public boolean isValid(Object object) {
        return !(object == null) && (object.getClass() == primitiveClass || object.getClass() == wrapperClass);
    }

    public boolean isValidNBT(Object object) {
        return !(object == null) && object.getClass() == minecraftClass;
    }

    public abstract T newInstance(V value);

    public abstract T fromNBT(Object object);

    public Object toNBT(V value) {
        return constructor.invoke(value);
    }

    public static NBTType[] values() {
        return new NBTType[] { DOUBLE, BOOLEAN, BYTE, BYTE_ARRAY, INT, INT_ARRAY, LIST, LONG, SHORT, STRING };
    }

    public static NBTType byObject(Object object) {
        if(object == null) return null;

        for(NBTType type : values()) {
            if(type.isValid(object)) return type;
        }

        return null;
    }

    public static NBTType byNBT(Object object) {
        if(object == null) return null;

        for(NBTType type : values()) {
            if(type.isValidNBT(object)) return type;
        }

        return null;
    }
}
