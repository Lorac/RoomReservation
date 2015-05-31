package ca.ulaval.ift6002.sputnik.domain.core.request;

import java.util.List;

public interface RoomRequestRepository {

    RoomRequest findReservationByIdentifier(RequestIdentifier identifier);

    List<RoomRequest> findAll();

    void update(RoomRequest roomRequest);

    void persist(RoomRequest roomRequest);
}
