package ca.ulaval.ift6002.sputnik.emailsender.notification;

import ca.ulaval.ift6002.sputnik.domain.core.notification.Notification;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import ca.ulaval.ift6002.sputnik.emailsender.JavaxMailSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.mail.MessagingException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JavaxMailSenderStrategyTest {

    private static final String AN_EMAIL = "TEST@ULAVAL.CA";
    private static final String A_MESSAGE = "A MESSAGE";
    private NotificationSenderStrategy emailSenderStrategy;

    @Mock
    private JavaxMailSender emailSender;

    @Mock
    private User user;

    @Mock
    private Notification notification;

    @Before
    public void setUp() {
        when(user.getEmail()).thenReturn(AN_EMAIL);
        when(notification.getMessage()).thenReturn(A_MESSAGE);
    }

    @Test
    public void whenSendingAMessageToAUserShouldSendMessageToUser() throws MessagingException {
        emailSenderStrategy = new JavaxMailSenderStrategy(emailSender);

        emailSenderStrategy.addRecipient(new User(null));
        emailSenderStrategy.send(notification);

        verify(emailSender).send();
    }
}
