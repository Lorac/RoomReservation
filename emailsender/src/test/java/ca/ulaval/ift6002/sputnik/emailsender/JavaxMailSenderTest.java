package ca.ulaval.ift6002.sputnik.emailsender;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Iterator;
import java.util.Properties;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class JavaxMailSenderTest {

    private static final int SMTP_PORT = 8080;

    private SimpleSmtpServer server;

    private JavaxMailSender emailSender;

    @Mock
    private Properties properties;

    @Before
    public void setUp() {
        server = SimpleSmtpServer.start(SMTP_PORT);

        emailSender = new JavaxMailSender(getMailProperties(SMTP_PORT));
    }

    @After
    public void tearDown() {
        server.stop();
    }

    @Test
    public void whenSendingAMessageItShouldSendTheMessage() throws MessagingException {
        sendMessage("sender@here.com", "Test", "Test Body", "receiver@there.com");

        assertEquals(1, server.getReceivedEmailSize());
        Iterator emailIter = server.getReceivedEmail();
        SmtpMessage email = (SmtpMessage) emailIter.next();
        assertTrue(email.getHeaderValue("Subject").equals("Test"));
        assertTrue(email.getBody().equals("Test Body"));
    }

    private Properties getMailProperties(int port) {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", "localhost");
        mailProps.setProperty("mail.smtp.port", "" + port);
        mailProps.setProperty("mail.smtp.sendpartial", "true");
        return mailProps;
    }

    private void sendMessage(String from, String subject, String body, String to) throws MessagingException {
        createMessage(from, to, subject, body);
        emailSender.send();
    }

    private void createMessage(
            String from, String to, String subject, String body) throws MessagingException {
        emailSender.setFrom(new InternetAddress(from));
        emailSender.setSubject(subject);
        emailSender.setBody(body);
        emailSender.addRecipient(MimeMessage.RecipientType.TO, to);
    }
}

