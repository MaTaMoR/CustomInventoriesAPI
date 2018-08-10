package me.matamor.custominventories.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PrimitiveUtils {

    public static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = Collections.unmodifiableMap(new HashMap<Class<?>, Class<?>>() {{

        put(byte.class, Byte.class);
        put(short.class, Short.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
        put(boolean.class, Boolean.class);
        put(char.class, Character.class);

    }});

    private PrimitiveUtils() {

    }

    public static boolean isInstance(Class<?> clazz, Object object) {
        if (clazz.isInstance(object)) {
            return true;
        } else if (clazz.isPrimitive() || object.getClass().isPrimitive()) {
            if (clazz.isPrimitive() && !object.getClass().isPrimitive()) {
                return object.getClass().isAssignableFrom(PRIMITIVE_TO_WRAPPER.get(clazz));
            } else if(!clazz.isPrimitive() && object.getClass().isPrimitive()) {
                return PRIMITIVE_TO_WRAPPER.get(object.getClass()).isAssignableFrom(clazz);
            }
        }

        return false;
    }
}
