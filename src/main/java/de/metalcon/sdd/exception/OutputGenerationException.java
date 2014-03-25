package de.metalcon.sdd.exception;

public class OutputGenerationException extends SddInternalServerException {

    private static final long serialVersionUID = 8623731891433162966L;

    public OutputGenerationException() {
        super();
    }

    public OutputGenerationException(
            String message) {
        super(message);
    }

    public OutputGenerationException(
            Throwable cause) {
        super(cause);
    }

    public OutputGenerationException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

}
