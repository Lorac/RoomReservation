package ca.ulaval.ift6002.sputnik.interfaces.rest.resources;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.request.Priority;
import ca.ulaval.ift6002.sputnik.domain.core.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.request.StandardRoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoomRequestResourceTest {

    private static final RequestIdentifier VALID_ROOM_REQUEST_IDENTIFIER = RequestIdentifier.create();
    private static final String EMAIL = "DIRECTOR@COMPAGNY.COM";
    private static final RoomRequest ROOM_REQUEST = new StandardRoomRequest(VALID_ROOM_REQUEST_IDENTIFIER, Priority.NORMAL, new User(EMAIL), new LinkedList<>());

    @Mock
    private Mailbox mailbox;

    @Mock
    private ReservationApplicationService service;

    @InjectMocks
    private RoomRequestResource resource;

    @Before
    public void setUp() {
        when(service.getRoomRequest(EMAIL, VALID_ROOM_REQUEST_IDENTIFIER)).thenReturn(ROOM_REQUEST);
    }


    @Test
    public void tryingToFindARoomRequestShouldForwardToTheService() {
        resource.getRequest(EMAIL, VALID_ROOM_REQUEST_IDENTIFIER.describe());

        verify(service).getRoomRequest(EMAIL, VALID_ROOM_REQUEST_IDENTIFIER);
    }

}
