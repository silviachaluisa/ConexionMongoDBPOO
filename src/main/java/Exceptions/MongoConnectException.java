package Exceptions;

public class MongoConnectException extends Exception {
    public MongoConnectException(String message) {
        super(message);
    }
    public MongoConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
