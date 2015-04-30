package ca.ulaval.ift6002.sputnik.domain.request;


import ca.ulaval.ift6002.sputnik.domain.notification.Notification;
import ca.ulaval.ift6002.sputnik.domain.notification.NotificationFactory;
import ca.ulaval.ift6002.sputnik.domain.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.room.Room;
import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.user.User;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "DEMANDE")
public class RoomRequest implements Serializable {

    @XmlTransient
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

    @Enumerated
    private Priority priority = Priority.NORMAL;
    private Instant timeOfAssignation;
    private Status status;

    public RoomRequest(RequestIdentifier identifier, Priority priority, User organizer, List<User> attendees) {
        this.priority = priority;
        this.organizer = organizer;
        this.attendees = new LinkedList<>(attendees);
        this.identifier = identifier;
        this.status = Status.WAITING;
    }

    protected RoomRequest() {
        identifier = null;
        organizer = null;
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
        return organizer.equals(email);
    }

    private void notifyOrganizer(NotificationSenderStrategy notificationSender, Notification notification) {
        notificationSender.addRecipient(organizer);
        notificationSender.send(notification);
    }

    private void notifyAttendees(NotificationSenderStrategy notificationSender, Notification notification) {
        for (User attendee : attendees) {
            notificationSender.addRecipient(attendee);
        }
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
