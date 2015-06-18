package ca.ulaval.ift6002.sputnik.domain.core.request;

public interface RoomRequestFactory {

    RoomRequest create(String organizerEmail, int numberOfPeople, int priority);

}
