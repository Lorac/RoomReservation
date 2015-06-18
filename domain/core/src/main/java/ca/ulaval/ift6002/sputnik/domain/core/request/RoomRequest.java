package ca.ulaval.ift6002.sputnik.domain.core.request;


import ca.ulaval.ift6002.sputnik.domain.core.notification.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;


public abstract class RoomRequest implements Serializable {


    private Instant timeOfAssignation;

    protected RoomRequest() {
    }

    public abstract Status getStatus();

    public abstract void setStatus(Status status);

    protected abstract List<User> getAttendees();

    public abstract User getOrganizer();

    public abstract RoomNumber getRoomNumber();

    protected abstract void setRoomNumber(RoomNumber roomNumber);

    public abstract int getNumberOfSeatsNeeded();

    public abstract Priority getPriority();

    public abstract RequestIdentifier getIdentifier();

    public boolean hasIdentifier(RequestIdentifier identifier) {
        return getIdentifier().isSame(identifier);
    }

    public boolean hasStatus(Status status) {
        return getStatus().equals(status);
    }

    public void refuse(NotificationSenderStrategy notificationSender, NotificationFactory notificationFactory) {
        setStatus(Status.REFUSED);

        Notification insufficientRoomNotification = notificationFactory.createInsufficientRoomNotification();

        notifyOrganizer(notificationSender, insufficientRoomNotification);
    }

    public void cancel(NotificationSenderStrategy notificationSender, NotificationFactory notificationFactory) {
        setStatus(Status.CANCELED);

        Notification notification = createAppropriateCancellationNotification(notificationFactory);

        notifyOrganizer(notificationSender, notification);
        notifyAttendees(notificationSender, notification);

        unAssignedRoom();
    }

    public void confirm(NotificationSenderStrategy notificationSender, NotificationFactory notificationFactory) {
        setStatus(Status.ACCEPTED);

        Notification successNotification = notificationFactory.createSuccessNotification(getRoomNumber());

        notifyOrganizer(notificationSender, successNotification);
    }

    public void assignRoom(Room room) {
        timeOfAssignation = Instant.now();
        setRoomNumber(room.getRoomNumber());
    }

    public boolean hasRoomAssign() {
        return getRoomNumber() != null;
    }

    public Instant getTimeOfAssignation() {
        return timeOfAssignation;
    }

    private void unAssignedRoom() {
        setRoomNumber(null);
    }

    public boolean hasSameOrganizer(String email) {
        return getOrganizer().hasEmail(email);
    }

    private void notifyOrganizer(NotificationSenderStrategy notificationSender, Notification notification) {
        notificationSender.addRecipient(getOrganizer());
        notificationSender.send(notification);
    }

    private void notifyAttendees(NotificationSenderStrategy notificationSender, Notification notification) {
        getAttendees().forEach(notificationSender::addRecipient);
        notificationSender.send(notification);
    }

    private Notification createAppropriateCancellationNotification(NotificationFactory notificationFactory) {
        Notification notification;
        if (hasRoomAssign()) {
            notification = notificationFactory.createCanceledNotification(getRoomNumber());
        } else {
            notification = notificationFactory.createCanceledNotification();
        }
        return notification;
    }
}
