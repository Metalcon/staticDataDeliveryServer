package de.metalcon.sdd;

public class ExceptionFactory {

    public static IllegalArgumentException createEmptyIdException() {
        return new IllegalArgumentException("Empty Id.");
    }

    public static IllegalArgumentException createInvalidNodeTypeException() {
        return new IllegalArgumentException("Invalid NodeType.");
    }

    public static IllegalArgumentException createInvalidRelationException() {
        return new IllegalArgumentException("Invalid Relation.");
    }

    public static IllegalArgumentException createInvalidPropertyException(
            String message) {
        return new IllegalArgumentException("Invalid Property: " + message);
    }

    public static IllegalArgumentException createInvalidDetailException() {
        return new IllegalArgumentException("Invalid Detail.");
    }
}
