package ca.ulaval.ift6002.sputnik.emailsender.notification;

import ca.ulaval.ift6002.sputnik.domain.core.notification.Notification;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import ca.ulaval.ift6002.sputnik.emailsender.JavaxMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaxMailSenderStrategy implements NotificationSenderStrategy {

    private final JavaxMailSender emailSender;

    public JavaxMailSenderStrategy(JavaxMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void addRecipient(User user) {
        try {
            emailSender.addRecipient(MimeMessage.RecipientType.TO, user.getEmail());
        } catch (MessagingException e) {
            throw new CannotAddRecipientException(String.format("Cannot add %s as a recipient", user.getEmail()), e);
        }
    }

    @Override
    public void send(Notification notification) {
        try {
            emailSender.setFrom(new InternetAddress("noreply@ca.ulaval.ift6002.sputnik.com"));
            emailSender.setSubject(notification.getSubject());
            emailSender.setBody(notification.getMessage());
            emailSender.send();
        } catch (MessagingException e) {
            throw new CannotSendEmailException("Couldn't send the email", e);
        }
    }
}
