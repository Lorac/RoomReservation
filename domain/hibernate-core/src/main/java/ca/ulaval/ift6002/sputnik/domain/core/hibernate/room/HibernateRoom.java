package ca.ulaval.ift6002.sputnik.domain.core.hibernate.room;

import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class HibernateRoom extends Room {

    @EmbeddedId
    private RoomNumber number;

    private boolean reserve;

    private int capacity;

    public HibernateRoom(RoomNumber number, int capacity) {
        this.number = number;
        this.capacity = capacity;
        reserve = false;
    }

    protected HibernateRoom() {
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
