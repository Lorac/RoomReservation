package ca.ulaval.ift6002.sputnik.persistence.memory;

import ca.ulaval.ift6002.sputnik.domain.core.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.TestCase.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class RoomRequestRepositoryInMemoryTest {

    private RequestIdentifier VALID_RESERVATION_IDENTIFIER = RequestIdentifier.create();
    private RequestIdentifier INVALID_RESERVATION_IDENTIFIER = RequestIdentifier.create();

    private RoomRequestRepositoryInMemory requestRepositoryInMemory;

    @Before
    public void setUp() {
        requestRepositoryInMemory = new RoomRequestRepositoryInMemory();
    }

    @Test
    public void whenInitializingReservationRepositoryItShouldBeEmpty() {
        List<RoomRequest> reservationList = requestRepositoryInMemory.findAll();

        assertTrue(reservationList.isEmpty());
    }

    @Test
    public void whenAddingAReservationShouldBeAbleToRetrieveItByIdentifier() {
        RoomRequest reservation = givenAValidReservation();

        RoomRequest foundReservation = requestRepositoryInMemory.findReservationByIdentifier(VALID_RESERVATION_IDENTIFIER);

        assertEquals(reservation, foundReservation);
    }

    @Test
    public void whenAddingAReservationTryingToFindItWithWrongIdentifierReturnNothing() {
        givenAValidReservation();

        RoomRequest foundReservation = requestRepositoryInMemory.findReservationByIdentifier(INVALID_RESERVATION_IDENTIFIER);

        assertNull(foundReservation);
    }

    private RoomRequest givenAValidReservation() {
        RoomRequest reservation = mock(RoomRequest.class);
        willReturn(true).given(reservation).hasIdentifier(VALID_RESERVATION_IDENTIFIER);
        willReturn(VALID_RESERVATION_IDENTIFIER).given(reservation).getIdentifier();
        requestRepositoryInMemory.persist(reservation);
        return reservation;
    }
}
