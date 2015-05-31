package ca.ulaval.ift6002.sputnik.domain.core.user;

import ca.ulaval.ift6002.sputnik.domain.core.notification.Notification;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationSenderStrategy;

import javax.persistence.Embeddable;

@Embeddable
public class User {

    private String email;

    public User(String email) {
        this.email = email;
    }

    protected User() {
    }

    public void notify(NotificationSenderStrategy notificationSender, Notification notification) {
        notificationSender.addRecipient(this);
        notificationSender.send(notification);
    }

    public String getEmail() {
        return email;
    }
}
