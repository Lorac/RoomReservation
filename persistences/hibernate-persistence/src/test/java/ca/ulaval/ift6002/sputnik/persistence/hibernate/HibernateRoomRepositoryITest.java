package ca.ulaval.ift6002.sputnik.persistence.hibernate;

import ca.ulaval.ift6002.sputnik.applicationservice.shared.persistence.EntityManagerProvider;
import ca.ulaval.ift6002.sputnik.domain.core.hibernate.room.HibernateRoom;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class HibernateRoomRepositoryITest {

    private static final RoomNumber ROOM_NUMBER = new RoomNumber("PLT-1234");
    private static final int ROOM_CAPACITY = 4;
    private HibernateRoomRepository repository;
    private EntityManager entityManager;

    @Before
    public void createEntityManager() {
        entityManager = EntityManagerFactoryProviderForTest.getFactory().createEntityManager();
        EntityManagerProvider.setEntityManager(entityManager);
        repository = new HibernateRoomRepository();
    }

    @After
    public void clearEntityManager() {
        EntityManagerProvider.clearEntityManager();
    }

    @Test
    public void persistsTheRoomWithTheRoomNumber() {
        HibernateRoom room = new HibernateRoom(ROOM_NUMBER, ROOM_CAPACITY);
        repository.persist(room);

        Room roomFound = repository.findRoomByNumber(ROOM_NUMBER);

        assertTrue("Should contain the room", roomFound.hasSameNumber(room));
    }

    @Test
    public void whenFindAllWithNoRoomShouldReturnNothing() {
        List<HibernateRoom> all = repository.findAll();

        assertTrue("Should not contain a room", all.isEmpty());
    }

    public static class EntityManagerFactoryProviderForTest {

        public static EntityManagerFactory getFactory() {
            return Persistence.createEntityManagerFactory("persistence");
        }
    }
}
