package ca.ulaval.ift6002.sputnik.domain.room;

import java.util.List;

public interface RoomRepository {

    Room findRoomByNumber(RoomNumber roomNumber);

    List<Room> findAll();

    void persist(Room room);
}

