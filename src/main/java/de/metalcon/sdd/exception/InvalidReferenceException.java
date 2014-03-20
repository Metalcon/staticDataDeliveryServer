package de.metalcon.sdd.exception;

public class InvalidReferenceException extends SddUsageException {

    private static final long serialVersionUID = -6802166774771080639L;

    public InvalidReferenceException(
            String message) {
        super(message);
    }

}
