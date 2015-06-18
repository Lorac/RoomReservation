package ca.ulaval.ift6002.sputnik.domain.core.room;

public abstract class Room {

    public abstract int getCapacity();

    public abstract RoomNumber getRoomNumber();

    public abstract void reserve();

    public abstract void release();

    public abstract boolean isReserved();

    public boolean hasNumber(RoomNumber roomNumber) {
        return compareRoomNumber(roomNumber);
    }

    public boolean hasSameNumber(Room room) {
        return compareRoomNumber(room.getRoomNumber());
    }

    public boolean hasCapacity(int requestedCapacity) {
        return requestedCapacity <= getCapacity();
    }

    private boolean compareRoomNumber(RoomNumber roomNumber) {
        return getRoomNumber().isSame(roomNumber);
    }
}
