package de.metalcon.sdd.exception;

public abstract class SddInternalServerException extends SddException {

    private static final long serialVersionUID = 6876913703865058572L;

    public SddInternalServerException() {
        super();
    }

    public SddInternalServerException(
            String message) {
        super(message);
    }

    public SddInternalServerException(
            Throwable cause) {
        super(cause);
    }

    public SddInternalServerException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

}
