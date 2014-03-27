package de.metalcon.sdd.exception;

public class InvalidPropertyException extends SddUsageException {

    private static final long serialVersionUID = 4742284846810735520L;

    public InvalidPropertyException() {
        super();
    }

    public InvalidPropertyException(
            String message) {
        super(message);
    }

    public InvalidPropertyException(
            Throwable cause) {
        super(cause);
    }

    public InvalidPropertyException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

}
