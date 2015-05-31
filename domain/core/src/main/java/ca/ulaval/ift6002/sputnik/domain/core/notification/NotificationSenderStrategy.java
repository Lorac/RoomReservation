package ca.ulaval.ift6002.sputnik.domain.core.notification;

import ca.ulaval.ift6002.sputnik.domain.core.user.User;

public interface NotificationSenderStrategy {

    void addRecipient(User user);

    void send(Notification notification);
}
