package ca.ulaval.ift6002.sputnik.domain.notification;

import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;

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
