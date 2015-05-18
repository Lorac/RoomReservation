package ca.ulaval.ift6002.sputnik.persistence.memory;

import ca.ulaval.ift6002.sputnik.domain.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequestRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
