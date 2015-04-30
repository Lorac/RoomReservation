package ca.ulaval.ift6002.sputnik.emailsender.notification;

public class CannotSendEmailException extends RuntimeException {
    public CannotSendEmailException(String msg, Exception e) {
        super(msg, e);
    }
}
