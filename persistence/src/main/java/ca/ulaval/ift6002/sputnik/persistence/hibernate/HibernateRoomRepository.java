package ca.ulaval.ift6002.sputnik.persistence.hibernate;

import ca.ulaval.ift6002.sputnik.domain.room.Room;
import ca.ulaval.ift6002.sputnik.domain.room.RoomNotFoundException;
import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.room.RoomRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class HibernateRoomRepository extends HibernateRepository implements RoomRepository {

    public HibernateRoomRepository() {
        super();
    }

    public HibernateRoomRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Room findRoomByNumber(RoomNumber roomNumber) {
        Room room =  entityManager.find(Room.class, roomNumber);
        if (room == null) {
            throw new RoomNotFoundException(String.format("Couldn't find the room with number '%s'.", roomNumber));
        }
        return room;
    }

    @Override
    public List<Room> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Room> cq = builder.createQuery(Room.class);
        Root<Room> root = cq.from(Room.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public void persist(Room room) {
        entityManager.getTransaction().begin();
        entityManager.persist(room);
        entityManager.getTransaction().commit();
    }
}
