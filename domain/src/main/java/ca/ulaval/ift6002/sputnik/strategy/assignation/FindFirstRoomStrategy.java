package ca.ulaval.ift6002.sputnik.strategy.assignation;

import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.room.Room;

import java.util.List;


public class FindFirstRoomStrategy implements FindRoomStrategy {

    @Override
    public Room findRoom(RoomRequest roomRequest, List<Room> availableRooms) {
        return availableRooms.stream().filter(r -> r.hasCapacity(roomRequest.getNumberOfSeatsNeeded())).findFirst().get();
    }
}
