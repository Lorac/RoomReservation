package ca.ulaval.ift6002.sputnik.applicationservice.reservations;

import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationFactory;
import ca.ulaval.ift6002.sputnik.domain.core.notification.NotificationSenderStrategy;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomRepository;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;

import java.util.ArrayList;
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

    public void cancelRequest(RequestIdentifier identifier) {
        RoomRequest request = (RoomRequest) roomRequestRepository.findReservationByIdentifier(identifier);
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

    public RoomRequest getRoomRequest(String email, RequestIdentifier roomRequestIdentifier) {
        RoomRequest roomRequest;
        try {
            roomRequest = mailbox.getRoomRequestByIdentifier(roomRequestIdentifier);
        } catch (NoSuchElementException ignore) {
            roomRequest = (RoomRequest) roomRequestRepository.findReservationByIdentifier(roomRequestIdentifier);
        }

        if (roomRequest.hasSameOrganizer(email)) {
            return roomRequest;
        }
        return roomRequest;
    }

    public RequestIdentifier addRequest(RoomRequestForm roomRequestForm) {
        validateForm(roomRequestForm);

        RequestIdentifier roomRequestIdentifier = RequestIdentifier.create();
        RoomRequest roomRequest = new StandardRoomRequest(roomRequestIdentifier, Priority.fromInteger(roomRequestForm.priority), new User(roomRequestForm.organizerEmail), new ArrayList<>());

        mailbox.add(roomRequest);

        return roomRequestIdentifier;
    }

    private void validateForm(RoomRequestForm roomRequestForm) {
        if (roomRequestForm.numberOfPeople < 0) {
            throw new InvalidRoomRequestFormApplicationException(String.format("Le nombre de personne %s est invalide", roomRequestForm.numberOfPeople));
        }
        if (roomRequestForm.organizerEmail == null || roomRequestForm.organizerEmail.equals("")) {
            throw new InvalidRoomRequestFormApplicationException(String.format("Le courriel \"%s\" n'est pas valide", roomRequestForm.organizerEmail));
        }
        if (roomRequestForm.priority < 0 || roomRequestForm.priority > 5) {
            throw new InvalidRoomRequestFormApplicationException(String.format("La priorité \"%s\" n'est pas valide", roomRequestForm.priority));
        }
    }

    private void cancelRoomRequest(RoomRequest request) {
        if (request.hasRoomAssign()) {
            Room room = (Room) roomRepository.findRoomByNumber(request.getRoomNumber());
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
        persistReservation(roomRequest);
    }

    private void persistReservation(RoomRequest roomRequest) {
        roomRequest.confirm(notificationSender, notificationFactory);
        roomRequestRepository.persist(roomRequest);
    }

    private void reserveRoom(Room room) {
        room.reserve();
        roomRepository.persist(room);
    }

    public RoomRequest getRequest(String email, RequestIdentifier roomRequestIdentifier) {
        RoomRequest roomRequest = (RoomRequest) roomRequestRepository.findReservationByIdentifier(roomRequestIdentifier);
        if (roomRequest == null) {
            roomRequest = mailbox.getRoomRequestByIdentifier(roomRequestIdentifier);
            if (roomRequest.hasSameOrganizer(email)) {
                return roomRequest;
            } else {
                throw new NotSameEmailException();
            }
        }
        return roomRequest;
    }

    private void releaseRoom(Room room) {
        room.release();
        roomRepository.persist(room);
    }
}
