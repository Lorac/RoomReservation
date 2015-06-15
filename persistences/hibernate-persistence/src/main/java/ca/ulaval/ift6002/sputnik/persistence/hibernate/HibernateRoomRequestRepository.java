package ca.ulaval.ift6002.sputnik.persistence.hibernate;

import ca.ulaval.ift6002.sputnik.domain.core.hibernate.room.HibernateRoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequestRepository;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

public class HibernateRoomRequestRepository extends HibernateRepository implements RoomRequestRepository<HibernateRoomRequest> {

    public HibernateRoomRequestRepository() {
        super();
    }

    public HibernateRoomRequestRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public HibernateRoomRequest findReservationByIdentifier(RequestIdentifier identifier) {
        HibernateRoomRequest room = entityManager.find(HibernateRoomRequest.class, identifier);
        if (room == null) {
            throw new RoomNotFoundException(String.format("Couldn't find the room with number '%s'.", identifier));
        }
        return room;
    }

    @Override
    public List<HibernateRoomRequest> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HibernateRoomRequest> cq = builder.createQuery(HibernateRoomRequest.class);
        Root<HibernateRoomRequest> root = cq.from(HibernateRoomRequest.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public void update(HibernateRoomRequest roomRequest) {
        entityManager.getTransaction().begin();
        entityManager.merge(roomRequest);
        entityManager.getTransaction().commit();
    }

    @Override
    public void persist(HibernateRoomRequest roomRequest) {
        entityManager.getTransaction().begin();
        entityManager.persist(roomRequest);
        entityManager.getTransaction().commit();
    }
}
