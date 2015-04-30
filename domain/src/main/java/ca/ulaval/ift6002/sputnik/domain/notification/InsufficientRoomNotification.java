package ca.ulaval.ift6002.sputnik.domain.notification;

public class InsufficientRoomNotification implements Notification {

    private String insufficientMessage = "Hi, your room request cannot be processed, because no room is available.";

    @Override
    public String getSubject() {
        return "Room request refused";
    }

    @Override
    public String getMessage() {
        return insufficientMessage;
    }
}
