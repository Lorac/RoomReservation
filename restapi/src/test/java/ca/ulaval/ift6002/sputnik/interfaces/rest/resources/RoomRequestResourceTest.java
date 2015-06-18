package ca.ulaval.ift6002.sputnik.interfaces.rest.resources;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.request.RequestIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RoomRequestResourceTest {

    private static final RequestIdentifier VALID_ROOM_REQUEST_IDENTIFIER = RequestIdentifier.create();
    private static final String EMAIL = "DIRECTOR@COMPAGNY.COM";

    @Mock
    private Mailbox mailbox;

    @Mock
    private ReservationApplicationService service;

    @InjectMocks
    private RoomRequestResource resource;


    @Test
    public void tryingToFindARoomRequestShouldForwardToTheService() {
        resource.getRequest(EMAIL, VALID_ROOM_REQUEST_IDENTIFIER.describe());

        verify(service).getRoomRequest(EMAIL, VALID_ROOM_REQUEST_IDENTIFIER);
    }

}
