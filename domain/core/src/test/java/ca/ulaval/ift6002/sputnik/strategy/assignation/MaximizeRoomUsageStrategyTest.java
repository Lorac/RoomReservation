package ca.ulaval.ift6002.sputnik.strategy.assignation;

import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MaximizeRoomUsageStrategyTest {

    private static final int REQUESTED_EIGHT = 8;
    private static final int ROOM_WITH_10_PLACES = 10;
    private static final int ROOM_WITH_20_PLACES = 20;
    private static final int ROOM_WITH_30_PLACES = 30;
    private List<Room> rooms = new LinkedList<>();

    @Mock
    private RoomRequest roomRequest;

    @Mock
    private Room roomWith10Places;

    @Mock
    private Room roomWith20Places;

    @Mock
    private Room roomWith30Places;

    @InjectMocks
    private MaximizeRoomUsageStrategy maximizeStrategy;

    @Test
    public void whenAllRoomCanAccommodateRequestRoomThatMaximizeIsFound() {
        addThreeRooms();
        when(roomRequest.getNumberOfSeatsNeeded()).thenReturn(REQUESTED_EIGHT);

        Room room = maximizeStrategy.findRoom(roomRequest, rooms);

        assertEquals(room, roomWith10Places);
    }

    @Test(expected = NoSuchElementException.class)
    public void whenNoRoomIsAvailableNoRoomIsFound() {
        when(roomRequest.getNumberOfSeatsNeeded()).thenReturn(REQUESTED_EIGHT);

        Room room = maximizeStrategy.findRoom(roomRequest, rooms);
    }

    private void addThreeRooms() {
        rooms.add(roomWith20Places);
        rooms.add(roomWith10Places);
        rooms.add(roomWith30Places);

        when(roomWith10Places.getCapacity()).thenReturn(ROOM_WITH_10_PLACES);
        when(roomWith20Places.getCapacity()).thenReturn(ROOM_WITH_20_PLACES);
        when(roomWith30Places.getCapacity()).thenReturn(ROOM_WITH_30_PLACES);

        when(roomWith10Places.hasCapacity(anyInt())).thenReturn(true);
        when(roomWith20Places.hasCapacity(anyInt())).thenReturn(true);
        when(roomWith30Places.hasCapacity(anyInt())).thenReturn(true);
    }
}
