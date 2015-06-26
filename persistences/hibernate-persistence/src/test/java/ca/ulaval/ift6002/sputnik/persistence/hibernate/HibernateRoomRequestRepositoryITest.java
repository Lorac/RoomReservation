package ca.ulaval.ift6002.sputnik.persistence.hibernate;

import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerProvider;
import ca.ulaval.ift6002.sputnik.domain.core.hibernate.room.HibernateRoom;
import ca.ulaval.ift6002.sputnik.domain.core.hibernate.roomrequest.HibernateRoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import org.junit.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HibernateRoomRequestRepositoryITest {

    private static final RequestIdentifier REQUEST_IDENTIFIER = RequestIdentifier.create();
    private HibernateRoomRequestRepository repository;
    private EntityManager entityManager;

    @Before
    public void createEntityManager() {
        entityManager = EntityManagerFactoryProviderForTest.getFactory().createEntityManager();
        EntityManagerProvider.setEntityManager(entityManager);
        repository = new HibernateRoomRequestRepository();
    }

    @After
    public void clearEntityManager() {
        EntityManagerProvider.clearEntityManager();
    }

    @Test
    public void persistsTheRoomWithTheRoomNumber() {
        repository = new HibernateRoomRequestRepository();
        HibernateRoomRequest roomRequest = new HibernateRoomRequest(REQUEST_IDENTIFIER, Priority.NORMAL, new User("ASD@ASD.COM"), new ArrayList<>());
        repository.persist(roomRequest);

        RoomRequest request = repository.findReservationByIdentifier(REQUEST_IDENTIFIER);

        assertTrue("Should contain the room request", request.hasIdentifier(REQUEST_IDENTIFIER));
    }

    @Test
    public void whenFindAllWithNoRoomRequestShouldReturnNothing() {
        List<HibernateRoomRequest> all = repository.findAll();

        assertTrue(all.isEmpty());
    }

    @Test
    public void whenPersistingWithARoomAssignedShouldReturnWithTheRoomAssigned() {
        HibernateRoomRequest roomRequest = new HibernateRoomRequest(REQUEST_IDENTIFIER, Priority.NORMAL, new User("ASD@ASDW.COM"), new ArrayList<>());
        repository.persist(roomRequest);

        Room mockedRoom = new HibernateRoom(new RoomNumber("123"), 10);
        roomRequest.assignRoom(mockedRoom);

        repository.update(roomRequest);

        assertEquals(mockedRoom.getRoomNumber(), repository.findReservationByIdentifier(REQUEST_IDENTIFIER).getRoomNumber());
    }

    public static class EntityManagerFactoryProviderForTest {

        public static EntityManagerFactory getFactory() {
            return Persistence.createEntityManagerFactory("persistence");
        }
    }
}
