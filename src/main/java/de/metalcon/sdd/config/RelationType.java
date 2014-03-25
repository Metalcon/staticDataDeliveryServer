package de.metalcon.sdd.config;

public class RelationType {

    public static final String ARRAY_SUFFIX = "[]";

    private String type;

    private boolean isArray;

    /* package */RelationType(
            String relationType) {
        if (relationType == null) {
            throw new IllegalArgumentException("relationType was null.");
        }

        type = relationType;

        if (type.length() > ARRAY_SUFFIX.length()
                && type.substring(type.length() - ARRAY_SUFFIX.length())
                        .equals(ARRAY_SUFFIX)) {
            type = type.substring(0, type.length() - ARRAY_SUFFIX.length());
            isArray = true;
        } else {
            isArray = false;
        }
    }

    public String getType() {
        return type;
    }

    public boolean isArray() {
        return isArray;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        RelationType o = (RelationType) other;
        return type.equals(o.type) && isArray == o.isArray;
    }

    @Override
    public int hashCode() {
        int hash = 54294;
        int mult = 359;

        hash = hash * mult + type.hashCode();
        hash = hash * mult + (isArray ? 0 : 1);

        return hash;
    }

}
