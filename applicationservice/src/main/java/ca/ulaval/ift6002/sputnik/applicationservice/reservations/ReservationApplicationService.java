package ca.ulaval.ift6002.sputnik.applicationservice.reservations;

import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.domain.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.notification.NotificationFactory;
import ca.ulaval.ift6002.sputnik.domain.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequestRepository;
import ca.ulaval.ift6002.sputnik.domain.room.Room;
import ca.ulaval.ift6002.sputnik.domain.room.RoomRepository;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ReservationApplicationService {

    private final Mailbox mailbox;
    private final RoomRepository roomRepository;
    private final FindRoomStrategy assignationStrategy;
    private final RoomRequestRepository roomRequestRepository;
    private final NotificationSenderStrategy notificationSender;
    private final NotificationFactory notificationFactory;

    public ReservationApplicationService() {
        roomRequestRepository = ServiceLocator.getInstance().resolve(RoomRequestRepository.class);
        roomRepository = ServiceLocator.getInstance().resolve(RoomRepository.class);
        mailbox = ServiceLocator.getInstance().resolve(Mailbox.class);
        assignationStrategy = ServiceLocator.getInstance().resolve(FindRoomStrategy.class);
        notificationSender = ServiceLocator.getInstance().resolve(NotificationSenderStrategy.class);
        notificationFactory = ServiceLocator.getInstance().resolve(NotificationFactory.class);
    }

    public ReservationApplicationService(RoomRepository roomRepository, RoomRequestRepository roomRequestRepository, Mailbox mailbox, FindRoomStrategy assignationStrategy, NotificationSenderStrategy notificationSender, NotificationFactory notificationFactory) {
        this.roomRepository = roomRepository;
        this.mailbox = mailbox;
        this.assignationStrategy = assignationStrategy;
        this.roomRequestRepository = roomRequestRepository;
        this.notificationSender = notificationSender;
        this.notificationFactory = notificationFactory;
    }

    public void addRequest(RoomRequest request) {
        mailbox.add(request);
    }

    public void cancelRequest(RequestIdentifier identifier) {
        RoomRequest request = roomRequestRepository.findReservationByIdentifier(identifier);
        if (request == null) {
            request = mailbox.getRoomRequestByIdentifier(identifier);
        }
        cancelRoomRequest(request);
    }

    public void assignRoomRequest() {
        List<Room> rooms = roomRepository.findAll();
        List<Room> availableRooms = rooms.stream().filter(r -> !r.isReserved()).collect(Collectors.toList());
        List<RoomRequest> roomRequests = mailbox.collect();

        assignToCreateReservation(availableRooms, roomRequests);
    }

    private void cancelRoomRequest(RoomRequest request) {
        if (request.hasRoomAssign()) {
            Room room = roomRepository.findRoomByNumber(request.getRoomNumber());
            releaseRoom(room);
        }
        request.cancel(notificationSender, notificationFactory);
        roomRequestRepository.persist(request);
    }

    private void assignToCreateReservation(List<Room> availableRooms, List<RoomRequest> roomRequests) {
        for (RoomRequest roomRequest : roomRequests) {
            try {
                Room accommodatingRoom = assignationStrategy.findRoom(roomRequest, availableRooms);
                availableRooms.remove(accommodatingRoom);
                assignRoomToRequest(roomRequest, accommodatingRoom);
            } catch (NoSuchElementException ignore) {
                roomRequest.refuse(notificationSender, notificationFactory);
            }
        }
    }

    private void assignRoomToRequest(RoomRequest roomRequest, Room accommodatingRoom) {
        reserveRoom(accommodatingRoom);
        roomRequest.assignRoom(accommodatingRoom);
        persistReservation(roomRequest, accommodatingRoom);
    }

    private void persistReservation(RoomRequest roomRequest, Room accommodatingRoom) {
        roomRequest.confirm(notificationSender, notificationFactory);
        roomRequestRepository.persist(roomRequest);
    }

    private void reserveRoom(Room room) {
        room.reserve();
        roomRepository.persist(room);
    }

    public RoomRequest getRequest(String email, RequestIdentifier roomRequestIdentifier) {
        RoomRequest roomRequest = roomRequestRepository.findReservationByIdentifier(roomRequestIdentifier);
        if(roomRequest.hasSameOrganizer(email)) {
            return roomRequest;
        } else {
            throw new NotSameEmailException();
        }


    }

    private void releaseRoom(Room room) {
        room.release();
        roomRepository.persist(room);
    }
}