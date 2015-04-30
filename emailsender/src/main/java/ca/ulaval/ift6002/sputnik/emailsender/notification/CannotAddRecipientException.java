package ca.ulaval.ift6002.sputnik.emailsender.notification;

public class CannotAddRecipientException extends RuntimeException {
    public CannotAddRecipientException(String msg, Exception e) {
        super(msg, e);
    }
}
