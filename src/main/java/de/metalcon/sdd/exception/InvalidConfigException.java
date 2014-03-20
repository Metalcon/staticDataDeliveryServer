package de.metalcon.sdd.exception;

/**
 * Thrown when an Exception related to invalid configuration occurs.
 */
public class InvalidConfigException extends SddUsageException {

    private static final long serialVersionUID = 8829689568298152661L;

    public InvalidConfigException(
            String message) {
        super(message);
    }

    public InvalidConfigException(
            Throwable cause) {
        super(cause);
    }

    public InvalidConfigException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

}
