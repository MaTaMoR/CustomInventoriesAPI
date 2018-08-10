package me.matamor.custominventories.utils;

public class CastUtils {

    public static int asInt(Object object) {
        if (object instanceof Integer) {
            return (Integer) object;
        } else if (object instanceof String) {
            try {
                return Integer.parseInt((String) object);
            } catch (NumberFormatException ignored) {

            }
        } else if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        throw new FormatException(object + " is not valid format");
    }

    public static double asDouble(Object object) {
        if (object instanceof Double) {
            return (Double) object;
        } else if (object instanceof String) {
            try {
                return Double.parseDouble((String) object);
            } catch (NumberFormatException ignored) {

            }
        } else if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        throw new FormatException(object + " is not valid format");
    }

    public static long asLong(Object object) {
        if (object instanceof Long) {
            return (Long) object;
        } else if (object instanceof String) {
            try {
                return Long.parseLong((String) object);
            } catch (NumberFormatException ignored) {

            }
        } else if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        throw new FormatException(object + " is not valid format");
    }

    public static float asFloat(Object object) {
        if (object instanceof Float) {
            return (Float) object;
        } else if (object instanceof String) {
            try {
                return Float.parseFloat((String) object);
            } catch (NumberFormatException ignored) {

            }
        } else if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        throw new FormatException(object + " is not valid format");
    }

    public static boolean asBoolean(Object object) {
        if (object instanceof Boolean) {
            return (Boolean) object;
        } else if (object instanceof String) {
            if (Boolean.TRUE.toString().equalsIgnoreCase((String) object)) {
                return true;
            } else if (Boolean.FALSE.toString().equalsIgnoreCase((String) object)) {
                return false;
            }
        }

        throw new FormatException(object + " is not valid format");
    }

    public static String asString(Object object) {
        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Integer) {
            return String.valueOf(object);
        } else if (object instanceof Double) {
            return String.valueOf(object);
        } else if (object instanceof Boolean) {
            return String.valueOf(object);
        } else if (object instanceof Long) {
            return String.valueOf(object);
        }

        throw new FormatException(object + " is not valid format");
    }

    public static class FormatException extends RuntimeException {

        public FormatException(String message) {
            super(message);
        }
    }
}
