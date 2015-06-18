package ca.ulaval.ift6002.sputnik.domain.core.hibernate.room;

import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;

import javax.persistence.*;
import java.util.*;

@Entity(name = "DEMANDE")
public class HibernateRoomRequest extends RoomRequest {

    private final static int ADD_ORGANIZER_TO_SEATS_NEEDED = 1;
    @AttributeOverride(name = "email", column = @Column(name = "ORGANIZER"))
    @Embedded
    private final User organizer;
    @AttributeOverride(name = "number", column = @Column(name = "REQUEST_IDENTIFER"))
    @EmbeddedId
    private final RequestIdentifier identifier;
    @ElementCollection()
    private List<User> attendees = new ArrayList<>();
    @Embedded
    private RoomNumber assignedRoomNumber;
    private Priority priority;
    private Status status = Status.WAITING;

    public HibernateRoomRequest(RequestIdentifier identifier, Priority priority, User organizer, List<User> attendees) {
        this.identifier = identifier;
        this.priority = priority;
        this.organizer = organizer;
        this.attendees = new LinkedList<>(attendees);
    }

    public HibernateRoomRequest() {
        identifier = RequestIdentifier.create();
        organizer = null;
        attendees = new LinkedList<>();
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
        return assignedRoomNumber;
    }

    @Override
    protected void setRoomNumber(RoomNumber roomNumber) {
        this.assignedRoomNumber = roomNumber;
    }

    @Override
    public int getNumberOfSeatsNeeded() {
        return attendees.size() + ADD_ORGANIZER_TO_SEATS_NEEDED;
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
