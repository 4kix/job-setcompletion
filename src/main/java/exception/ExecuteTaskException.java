package exception;

public class ExecuteTaskException extends RuntimeException {

    public ExecuteTaskException() {
    }

    public ExecuteTaskException(String message) {
        super(message);
    }

    public ExecuteTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteTaskException(Throwable cause) {
        super(cause);
    }
}
