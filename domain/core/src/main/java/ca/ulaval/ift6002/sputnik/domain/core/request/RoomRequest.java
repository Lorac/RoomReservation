package ca.ulaval.ift6002.sputnik.domain.core.request;


import ca.ulaval.ift6002.sputnik.domain.core.notification.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Entity(name = "DEMANDE")
@JsonInclude(Include.NON_NULL)
public class RoomRequest implements Serializable {

    private final static int ADD_ORGANIZER_TO_SEATS_NEEDED = 1;

    @AttributeOverride(name = "email", column = @Column(name = "ORGANIZER"))
    @Embedded
    @JsonUnwrapped
    private final User organizer;

    @AttributeOverride(name = "number", column = @Column(name = "REQUEST_IDENTIFER"))
    @EmbeddedId
    @JsonIgnore
    private final RequestIdentifier identifier;
    @JsonIgnore
    private Instant timeOfAssignation;
    @ElementCollection()
    private List<User> attendees = new ArrayList<>();
    @Embedded
    @JsonUnwrapped
    private RoomNumber assignedRoomNumber;
    @Enumerated
    private Priority priority = Priority.NORMAL;
    @Enumerated
    private Status status = Status.WAITING;

    public RoomRequest(RequestIdentifier identifier, Priority priority, User organizer, List<User> attendees) {
        this.priority = priority;
        this.organizer = organizer;
        this.attendees = new LinkedList<>(attendees);
        this.identifier = identifier;
    }

    protected RoomRequest() {
        identifier = null;
        organizer = null;
    }

    public Status getStatus() {
        return status;
    }

    public User getOrganizer() {
        return organizer;
    }

    public RoomNumber getRoomNumber() {
        return assignedRoomNumber;
    }

    public int getNumberOfSeatsNeeded() {
        return attendees.size() + ADD_ORGANIZER_TO_SEATS_NEEDED;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean hasIdentifier(RequestIdentifier identifier) {
        return this.identifier.isSame(identifier);
    }

    public RequestIdentifier getIdentifier() {
        return identifier;
    }

    public boolean hasStatus(Status status) {
        return this.status.equals(status);
    }

    public void refuse(NotificationSenderStrategy notificationSender, NotificationFactory notificationFactory) {
        status = Status.REFUSED;

        Notification insufficientRoomNotification = notificationFactory.createInsufficientRoomNotification();

        notifyOrganizer(notificationSender, insufficientRoomNotification);
    }

    public void cancel(NotificationSenderStrategy notificationSender, NotificationFactory notificationFactory) {
        status = Status.CANCELED;

        Notification notification = createAppropriateCancellationNotification(notificationFactory);

        notifyOrganizer(notificationSender, notification);
        notifyAttendees(notificationSender, notification);

        unAssignedRoom();
    }

    public void confirm(NotificationSenderStrategy notificationSender, NotificationFactory notificationFactory) {
        status = Status.ACCEPTED;

        Notification successNotification = notificationFactory.createSuccessNotification(assignedRoomNumber);

        notifyOrganizer(notificationSender, successNotification);
    }

    public void assignRoom(Room room) {
        timeOfAssignation = Instant.now();
        this.assignedRoomNumber = room.getRoomNumber();
    }

    public boolean hasRoomAssign() {
        return assignedRoomNumber != null;
    }

    public Instant getTimeOfAssignation() {
        return timeOfAssignation;
    }

    private void unAssignedRoom() {
        assignedRoomNumber = null;
    }

    public boolean hasSameOrganizer(String email) {
        return organizer.hasEmail(email);
    }

    private void notifyOrganizer(NotificationSenderStrategy notificationSender, Notification notification) {
        notificationSender.addRecipient(organizer);
        notificationSender.send(notification);
    }

    private void notifyAttendees(NotificationSenderStrategy notificationSender, Notification notification) {
        attendees.forEach(notificationSender::addRecipient);
        notificationSender.send(notification);
    }

    private Notification createAppropriateCancellationNotification(NotificationFactory notificationFactory) {
        Notification notification;
        if (hasRoomAssign()) {
            notification = notificationFactory.createCanceledNotification(assignedRoomNumber);
        } else {
            notification = notificationFactory.createCanceledNotification();
        }
        return notification;
    }
}
