package ca.ulaval.ift6002.sputnik.strategy.assignation;

import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;

import java.util.List;

@FunctionalInterface
public interface FindRoomStrategy {

    Room findRoom(RoomRequest roomRequest, List<Room> rooms);
}
