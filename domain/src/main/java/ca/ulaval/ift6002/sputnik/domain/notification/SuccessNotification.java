package ca.ulaval.ift6002.sputnik.domain.notification;

import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;

public class SuccessNotification implements Notification {

    private String successMessage = "Hi, your room request has been successfully processed and your room number is %s.";

    public SuccessNotification(RoomNumber roomNumber) {
        successMessage = String.format(successMessage, roomNumber.describe());
    }

    @Override
    public String getSubject() {
        return "Reservation confirmed.";
    }

    @Override
    public String getMessage() {
        return successMessage;
    }
}
