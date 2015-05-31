package ca.ulaval.ift6002.sputnik.domain.core.notification;

import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;

public class NotificationFactory {

    public NotificationFactory() {
    }

    public Notification createInsufficientRoomNotification() {
        return new InsufficientRoomNotification();
    }

    public Notification createSuccessNotification(RoomNumber roomNumber) {
        return new SuccessNotification(roomNumber);
    }

    public Notification createCanceledNotification() {
        return new CanceledNotification();
    }

    public Notification createCanceledNotification(RoomNumber roomNumber) {
        return new CanceledNotification(roomNumber);
    }
}
