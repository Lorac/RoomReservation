package ca.ulaval.ift6002.sputnik.strategy.sorting;

import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;

import java.util.Comparator;
import java.util.List;

public class PriorityStrategy implements SortingStrategy {

    @Override
    public void sortRoomRequest(List<RoomRequest> requests) {
        requests.sort(Comparator.comparing(RoomRequest::getPriority));
    }
}
