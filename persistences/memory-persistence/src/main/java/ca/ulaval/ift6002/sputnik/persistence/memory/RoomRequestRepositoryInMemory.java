package ca.ulaval.ift6002.sputnik.persistence.memory;

import ca.ulaval.ift6002.sputnik.domain.core.request.*;

import java.util.*;

public class RoomRequestRepositoryInMemory implements RoomRequestRepository {

    private Map<RequestIdentifier, RoomRequest> reservations;

    public RoomRequestRepositoryInMemory() {
        reservations = new HashMap<>();
    }

    @Override
    public RoomRequest findReservationByIdentifier(RequestIdentifier reservationIdentifier) {
        return reservations.get(reservationIdentifier);
    }

    @Override
    public List<RoomRequest> findAll() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public void update(RoomRequest roomRequest) {
        persist(roomRequest);
    }

    @Override
    public void persist(RoomRequest reservation) {
        reservations.put(reservation.getIdentifier(), reservation);
    }
}
