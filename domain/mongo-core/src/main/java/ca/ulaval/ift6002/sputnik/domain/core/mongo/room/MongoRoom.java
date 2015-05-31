package ca.ulaval.ift6002.sputnik.domain.core.mongo.room;


import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("rooms")
public class MongoRoom extends Room {

    @Id
    private RoomNumber roomNumber;

    private boolean reserve;

    private int capacity;

    public MongoRoom(RoomNumber number, int capacity) {
        this.roomNumber = number;
        this.capacity = capacity;
        reserve = false;
    }

    protected MongoRoom() {
        // Default for Morphia
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public RoomNumber getRoomNumber() {
        return roomNumber;
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
