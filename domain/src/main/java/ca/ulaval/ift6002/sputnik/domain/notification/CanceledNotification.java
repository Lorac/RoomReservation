package ca.ulaval.ift6002.sputnik.domain.notification;

import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;

public class CanceledNotification implements Notification {

    private RoomNumber roomNumber;
    private String cancelMessageWithRoomNumber = "Hi, your reservation for the room %s has been canceled.";
    private String cancelMessage = "Hi, your reservation for a room has been canceled";

    public CanceledNotification() {
        roomNumber = null;
    }

    public CanceledNotification(RoomNumber roomNumber) {
        this.roomNumber = roomNumber;
        cancelMessageWithRoomNumber = String.format(cancelMessageWithRoomNumber, roomNumber.describe());
    }

    @Override
    public String getSubject() {
        return "Reservation canceled.";
    }

    @Override
    public String getMessage() {
        return roomNumber == null ? cancelMessage : cancelMessageWithRoomNumber;
    }
}
