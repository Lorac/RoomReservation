package ca.ulaval.ift6002.sputnik.domain.mailbox;

import ca.ulaval.ift6002.sputnik.domain.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.strategy.sorting.SortingStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.willReturn;

import java.util.NoSuchElementException;


import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MailboxTest {

    private static final int THREE_ROOM_REQUESTS = 3;
    private static final RequestIdentifier ROOM_REQUEST_IDENTIFIER = RequestIdentifier.create();
    private static final RequestIdentifier INVALID_ROOM_REQUEST_IDENTIFIER = RequestIdentifier.create();

    private Mailbox mailbox;

    @Mock
    private RoomRequest roomRequest;

    @Mock
    private MailboxObserver mailboxMailboxObserver;

    @Mock
    private SortingStrategy sortingStrategy;

    @Before
    public void setUp() {
        when(roomRequest.hasIdentifier(ROOM_REQUEST_IDENTIFIER)).thenReturn(true);
        mailbox = new Mailbox(sortingStrategy);
    }

    @Test
    public void whenAddingARoomRequestMailboxShouldNotBeEmpty() {
        mailbox.add(mock(RoomRequest.class));
        assertFalse(mailbox.isEmpty());
    }

    @Test
    public void whenAddingThreeRoomRequestShouldHaveThreeRoomRequests() {
        givenThreeRoomRequests();

        assertEquals(THREE_ROOM_REQUESTS, mailbox.count());
    }

    @Test
    public void whenCollectingAllRoomRequestMailboxShouldBeEmpty() {
        givenThreeRoomRequests();

        mailbox.collect();

        assertTrue(mailbox.isEmpty());
    }

    @Test
    public void whenAddingRoomRequestObserversShouldBeNotified() {
        mailbox.addObserver(mailboxMailboxObserver);
        givenThreeRoomRequests();

        verify(mailboxMailboxObserver, times(3)).notifyNewRoomRequest();
    }

    @Test
    public void whenTryingToFindARoomRequestShouldFindTheRoomRequestWithThatIdentifier() {
        mailbox.add(roomRequest);
        RoomRequest roomRequestFound = mailbox.getRoomRequestByIdentifier(ROOM_REQUEST_IDENTIFIER);

        assertEquals(roomRequest, roomRequestFound);
    }

    private RoomRequest givenARoom() {
        RoomRequest roomRequest = mock(RoomRequest.class);
        willReturn(true).given(roomRequest).hasIdentifier(ROOM_REQUEST_IDENTIFIER);
        mailbox.add(roomRequest);
        return roomRequest;
    }

    @Test(expected = NoSuchElementException.class)
    public void whenTryingToFindARoomRequestWithInvalidIdentifierItShouldThrow() {
        mailbox.getRoomRequestByIdentifier(INVALID_ROOM_REQUEST_IDENTIFIER);
    }

    private void givenThreeRoomRequests() {
        RoomRequest mockRoomRequest1 = mock(RoomRequest.class);
        RoomRequest mockRoomRequest2 = mock(RoomRequest.class);
        RoomRequest mockRoomRequest3 = mock(RoomRequest.class);

        mailbox.add(mockRoomRequest1);
        mailbox.add(mockRoomRequest2);
        mailbox.add(mockRoomRequest3);
    }
}
