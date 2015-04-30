package ca.ulaval.ift6002.sputnik.domain.request;

import java.util.List;

public interface RoomRequestRepository {

    RoomRequest findReservationByIdentifier(RequestIdentifier identifier);

    List<RoomRequest> findAll();

    void persist(RoomRequest roomRequest);
}
