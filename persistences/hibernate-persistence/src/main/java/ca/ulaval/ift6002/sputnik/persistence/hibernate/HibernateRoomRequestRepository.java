package ca.ulaval.ift6002.sputnik.persistence.hibernate;

import ca.ulaval.ift6002.sputnik.domain.core.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequestRepository;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class HibernateRoomRequestRepository extends HibernateRepository implements RoomRequestRepository {

    public HibernateRoomRequestRepository() {
        super();
    }

    public HibernateRoomRequestRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public RoomRequest findReservationByIdentifier(RequestIdentifier identifier) {
        RoomRequest room = entityManager.find(RoomRequest.class, identifier);
        if (room == null) {
            throw new RoomNotFoundException(String.format("Couldn't find the room with number '%s'.", identifier));
        }
        return room;
    }

    @Override
    public List<RoomRequest> findAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoomRequest> cq = builder.createQuery(RoomRequest.class);
        Root<RoomRequest> root = cq.from(RoomRequest.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public void update(RoomRequest roomRequest) {
        entityManager.getTransaction().begin();
        entityManager.merge(roomRequest);
        entityManager.getTransaction().commit();
    }

    @Override
    public void persist(RoomRequest roomRequest) {
        entityManager.getTransaction().begin();
        entityManager.persist(roomRequest);
        entityManager.getTransaction().commit();
    }
}
