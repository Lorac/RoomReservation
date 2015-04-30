package ca.ulaval.ift6002.sputnik.domain.room;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Room {

    @EmbeddedId
    protected RoomNumber number;

    protected boolean reserve;

    protected int capacity;

    public Room(RoomNumber roomNumber, int capacity) {
        number = roomNumber;
        this.capacity = capacity;
    }

    protected Room() {
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean hasNumber(RoomNumber roomNumber) {
        return compareRoomNumber(roomNumber);
    }

    public boolean hasSameNumber(Room room) {
        return compareRoomNumber(room.number);
    }

    public RoomNumber getRoomNumber() {
        return number;
    }

    public boolean hasCapacity(int requestedCapacity) {
        return requestedCapacity <= capacity;
    }

    public void reserve() {
        reserve = true;
    }

    public void release() {
        reserve = false;
    }

    public boolean isReserved() {
        return reserve;
    }

    private boolean compareRoomNumber(RoomNumber roomNumber) {
        return number.isSame(roomNumber);
    }
}
