package ca.ulaval.ift6002.sputnik.persistence.hibernate;

public class RoomRequestNotFoundException extends RuntimeException {

    public RoomRequestNotFoundException(String message) {
        super(message);
    }

    public RoomRequestNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
