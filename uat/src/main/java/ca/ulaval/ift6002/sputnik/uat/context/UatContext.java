package ca.ulaval.ift6002.sputnik.uat.context;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.context.ContextBase;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationFactory;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequestFactory;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequestRepository;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomRepository;
import ca.ulaval.ift6002.sputnik.emailsender.JavaxMailSender;
import ca.ulaval.ift6002.sputnik.emailsender.notification.JavaxMailSenderStrategy;
import ca.ulaval.ift6002.sputnik.persistence.memory.InMemoryRoomRepository;
import ca.ulaval.ift6002.sputnik.persistence.memory.RoomRequestRepositoryInMemory;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindFirstRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.sorting.DefaultStrategy;
import ca.ulaval.ift6002.sputnik.uat.fakes.FakeRoomRequestFactory;
import com.dumbster.smtp.SimpleSmtpServer;
import org.jbehave.core.annotations.AfterStories;

import java.util.Properties;

public class UatContext extends ContextBase {

    private static final int SMTP_PORT = 8852;

    public void stopMailServer() {
        SimpleSmtpServer server = ServiceLocator.getInstance().resolve(SimpleSmtpServer.class);
        server.stop();
    }

    @AfterStories
    public void reinitialize() {
        stopMailServer();
        ServiceLocator.reset();
        apply();
    }

    @Override
    protected void registerServices() {
        ServiceLocator.getInstance().register(RoomRequestFactory.class, new FakeRoomRequestFactory());
        ServiceLocator.getInstance().register(FindRoomStrategy.class, new FindFirstRoomStrategy());
        ServiceLocator.getInstance().register(Mailbox.class, new Mailbox(new DefaultStrategy()));
        ServiceLocator.getInstance().register(RoomRepository.class, new InMemoryRoomRepository());
        ServiceLocator.getInstance().register(RoomRequestRepository.class, new RoomRequestRepositoryInMemory());
        ServiceLocator.getInstance().register(SimpleSmtpServer.class, SimpleSmtpServer.start(SMTP_PORT));
        ServiceLocator.getInstance().register(NotificationSenderStrategy.class, new JavaxMailSenderStrategy(new JavaxMailSender(getMailProperties(SMTP_PORT))));
        ServiceLocator.getInstance().register(NotificationFactory.class, new NotificationFactory());
        ServiceLocator.getInstance().register(ReservationApplicationService.class, new ReservationApplicationService());
    }

    @Override
    protected void applyFillers() {

    }

    private Properties getMailProperties(int port) {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", "localhost");
        mailProps.setProperty("mail.smtp.port", "" + port);
        mailProps.setProperty("mail.smtp.sendpartial", "true");
        return mailProps;
    }
}
