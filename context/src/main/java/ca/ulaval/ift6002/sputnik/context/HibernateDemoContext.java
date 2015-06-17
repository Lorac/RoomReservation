package ca.ulaval.ift6002.sputnik.context;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerFactoryProvider;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerProvider;
import ca.ulaval.ift6002.sputnik.domain.core.hibernate.room.HibernateRoom;
import ca.ulaval.ift6002.sputnik.domain.core.hibernate.room.HibernateRoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationFactory;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.*;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import ca.ulaval.ift6002.sputnik.emailsender.JavaxMailSender;
import ca.ulaval.ift6002.sputnik.emailsender.notification.JavaxMailSenderStrategy;
import ca.ulaval.ift6002.sputnik.persistence.hibernate.HibernateRoomRepository;
import ca.ulaval.ift6002.sputnik.persistence.hibernate.HibernateRoomRequestRepository;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindFirstRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.sorting.DefaultStrategy;
import com.dumbster.smtp.SimpleSmtpServer;

import java.util.*;

public class HibernateDemoContext extends ContextBase {

    private static final int SMTP_PORT = 8080;

    @Override
    protected void registerServices() {
        EntityManagerProvider.setEntityManager(EntityManagerFactoryProvider.getFactory().createEntityManager());
        ServiceLocator.getInstance().register(FindRoomStrategy.class, new FindFirstRoomStrategy());
        ServiceLocator.getInstance().register(Mailbox.class, new Mailbox(new DefaultStrategy()));
        ServiceLocator.getInstance().register(RoomRepository.class, new HibernateRoomRepository());
        ServiceLocator.getInstance().register(RoomRequestRepository.class, new HibernateRoomRequestRepository());
        ServiceLocator.getInstance().register(SimpleSmtpServer.class, SimpleSmtpServer.start(SMTP_PORT));
        ServiceLocator.getInstance().register(NotificationSenderStrategy.class, new JavaxMailSenderStrategy(new JavaxMailSender(getMailProperties(SMTP_PORT))));
        ServiceLocator.getInstance().register(NotificationFactory.class, new NotificationFactory());
        ServiceLocator.getInstance().register(ReservationApplicationService.class, new ReservationApplicationService());
    }

    @Override
    protected void applyFillers() {

        RoomRepository roomRepository = ServiceLocator.getInstance().resolve(RoomRepository.class);
        RoomRequestRepository roomRequestRepository = ServiceLocator.getInstance().resolve(RoomRequestRepository.class);
        Room room1 = new HibernateRoom(new RoomNumber("PLT-3904"), 50);
        Room room2 = new HibernateRoom(new RoomNumber("PLT-2551"), 30);
        Room room3 = new HibernateRoom(new RoomNumber("VCH-2860"), 75);

        roomRepository.persist(room1);
        roomRepository.persist(room2);
        roomRepository.persist(room3);

        RequestIdentifier requestIdentifier1 = RequestIdentifier.create();
        RequestIdentifier requestIdentifier2 = RequestIdentifier.create();
        RequestIdentifier requestIdentifier3 = RequestIdentifier.create();

        System.out.println(requestIdentifier1.describe());
        System.out.println(requestIdentifier2.describe());
        System.out.println(requestIdentifier3.describe());

        List<User> users = new LinkedList<>();
        users.add(new User("username1"));
        users.add(new User("username2"));
        users.add(new User("username3"));
        users.add(new User("username4"));

        RoomRequest roomRequest1 = new HibernateRoomRequest(requestIdentifier1, Priority.NORMAL, new User("mroussin@hotmail.com"), users);
        RoomRequest roomRequest2 = new HibernateRoomRequest(requestIdentifier2, Priority.HIGH, new User("patate@hotmail.com"), new LinkedList<>());
        RoomRequest roomRequest3 = new HibernateRoomRequest(requestIdentifier3, Priority.LOW, new User("chef@hotmail.com"), new LinkedList<>());

        roomRequestRepository.persist(roomRequest1);
        roomRequestRepository.persist(roomRequest2);
        roomRequestRepository.persist(roomRequest3);

    }

    private Properties getMailProperties(int port) {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", "localhost");
        mailProps.setProperty("mail.smtp.port", "" + port);
        mailProps.setProperty("mail.smtp.sendpartial", "true");
        return mailProps;
    }
}
