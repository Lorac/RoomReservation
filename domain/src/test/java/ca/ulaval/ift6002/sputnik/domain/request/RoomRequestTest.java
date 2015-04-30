package ca.ulaval.ift6002.sputnik.domain.request;

import ca.ulaval.ift6002.sputnik.domain.notification.*;
import ca.ulaval.ift6002.sputnik.domain.room.Room;
import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoomRequestTest {

    private static final RequestIdentifier VALID_ROOM_REQUEST_IDENTIFIER = RequestIdentifier.create();
    private static final RequestIdentifier INVALID_ROOM_REQUEST_IDENTIFIER = RequestIdentifier.create();

    private static final RoomNumber ROOM_NUMBER = new RoomNumber("1234");

    private static final Priority PRIORITY = Priority.HIGH;
    private static final String EMAIL = "SOMEONE@SPUTNIK.COM";
    private static final User USER = new User(EMAIL);
    private static final List<User> ATTENDEES = new LinkedList<>();

    private static final int NUMBER_OF_ATTENDEES = 3;
    private static final int NUMBER_OF_ORGANIZER = 1;

    @Mock
    private NotificationSenderStrategy notificationSender;

    @Mock
    private NotificationFactory notificationFactory;

    @Test
    public void hasIdentifierIfComparedToTheInitialIdentifiedUUID() {
        RoomRequest roomRequest = new RoomRequest(VALID_ROOM_REQUEST_IDENTIFIER, PRIORITY, USER, ATTENDEES);

        boolean sameIdentifier = roomRequest.hasIdentifier(VALID_ROOM_REQUEST_IDENTIFIER);

        assertTrue(sameIdentifier);
    }

    @Test
    public void hasIdentifierIfComparedToADifferentIdentifier() {
        RoomRequest roomRequest = new RoomRequest(VALID_ROOM_REQUEST_IDENTIFIER, PRIORITY, USER, ATTENDEES);

        boolean sameIdentifier = roomRequest.hasIdentifier(INVALID_ROOM_REQUEST_IDENTIFIER);

        assertFalse(sameIdentifier);
    }

    @Test
    public void whenRoomRequestIsRefusedOrganizerIsNotified() {
        whenRoomRequestIsRefused();
        verify(notificationSender, times(1)).send(any(InsufficientRoomNotification.class));
    }

    @Test
    public void whenRoomRequestIsRefusedTheStatusOfTheRoomRequestIsRefused() {
        RoomRequest roomRequest = whenRoomRequestIsRefused();

        assertTrue(roomRequest.hasStatus(Status.REFUSED));
    }

    @Test
    public void whenCancellingARoomRequestItShouldSendEmailToOrganizerAndAttendees() {
        RoomRequest roomRequest = new RoomRequest(VALID_ROOM_REQUEST_IDENTIFIER, PRIORITY, USER, ATTENDEES);

        roomRequest.cancel(notificationSender, notificationFactory);

        verify(notificationSender, times(2)).send(any(CanceledNotification.class));
    }

    @Test
    public void whenCancellingARoomRequestItShouldAddAllTheAttendeesPlusOrganizerToTheRecipientList() {
        giveAmountOfAttendees();
        RoomRequest roomRequest = new RoomRequest(VALID_ROOM_REQUEST_IDENTIFIER, PRIORITY, USER, ATTENDEES);

        roomRequest.cancel(notificationSender, notificationFactory);

        verify(notificationSender, times(NUMBER_OF_ATTENDEES + NUMBER_OF_ORGANIZER)).addRecipient(any(User.class));
    }

    @Test
    public void whenCancellingARoomRequestAlreadyProcessedThenNotificationIncludesRoomNumber() {
        RoomRequest roomRequest = givenARoomRequestWithRoomAssigned();

        roomRequest.cancel(notificationSender, notificationFactory);

        verify(notificationFactory).createCanceledNotification(ROOM_NUMBER);
    }

    @Test
    public void whenAssigningARoomToRoomRequestItShouldHaveARoomAssigned() {
        RoomRequest roomRequest = givenARoomRequestWithRoomAssigned();

        assertTrue(roomRequest.hasRoomAssign());
    }

    @Test
    public void whenARoomRequestIsAcceptedThenTheOrganizerIsNotified() {
        RoomRequest roomRequest = new RoomRequest(VALID_ROOM_REQUEST_IDENTIFIER, PRIORITY, USER, ATTENDEES);

        roomRequest.confirm(notificationSender, notificationFactory);

        verify(notificationSender).send(any(SuccessNotification.class));
    }

    @Test
    public void whenARoomRequestIsAcceptedThenNotificationIncludesRoomNumber() {
        RoomRequest roomRequest = givenARoomRequestWithRoomAssigned();

        roomRequest.confirm(notificationSender, notificationFactory);

        verify(notificationFactory).createSuccessNotification(ROOM_NUMBER);
    }

    private RoomRequest givenARoomRequestWithRoomAssigned() {
        Room mockedRoom = givenARoom();
        RoomRequest roomRequest = new RoomRequest(VALID_ROOM_REQUEST_IDENTIFIER, PRIORITY, USER, ATTENDEES);
        roomRequest.assignRoom(mockedRoom);
        return roomRequest;
    }

    private Room givenARoom() {
        Room mockedRoom = mock(Room.class);
        when(mockedRoom.getRoomNumber()).thenReturn(ROOM_NUMBER);
        return mockedRoom;
    }

    private void giveAmountOfAttendees() {
        for (int i = 0; i < NUMBER_OF_ATTENDEES; i++) {
            ATTENDEES.add(new User(EMAIL));
        }
    }

    private RoomRequest whenRoomRequestIsRefused() {
        RoomRequest roomRequest = new RoomRequest(VALID_ROOM_REQUEST_IDENTIFIER, PRIORITY, USER, ATTENDEES);
        roomRequest.refuse(notificationSender, new NotificationFactory());
        return roomRequest;
    }

}
