package ca.ulaval.ift6002.sputnik.strategy.sorting;

import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;

import java.util.List;

public class DefaultStrategy implements SortingStrategy {
    @Override
    public void sortRoomRequest(List<RoomRequest> requests) {
        // FirstInFirstOut : the RoomRequest list is already a Queue.
    }
}
