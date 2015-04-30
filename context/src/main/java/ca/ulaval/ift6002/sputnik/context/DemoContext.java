package ca.ulaval.ift6002.sputnik.context;


import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerFactoryProvider;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerProvider;
import ca.ulaval.ift6002.sputnik.domain.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.notification.NotificationFactory;
import ca.ulaval.ift6002.sputnik.domain.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequestRepository;
import ca.ulaval.ift6002.sputnik.domain.room.Room;
import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.room.RoomRepository;
import ca.ulaval.ift6002.sputnik.emailsender.JavaxMailSender;
import ca.ulaval.ift6002.sputnik.emailsender.notification.JavaxMailSenderStrategy;
import ca.ulaval.ift6002.sputnik.persistence.hibernate.HibernateRoomRepository;
import ca.ulaval.ift6002.sputnik.persistence.hibernate.HibernateRoomRequestRepository;
import ca.ulaval.ift6002.sputnik.processor.RoomRequestProcessor;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindFirstRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.sorting.DefaultStrategy;
import com.dumbster.smtp.SimpleSmtpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class DemoContext extends ContextBase {

    private static final int EMAIL_THRESHOLD = 50;
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
        setUpRoomRequestProcessor();

    }

    @Override
    protected void applyFillers() {
        RoomRepository roomRepository = ServiceLocator.getInstance().resolve(RoomRepository.class);
        Room room1 = new Room(new RoomNumber("PLT-3904"), 50);
        Room room2 = new Room(new RoomNumber("PLT-2551"), 30);
        Room room3 = new Room(new RoomNumber("VCH-2860"), 75);

        roomRepository.persist(room1);
        roomRepository.persist(room2);
        roomRepository.persist(room3);
    }

    private Properties getMailProperties(int port) {
        ClassLoader classLoader = getClass().getClassLoader();
        FileInputStream fin = null;
        Properties mailProps = new Properties();
        try {
            fin = new FileInputStream(new File(classLoader.getResource("config.properties").getFile()));
            mailProps.load(fin);
            mailProps.setProperty("mail.smtp.host", "localhost");
            mailProps.setProperty("mail.smtp.port", "" + port);
            mailProps.setProperty("mail.smtp.sendpartial", "true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mailProps;
    }

    private void setUpRoomRequestProcessor() {
        ReservationApplicationService reservationService = ServiceLocator.getInstance().resolve(ReservationApplicationService.class);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        Mailbox mailbox = ServiceLocator.getInstance().resolve(Mailbox.class);
        Runnable runner = reservationService::assignRoomRequest;

        RoomRequestProcessor roomRequestProcessor = new RoomRequestProcessor(mailbox, runner, executorService, 10, TimeUnit.SECONDS, EMAIL_THRESHOLD);
        ServiceLocator.getInstance().register(RoomRequestProcessor.class, roomRequestProcessor);
        roomRequestProcessor.startAssignationAtFixedRate(10);
    }
}
