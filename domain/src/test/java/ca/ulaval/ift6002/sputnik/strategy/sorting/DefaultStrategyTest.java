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
public class DefaultStrategyTest {

    private List<RoomRequest> roomRequests = new LinkedList<>();

    @Mock
    private RoomRequest firstRoomRequest;

    @Mock
    private RoomRequest secondRoomRequest;

    @Mock
    private RoomRequest thirdRoomRequest;

    @InjectMocks
    private DefaultStrategy defaultStrategy;

    @Before
    public void setUp() {
        when(firstRoomRequest.getPriority()).thenReturn(Priority.NORMAL);
        when(secondRoomRequest.getPriority()).thenReturn(Priority.NORMAL);
        when(thirdRoomRequest.getPriority()).thenReturn(Priority.NORMAL);
    }

    @Test
    public void whenSortingRoomByDefaultTheyAreSortedByFifo() {
        List<RoomRequest> expectedList = new ArrayList<>(3);
        expectedList.add(firstRoomRequest);
        expectedList.add(secondRoomRequest);
        expectedList.add(thirdRoomRequest);

        roomRequests.add(firstRoomRequest);
        roomRequests.add(secondRoomRequest);
        roomRequests.add(thirdRoomRequest);

        defaultStrategy.sortRoomRequest(roomRequests);

        assertEquals(expectedList, roomRequests);
    }
}
