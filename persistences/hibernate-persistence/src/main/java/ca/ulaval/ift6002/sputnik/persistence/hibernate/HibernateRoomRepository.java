package ca.ulaval.ift6002.sputnik.persistence.hibernate;

import ca.ulaval.ift6002.sputnik.domain.core.hibernate.room.HibernateRoom;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNotFoundException;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class HibernateRoomRepository extends HibernateRepository implements RoomRepository<HibernateRoom> {

    public HibernateRoomRepository() {
        super();
    }

    public HibernateRoomRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public HibernateRoom findRoomByNumber(RoomNumber roomNumber) {
        HibernateRoom room = entityManager.find(HibernateRoom.class, roomNumber);
        if (room == null) {
            throw new RoomNotFoundException(String.format("Couldn't find the room with number '%s'.", roomNumber));
        }
        return room;
    }

    @Override
    public List<HibernateRoom> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HibernateRoom> cq = builder.createQuery(HibernateRoom.class);
        Root<HibernateRoom> root = cq.from(HibernateRoom.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public void persist(HibernateRoom room) {
        entityManager.getTransaction().begin();
        entityManager.persist(room);
        entityManager.getTransaction().commit();
    }
}
