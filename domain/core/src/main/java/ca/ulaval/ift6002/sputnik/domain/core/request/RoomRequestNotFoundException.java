package ca.ulaval.ift6002.sputnik.domain.core.request;

public class RoomRequestNotFoundException extends RuntimeException {
    public RoomRequestNotFoundException() {
        super("A RoomRequest couldn't be found");
    }

    public RoomRequestNotFoundException(String message) {
        super(message);
    }
}
