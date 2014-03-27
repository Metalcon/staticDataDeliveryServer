package de.metalcon.sdd.exception;

/**
 * General exception class for all exceptions thrown by Sdd.
 * 
 * Only immediate subclasses of this should be SddInternalServerException and
 * SddUsageException, other Exceptions should subclass those.
 */
public abstract class SddException extends RuntimeException {

    private static final long serialVersionUID = -2537157851779990259L;

    public SddException() {
        super();
    }

    public SddException(
            String message) {
        super(message);
    }

    public SddException(
            Throwable cause) {
        super(cause);
    }

    public SddException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

}
