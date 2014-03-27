package de.metalcon.sdd.exception;

public abstract class SddUsageException extends SddException {

    private static final long serialVersionUID = 2738900472360915340L;

    public SddUsageException() {
        super();
    }

    public SddUsageException(
            String message) {
        super(message);
    }

    public SddUsageException(
            Throwable cause) {
        super(cause);
    }

    public SddUsageException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

}
