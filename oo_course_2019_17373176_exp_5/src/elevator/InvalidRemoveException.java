package elevator;

public class InvalidRemoveException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidRemoveException(String message) {
        super(message);
    }
}