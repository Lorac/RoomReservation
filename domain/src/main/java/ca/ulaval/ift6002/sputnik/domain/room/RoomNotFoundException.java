package ca.ulaval.ift6002.sputnik.domain.room;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException() {
        super();
    }

    public RoomNotFoundException(String message) {
        super(message);
    }
}
