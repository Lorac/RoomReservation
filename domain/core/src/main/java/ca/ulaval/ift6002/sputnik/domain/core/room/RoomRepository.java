package ca.ulaval.ift6002.sputnik.domain.core.room;

import java.util.List;

public interface RoomRepository<T> {

    T findRoomByNumber(RoomNumber roomNumber);

    List<T> findAll();

    void persist(T t);
}

