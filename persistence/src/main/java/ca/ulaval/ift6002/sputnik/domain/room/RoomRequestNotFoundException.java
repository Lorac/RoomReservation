package ca.ulaval.ift6002.sputnik.domain.room;

public class RoomRequestNotFoundException extends RuntimeException {
    public RoomRequestNotFoundException(String message) {
        super(message);
    }
}
