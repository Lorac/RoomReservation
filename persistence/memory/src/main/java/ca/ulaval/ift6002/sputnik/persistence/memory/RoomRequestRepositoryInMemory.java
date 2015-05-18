package ca.ulaval.ift6002.sputnik.persistence.memory;

import ca.ulaval.ift6002.sputnik.domain.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequestRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomRequestRepositoryInMemory implements RoomRequestRepository {

    private Map<RequestIdentifier, RoomRequest> requests;

    public RoomRequestRepositoryInMemory() {
        requests = new HashMap<>();
    }

    @Override
    public RoomRequest findReservationByIdentifier(RequestIdentifier reservationIdentifier) {
        return requests.get(reservationIdentifier);
    }

    @Override
    public List<RoomRequest> findAll() {
        return new ArrayList<>(requests.values());
    }

    @Override
    public void persist(RoomRequest reservation) {
        requests.put(reservation.getIdentifier(), reservation);
    }
}
