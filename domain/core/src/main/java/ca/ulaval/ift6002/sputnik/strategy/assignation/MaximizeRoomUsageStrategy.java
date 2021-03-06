package ca.ulaval.ift6002.sputnik.strategy.assignation;


import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;

import java.util.Comparator;
import java.util.List;


public class MaximizeRoomUsageStrategy implements FindRoomStrategy {

    @Override
    public Room findRoom(RoomRequest roomRequest, List<Room> availableRooms) {
        availableRooms.sort(Comparator.comparing(Room::getCapacity));
        return availableRooms.stream().filter(r -> r.hasCapacity(roomRequest.getNumberOfSeatsNeeded())).findFirst().get();
    }
}
