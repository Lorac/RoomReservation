package ca.ulaval.ift6002.sputnik.domain.core.room;


public class StandardRoom extends Room {
    private RoomNumber number;

    private boolean reserve;

    private int capacity;

    public StandardRoom(RoomNumber number, int capacity) {
        this.number = number;
        this.capacity = capacity;
        reserve = false;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public RoomNumber getRoomNumber() {
        return number;
    }

    @Override
    public void reserve() {
        reserve = true;
    }

    @Override
    public void release() {
        reserve = false;
    }

    @Override
    public boolean isReserved() {
        return reserve;
    }
}
