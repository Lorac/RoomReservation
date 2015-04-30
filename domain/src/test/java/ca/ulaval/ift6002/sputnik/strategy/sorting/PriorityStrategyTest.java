package ca.ulaval.ift6002.sputnik.strategy.sorting;

import ca.ulaval.ift6002.sputnik.domain.request.Priority;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriorityStrategyTest {

    private List<RoomRequest> roomRequests = new LinkedList<>();

    @Mock
    private RoomRequest highestPriorityRoomRequest;

    @Mock
    private RoomRequest normalPriorityRoomRequestEnteredFirst;

    @Mock
    private RoomRequest normalPriorityRoomRequestEnteredSecond;

    @Mock
    private RoomRequest lowestPriorityRoomRequest;

    @InjectMocks
    private PriorityStrategy priorityStrategy;

    @Before
    public void setUp() {
        when(highestPriorityRoomRequest.getPriority()).thenReturn(Priority.VERYHIGH);
        when(normalPriorityRoomRequestEnteredFirst.getPriority()).thenReturn(Priority.NORMAL);
        when(normalPriorityRoomRequestEnteredSecond.getPriority()).thenReturn(Priority.NORMAL);
        when(lowestPriorityRoomRequest.getPriority()).thenReturn(Priority.VERYLOW);
    }

    @Test
    public void whenSortingRoomByPriorityTheyAreSortedByHighestPriorityFirst() {
        List<RoomRequest> expectedList = new ArrayList<>(3);
        expectedList.add(highestPriorityRoomRequest);
        expectedList.add(normalPriorityRoomRequestEnteredFirst);
        expectedList.add(lowestPriorityRoomRequest);

        roomRequests.add(lowestPriorityRoomRequest);
        roomRequests.add(normalPriorityRoomRequestEnteredFirst);
        roomRequests.add(highestPriorityRoomRequest);

        priorityStrategy.sortRoomRequest(roomRequests);

        assertEquals(expectedList, roomRequests);
    }

    @Test
    public void whenSortingRoomByPriorityTheyAreSortedByHighestPriorityThenByFifo() {
        List<RoomRequest> expectedList = new ArrayList<>(4);
        expectedList.add(highestPriorityRoomRequest);
        expectedList.add(normalPriorityRoomRequestEnteredFirst);
        expectedList.add(normalPriorityRoomRequestEnteredSecond);
        expectedList.add(lowestPriorityRoomRequest);

        roomRequests.add(lowestPriorityRoomRequest);
        roomRequests.add(normalPriorityRoomRequestEnteredFirst);
        roomRequests.add(normalPriorityRoomRequestEnteredSecond);
        roomRequests.add(highestPriorityRoomRequest);

        priorityStrategy.sortRoomRequest(roomRequests);

        assertEquals(expectedList, roomRequests);
    }
}
