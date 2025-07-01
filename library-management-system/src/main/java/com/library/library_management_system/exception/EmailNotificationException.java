package com.library.library_management_system.exception;

/**
 * Exception thrown when email notification fails to be sent
 */
public class EmailNotificationException extends RuntimeException {

    public EmailNotificationException() {
        super("Failed to send email notification");
    }

    public EmailNotificationException(String message) {
        super(message);
    }

    public EmailNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotificationException(Throwable cause) {
        super("Failed to send email notification", cause);
    }
}