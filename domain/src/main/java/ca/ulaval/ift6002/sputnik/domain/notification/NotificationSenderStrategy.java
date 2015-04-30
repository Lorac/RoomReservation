package ca.ulaval.ift6002.sputnik.domain.notification;

import ca.ulaval.ift6002.sputnik.domain.user.User;

public interface NotificationSenderStrategy {

    void addRecipient(User user);

    void send(Notification notification);
}
