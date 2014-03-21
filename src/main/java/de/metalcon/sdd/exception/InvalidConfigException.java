package de.metalcon.sdd.exception;

/**
 * Thrown in case of invlaid configuration.
 */
public class InvalidConfigException extends SddUsageException {

    private static final long serialVersionUID = -4786932491385654014L;

    public InvalidConfigException(
            String message) {
        super(message);
    }

}
