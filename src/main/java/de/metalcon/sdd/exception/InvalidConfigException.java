package de.metalcon.sdd.exception;

/**
 * Thrown in case of invalid configuration.
 */
public class InvalidConfigException extends SddUsageException {

    private static final long serialVersionUID = -4786932491385654014L;

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
