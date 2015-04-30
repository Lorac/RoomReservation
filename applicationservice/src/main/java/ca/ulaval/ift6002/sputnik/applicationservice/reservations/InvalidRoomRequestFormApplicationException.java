package ca.ulaval.ift6002.sputnik.applicationservice.reservations;

public class InvalidRoomRequestFormApplicationException extends RuntimeException {
    public InvalidRoomRequestFormApplicationException(String message) {
        super(message);
    }
}
