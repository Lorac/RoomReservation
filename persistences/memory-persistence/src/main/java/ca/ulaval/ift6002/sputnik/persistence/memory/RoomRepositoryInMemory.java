package ca.ulaval.ift6002.sputnik.persistence.memory;

import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomRepositoryInMemory implements RoomRepository<Room> {

    private Map<RoomNumber, Room> rooms;

    public RoomRepositoryInMemory() {
        rooms = new HashMap<>();
    }

    @Override
    public Room findRoomByNumber(RoomNumber roomNumber) {
        return rooms.get(roomNumber);
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public void persist(Room roomToPersist) {
        rooms.put(roomToPersist.getRoomNumber(), roomToPersist);
    }
}
