package ca.ulaval.ift6002.sputnik.domain.core.request;

import java.util.List;

public interface RoomRequestRepository<T> {

    T findReservationByIdentifier(RequestIdentifier identifier);

    List<T> findAll();

    void persist(T roomRequest);
}
