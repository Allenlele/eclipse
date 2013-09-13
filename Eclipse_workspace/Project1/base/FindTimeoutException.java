package base;

public class FindTimeoutException extends Exception {

    private static final long serialVersionUID = 8036582355770749922L;

    public FindTimeoutException() {
    }

    public FindTimeoutException(String message) {
        super(message);
    }

    public FindTimeoutException(Throwable cause) {
        super(cause);
    }

    public FindTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

}
