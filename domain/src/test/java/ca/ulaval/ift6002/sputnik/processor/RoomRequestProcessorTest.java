package ca.ulaval.ift6002.sputnik.processor;

import ca.ulaval.ift6002.sputnik.domain.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.room.RoomRepository;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoomRequestProcessorTest {

    private static final int THRESHOLD_ROOM_REQUEST = 2;
    private static final long ONE_MINUTE = 1;
    private static final long RUN_NOW = 0;
    private static final long FIFTY_SECONDS_DELAY = 50;

    private RoomRequestProcessor roomRequestProcessor;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private Mailbox mailbox;

    @Mock
    private FindRoomStrategy assignationStrategy;

    @Mock
    private ScheduledExecutorService executorService;

    @Mock
    private Runnable runnable;

    @Before
    public void setUp() {
        roomRequestProcessor =
                new RoomRequestProcessor(mailbox, runnable, executorService, ONE_MINUTE, TimeUnit.MINUTES, THRESHOLD_ROOM_REQUEST);
    }

    @Test
    public void whenStartingTheProcessShouldBeAbleToRunAtFixedRateWithPeriod() {
        roomRequestProcessor.startAssignationAtFixedRate(RUN_NOW);
        verify(executorService, timeout(100).atLeastOnce())
                .scheduleAtFixedRate(eq(runnable), eq(RUN_NOW), eq(ONE_MINUTE), eq(TimeUnit.MINUTES));
    }

    @Test
    public void whenAddingThresholdAmountRoomRequestStartAssignationAutomatically() {
        roomRequestProcessor.startAssignationAtFixedRate(FIFTY_SECONDS_DELAY);
        givenThresholdAmountRoomRequest();

        verify(executorService, timeout(100).atLeastOnce()).execute(eq(runnable));
    }

    private void givenThresholdAmountRoomRequest() {
        when(mailbox.count()).thenReturn(THRESHOLD_ROOM_REQUEST);
        roomRequestProcessor.notifyNewRoomRequest();
    }

}
