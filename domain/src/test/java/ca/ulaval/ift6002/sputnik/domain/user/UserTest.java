package ca.ulaval.ift6002.sputnik.domain.user;

import ca.ulaval.ift6002.sputnik.domain.notification.Notification;
import ca.ulaval.ift6002.sputnik.domain.notification.NotificationSenderStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    private static final String AN_EMAIL = "home@ulaval.ca";
    private User user;

    @Mock
    private Notification notification;

    @Mock
    private NotificationSenderStrategy notificationSender;

    @Test
    public void notifyUser() {
        user = new User(AN_EMAIL);

        user.notify(notificationSender, notification);

        verify(notificationSender).send(notification);
    }
}
