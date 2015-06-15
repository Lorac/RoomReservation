package ca.ulaval.ift6002.sputnik.domain.core.request;

import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;

import java.util.LinkedList;
import java.util.List;

public class StandardRoomRequest extends RoomRequest {

    private List<User> attendees;
    private Status status;
    private RoomNumber roomNumber;
    private User organizer;
    private Priority priority;
    private RequestIdentifier identifier;

    public StandardRoomRequest(RequestIdentifier identifier, Priority priority, User organizer, List<User> attendees) {
        this.identifier = identifier;
        this.priority = priority;
        this.organizer = organizer;
        this.attendees = new LinkedList<>(attendees);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    protected List<User> getAttendees() {
        return attendees;
    }

    @Override
    public User getOrganizer() {
        return this.organizer;
    }

    @Override
    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    @Override
    protected void setRoomNumber(RoomNumber roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public int getNumberOfSeatsNeeded() {
        return attendees.size() + 1;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public RequestIdentifier getIdentifier() {
        return identifier;
    }
}
