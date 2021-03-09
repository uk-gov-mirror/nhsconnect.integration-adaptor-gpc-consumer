package uk.nhs.adaptors.gpc.consumer.gpc.exception;

public class GpConnectException extends RuntimeException {
    public GpConnectException(String message) {
        super(message);
    }

    public GpConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
