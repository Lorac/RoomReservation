package ca.ulaval.ift6002.sputnik.context;


import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerFactoryProvider;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerProvider;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationFactory;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequestRepository;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomRepository;
import ca.ulaval.ift6002.sputnik.domain.core.room.StandardRoom;
import ca.ulaval.ift6002.sputnik.emailsender.JavaxMailSender;
import ca.ulaval.ift6002.sputnik.emailsender.notification.JavaxMailSenderStrategy;
import ca.ulaval.ift6002.sputnik.persistence.memory.InMemoryRoomRepository;
import ca.ulaval.ift6002.sputnik.persistence.memory.RoomRequestRepositoryInMemory;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindFirstRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.sorting.DefaultStrategy;
import com.dumbster.smtp.SimpleSmtpServer;

import java.util.Properties;

public class InMemoryDemoContext extends ContextBase {
    private static final int SMTP_PORT = 8080;

    @Override
    protected void registerServices() {
        EntityManagerProvider.setEntityManager(EntityManagerFactoryProvider.getFactory().createEntityManager());
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

        RoomRepository roomRepository = ServiceLocator.getInstance().resolve(RoomRepository.class);
        Room room1 = new StandardRoom(new RoomNumber("PLT-3904"), 50);
        Room room2 = new StandardRoom(new RoomNumber("PLT-2551"), 30);
        Room room3 = new StandardRoom(new RoomNumber("VCH-2860"), 75);

        roomRepository.persist(room1);
        roomRepository.persist(room2);
        roomRepository.persist(room3);
    }

    private Properties getMailProperties(int port) {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", "localhost");
        mailProps.setProperty("mail.smtp.port", "" + port);
        mailProps.setProperty("mail.smtp.sendpartial", "true");
        return mailProps;
    }
}
