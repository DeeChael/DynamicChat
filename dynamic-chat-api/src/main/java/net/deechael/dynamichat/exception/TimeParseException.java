package net.deechael.dynamichat.exception;

public class TimeParseException extends RuntimeException {

    public TimeParseException() {
        super();
    }

    public TimeParseException(String message) {
        super(message);
    }

    public TimeParseException(String message, Exception extra) {
        super(message, extra);
    }

    public TimeParseException(Exception extra) {
        super(extra);
    }

}
