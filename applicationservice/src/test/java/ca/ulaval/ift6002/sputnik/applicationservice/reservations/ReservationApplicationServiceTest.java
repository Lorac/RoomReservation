package ca.ulaval.ift6002.sputnik.applicationservice.reservations;

import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationFactory;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.*;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindFirstRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.sorting.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReservationApplicationServiceTest {

    private static final int THREE = 3;
    private static final int TWO = 2;

    private static final String EMAIL = "SOMEONE@SPUTNIK.COM";

    private static final RequestIdentifier VALID_REQUEST_IDENTIFIER = RequestIdentifier.create();
    private static final RequestIdentifier INVALID_REQUEST_IDENTIFIER = RequestIdentifier.create();

    private static final RoomNumber VALID_ROOM_NUMBER = new RoomNumber("PLT-1234");
    private static final RoomNumber ANOTHER_VALID_ROOM_NUMBER = new RoomNumber("PLT-4567");

    private List<Room> rooms = new LinkedList<>();
    private List<RoomRequest> roomRequests = new LinkedList<>();

    @Mock
    private RoomRequestRepository roomRequestRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private Mailbox mailbox;

    @Mock
    private Room roomWith15Places;

    @Mock
    private Room roomWith25Places;

    @Mock
    private Room roomWith35Places;

    @Mock
    private RoomRequest roomRequest10Places;

    @Mock
    private RoomRequest roomRequest20Places;

    @Mock
    private RoomRequest roomRequest30Places;

    @Mock
    private NotificationSenderStrategy notificationSender;

    @Mock
    private RoomRequest roomRequestWithRoomAssigned;

    @Mock
    private NotificationFactory notificationFactory;

    @InjectMocks
    private ReservationApplicationService reservationService;

    @Before
    public void setUp() {
        FindRoomStrategy assignationStrategy = new FindFirstRoomStrategy();
        reservationService = new ReservationApplicationService(roomRepository, roomRequestRepository, mailbox, assignationStrategy, notificationSender, notificationFactory);
        when(roomRepository.findAll()).thenReturn(rooms);

        setUpMailbox();
        setUpRooms();
        setUpRoomRequests();

        when(roomRequestWithRoomAssigned.getRoomNumber()).thenReturn(VALID_ROOM_NUMBER);
        when(roomRequestWithRoomAssigned.hasRoomAssign()).thenReturn(true);
    }

    @Test
    public void whenAddingARoomRequestItShouldAddItToTheMailbox() {
        reservationService.addRequest(mock(RoomRequest.class));

        verify(mailbox).add(any(RoomRequest.class));
    }

    @Test
    public void afterRunningStrategyRoomRepositoryShouldHaveTheSameAmountOfRoomReserveThanRoomRequestTreated() {
        addTwoRequestAndTwoRooms();

        reservationService.assignRoomRequest();

        verify(roomRepository, times(TWO)).persist(roomWith15Places);
    }


    @Test
    public void whenHavingMoreRequestThenAvailableRoomsShouldPersistSameAmountOfRequests() {
        addThreeRequestsAndTwoRooms();

        reservationService.assignRoomRequest();

        verify(roomRepository, times(TWO)).persist(roomWith15Places);
    }

    @Test
    public void whenHavingNoAvailableRoomThenShouldNotReserveRooms() {
        addThreeRequestsAndTwoRooms();
        when(roomWith15Places.isReserved()).thenReturn(true);

        reservationService.assignRoomRequest();

        verify(roomRepository, never()).persist(roomWith15Places);
    }

    @Test
    public void whenHavingNoAvailableRoomThenTheRoomRequestShouldBeRefused() {
        addThreeRequestsAndTwoRooms();
        when(roomWith15Places.isReserved()).thenReturn(true);

        reservationService.assignRoomRequest();

        verify(roomRequest10Places, times(3)).refuse(any(NotificationSenderStrategy.class), eq(notificationFactory));
    }

    @Test
    public void whenCancellingARequestItShouldSendAnEmail() {
        when(roomRequestRepository.findReservationByIdentifier(VALID_REQUEST_IDENTIFIER)).thenReturn(null);

        reservationService.cancelRequest(VALID_REQUEST_IDENTIFIER);

        verify(roomRequest10Places).cancel(any(NotificationSenderStrategy.class), eq(notificationFactory));
    }

    @Test
    public void canCancelARoomRequestAfterItWasProcessed() {
        when(roomRequestRepository.findReservationByIdentifier(VALID_REQUEST_IDENTIFIER)).thenReturn(roomRequestWithRoomAssigned);
        when(roomRepository.findRoomByNumber(VALID_ROOM_NUMBER)).thenReturn(roomWith15Places);

        reservationService.cancelRequest(VALID_REQUEST_IDENTIFIER);

        verify(roomRequestWithRoomAssigned).cancel(any(NotificationSenderStrategy.class), eq(notificationFactory));
        verify(roomWith15Places).release();
        verify(roomRepository).persist(roomWith15Places);
    }

    @Test(expected = NoSuchElementException.class)
    public void cannotCancelARequestWithWrongIdentifier() {
        addTwoRequestAndTwoRooms();
        when(roomRequestRepository.findReservationByIdentifier(VALID_REQUEST_IDENTIFIER)).thenReturn(null);

        reservationService.cancelRequest(INVALID_REQUEST_IDENTIFIER);
    }

    @Test
    public void whenAssigningByPriorityThenHighestPriorityShouldBeAssignedFirst() {
        addThreeRequestsAndThreeRoomsForAssigningByPriority();
        SortingStrategy prioritySortingStrategy = new PriorityStrategy();
        prioritySortingStrategy.sortRoomRequest(roomRequests);

        reservationService.assignRoomRequest();

        InOrder inOrder = inOrder(roomRequest20Places, roomRequest10Places, roomRequest30Places);
        inOrder.verify(roomRequest20Places).assignRoom(any(Room.class));
        inOrder.verify(roomRequest10Places).assignRoom(any(Room.class));
        inOrder.verify(roomRequest30Places).assignRoom(any(Room.class));
    }

    @Test
    public void whenAssigningByFifoThenShouldBeAssignedAccordingToFifo() {
        addThreeRequestsAndThreeRoomsForAssigningByFifo();
        SortingStrategy fifoSortingStrategy = new DefaultStrategy();
        fifoSortingStrategy.sortRoomRequest(roomRequests);

        reservationService.assignRoomRequest();

        InOrder inOrder = inOrder(roomRequest30Places, roomRequest20Places, roomRequest10Places);
        inOrder.verify(roomRequest30Places).assignRoom(any(Room.class));
        inOrder.verify(roomRequest20Places).assignRoom(any(Room.class));
        inOrder.verify(roomRequest10Places).assignRoom(any(Room.class));
    }

    private void addTwoRequestAndTwoRooms() {
        for (int i = 0; i < TWO; i++) {
            roomRequests.add(roomRequest10Places);
        }
        addTwoRooms();
    }

    private void addThreeRequestsAndTwoRooms() {
        for (int i = 0; i < THREE; i++) {
            roomRequests.add(roomRequest10Places);
        }
        addTwoRooms();
    }

    private void addThreeRequestsAndThreeRoomsForAssigningByPriority() {
        roomRequests.add(roomRequest10Places);
        roomRequests.add(roomRequest20Places);
        roomRequests.add(roomRequest30Places);

        when(roomRequest10Places.getPriority()).thenReturn(Priority.NORMAL);
        when(roomRequest20Places.getPriority()).thenReturn(Priority.HIGH);
        when(roomRequest30Places.getPriority()).thenReturn(Priority.NORMAL);

        addThreeRooms();
    }

    private void addThreeRequestsAndThreeRoomsForAssigningByFifo() {
        roomRequests.add(roomRequest30Places);
        roomRequests.add(roomRequest20Places);
        roomRequests.add(roomRequest10Places);
        addThreeRooms();
    }

    private void addTwoRooms() {
        for (int i = 0; i < TWO; i++) {
            rooms.add(roomWith15Places);
        }
    }

    private void addThreeRooms() {
        rooms.add(roomWith15Places);
        rooms.add(roomWith25Places);
        rooms.add(roomWith35Places);
    }

    private void setUpRoomRequests() {
        when(roomRequest10Places.getIdentifier()).thenReturn(VALID_REQUEST_IDENTIFIER);
        when(roomRequest20Places.getIdentifier()).thenReturn(VALID_REQUEST_IDENTIFIER);
        when(roomRequest30Places.getIdentifier()).thenReturn(VALID_REQUEST_IDENTIFIER);
    }

    private void setUpMailbox() {
        when(mailbox.collect()).thenReturn(roomRequests);
        when(mailbox.getRoomRequestByIdentifier(VALID_REQUEST_IDENTIFIER)).thenReturn(roomRequest10Places);
        when(mailbox.getRoomRequestByIdentifier(INVALID_REQUEST_IDENTIFIER)).thenThrow(NoSuchElementException.class);
    }

    private void setUpRooms() {
        when(roomWith15Places.hasCapacity(anyInt())).thenReturn(true);
        when(roomWith15Places.getRoomNumber()).thenReturn(VALID_ROOM_NUMBER);
        when(roomWith25Places.hasCapacity(anyInt())).thenReturn(true);
        when(roomWith25Places.getRoomNumber()).thenReturn(ANOTHER_VALID_ROOM_NUMBER);
        when(roomWith35Places.hasCapacity(anyInt())).thenReturn(true);
        when(roomWith35Places.getRoomNumber()).thenReturn(ANOTHER_VALID_ROOM_NUMBER);
    }
}
