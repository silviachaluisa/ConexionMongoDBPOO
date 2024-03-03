package Exceptions;

public class MongoOperationException extends Exception{
    public MongoOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    public MongoOperationException(String message) {
        super(message);
    }
}
