package ca.ulaval.ift6002.sputnik.domain.core.mongo.request;

import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

@Entity("requests")
public class MongoRoomRequest extends RoomRequest {

    @Id
    private RequestIdentifier identifier;
    private Priority priority;
    private RoomNumber roomNumber;
    private User organizer;
    private List<User> attendees;
    private Status status;

    public MongoRoomRequest(RequestIdentifier identifier, Priority priority, User organizer, List<User> attendees) {
        this.identifier = identifier;
        this.priority = priority;
        this.organizer = organizer;
        this.attendees = attendees;
    }


    protected MongoRoomRequest() {
        // Default for Morphia
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
        return organizer;
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
