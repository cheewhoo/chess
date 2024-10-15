package dataaccess;

public class UnauthorizedException extends Exception {
    // Constructor with only a message
    public UnauthorizedException(String message) {
        super(message);
    }

    // Overloaded constructor with message and cause (Throwable)
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
