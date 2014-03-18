package de.metalcon.sdd.config;

/**
 * Convenience class for entity types used in configuration.
 */
public class ConfigType {

    /**
     * The suffix on an type do denote an array.
     */
    public static final String ARRAY_SUFFIX = "[]";

    /**
     * The actual type. Either name of the entity it references or one of the
     * primitive type name. Does not have ARRAY_SUFFIX at its end. Use isArray
     * to check whether this type is an array.
     */
    private String type;

    /**
     * Whether this type is an array.
     */
    private boolean isArray;

    /**
     * Whether this type is a primitive type.
     */
    private boolean isPrimitive;

    /**
     * Construct a type from a string representation of a type.
     * 
     * @param type
     *            The string representation of the type to be created.
     */
    public ConfigType(
            String type) {
        if (type == null) {
            throw new IllegalArgumentException("type was null");
        }

        this.type = type;

        if (type.length() >= ARRAY_SUFFIX.length()
                && type.substring(type.length() - ARRAY_SUFFIX.length())
                        .equals(ARRAY_SUFFIX)) {
            this.type =
                    type.substring(0, type.length() - ARRAY_SUFFIX.length());
            isArray = true;
        } else {
            isArray = false;
        }

        if (type.equals("String")) {
            isPrimitive = true;
        } else {
            isPrimitive = false;
        }
    }

    /**
     * @return The type this ConfigType represents as string. Does not contain
     *         ARRAY_SUFFIX at its end. Use isArray() to check whether this type
     *         is an array.
     */
    public String getType() {
        return type;
    }

    /**
     * @return <code>true</code> if this type is an array, <code>false</code> if
     *         not.
     */
    public boolean isArray() {
        return isArray;
    }

    /**
     * @return <code>true</code> if this type has primitive type,
     *         <code>false</code> if not.
     */
    public boolean isPrimitive() {
        return isPrimitive;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        ConfigType o = (ConfigType) other;
        return type.equals(o.type) && isArray == o.isArray
                && isPrimitive == o.isPrimitive;
    }

    @Override
    public int hashCode() {
        int hash = 54294;
        int mult = 359;

        hash = hash * mult + type.hashCode();
        hash = hash * mult + (isArray ? 0 : 1);
        hash = hash * mult + (isPrimitive ? 0 : 1);

        return hash;
    }

}
