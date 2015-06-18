package ca.ulaval.ift6002.sputnik.uat.fakes;

import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;

import java.util.ArrayList;

public class FakeRoomRequestFactory implements RoomRequestFactory {

    public RequestIdentifier lastCreatedIdentifier;

    @Override
    public RoomRequest create(String organizerEmail, int numberOfPeople, int priority) {
        RequestIdentifier identifier = RequestIdentifier.create();
        lastCreatedIdentifier = identifier;
        return new StandardRoomRequest(identifier, Priority.fromInteger(priority), new User(organizerEmail), new ArrayList<>(numberOfPeople));
    }
}
