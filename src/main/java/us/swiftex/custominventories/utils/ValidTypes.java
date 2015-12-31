package us.swiftex.custominventories.utils;

public enum ValidTypes {

    STRING(String.class, String.class, "setString", "getString"),
    INTEGER(int.class, Integer.class, "setInt", "setInt"),
    DOUBLE(double.class, Double.class, "setDouble", "getDouble"),
    BOOLEAN(boolean.class, Boolean.class, "setBoolean", "getBoolean"),
    LONG(long.class, Long.class,  "setLong", "getLong"),
    UNKNOWN(null, null, null, null);

    private final Class<?> primitiveClass;
    private final Class<?> objectClass;
    private final String set;
    private final String get;

    private ValidTypes(Class<?> primitiveClass, Class<?> objectClass,  String set, String get) {
        this.primitiveClass = primitiveClass;
        this.objectClass = objectClass;
        this.set = set;
        this.get = get;
    }

    public Class<?> getPrimitiveClass() {
        return primitiveClass;
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

    public String getSet() {
        return set;
    }

    public String getGet() {
        return get;
    }

    public static ValidTypes byObject(Object object) {
        for(ValidTypes validType : values()) {

            if(validType.getPrimitiveClass() == object.getClass() || validType.getObjectClass() == object.getClass()) {
                return validType;
            }
        }

        return UNKNOWN;
    }
}
