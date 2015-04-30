package ca.ulaval.ift6002.sputnik.strategy.sorting;

import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;

import java.util.List;

@FunctionalInterface
public interface SortingStrategy {

    void sortRoomRequest(List<RoomRequest> requests);
}
