package ca.ulaval.ift6002.sputnik.domain.room;

public class Room {

    private boolean reserve;
    private RoomNumber number;
    private int capacity;

    public Room(RoomNumber roomNumber, int capacity) {
        number = roomNumber;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
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
}
