package ca.ulaval.ift6002.sputnik.uat.steps;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.*;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import ca.ulaval.ift6002.sputnik.strategy.assignation.FindRoomStrategy;
import ca.ulaval.ift6002.sputnik.strategy.assignation.MaximizeRoomUsageStrategy;
import ca.ulaval.ift6002.sputnik.uat.steps.MaximizeRoomUsageSteps.MaximizeRoomUsageStepsState;
import ca.ulaval.ift6002.sputnik.uat.steps.state.StatefulStep;
import ca.ulaval.ift6002.sputnik.uat.steps.state.StepState;
import org.jbehave.core.annotations.*;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;


public class MaximizeRoomUsageSteps extends StatefulStep<MaximizeRoomUsageStepsState> {

    private final static int NUMBER_OF_ATTENDEES = 12;
    private final static String EMAIL_ORGANIZER = "organizer@sputnik.com";
    private final static String EMAIL_ATTENDEES = "attendees%s@c.sputnik.com";
    private static final int NUMBER_OF_ROOM_FOUND = 1;

    protected MaximizeRoomUsageStepsState getInitialState() {
        return new MaximizeRoomUsageStepsState();
    }

    @Given("a room request with attendees")
    public void givenARoomRequestWithAttendees() {
        state().roomRequest = new StandardRoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(EMAIL_ORGANIZER), getAttendees(NUMBER_OF_ATTENDEES));
        switchToMaximumRoomUsageStrategy();
        Mailbox mailbox = getMailbox();
        mailbox.add(state().roomRequest);
    }

    @Given("multiple unreserved rooms of different capacities")
    public void givenUnreservedRoomsOfDifferentCapacities() {
        RoomRepository roomRepository = getRoomRepository();

        roomRepository.persist(new StandardRoom(new RoomNumber("SPUT-10"), 10));
        roomRepository.persist(new StandardRoom(new RoomNumber("SPUT-20"), 20));
        roomRepository.persist(new StandardRoom(new RoomNumber("SPUT-30"), 30));
    }

    @Given("multiple unreserved rooms of same capacities")
    public void givenUnreservedRoomsOfSmeCapacities() {
        RoomRepository roomRepository = getRoomRepository();

        roomRepository.persist(new StandardRoom(new RoomNumber("SPUT-20"), 20));
        roomRepository.persist(new StandardRoom(new RoomNumber("SPUT-20"), 20));
        roomRepository.persist(new StandardRoom(new RoomNumber("SPUT-20"), 20));
    }

    @When("the room requests are processed")
    public void whenIAssignARoomRequest() {
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.assignRoomRequest();
    }

    @Then("the smallest room fitting the request needs is found")
    public void thenTheSmallestRoomIsFound() {
        RoomRequest reservation = findReservationWithRequest(state().roomRequest);

        assertTrue(reservation.getRoomNumber().isSame(new RoomNumber("SPUT-20")));
    }

    @Then("any room fitting the request needs is found")
    public void thenARoomIsFound() {
        RoomRequestRepository roomRequestRepository = getRoomRequestRepository();
        assertTrue(roomRequestRepository.findAll().size() == NUMBER_OF_ROOM_FOUND);
    }

    private void switchToMaximumRoomUsageStrategy() {
        ServiceLocator.getInstance().unregister(FindRoomStrategy.class);
        ServiceLocator.getInstance().register(FindRoomStrategy.class, new MaximizeRoomUsageStrategy());
        ServiceLocator.getInstance().unregister(ReservationApplicationService.class);
        ServiceLocator.getInstance().register(ReservationApplicationService.class, new ReservationApplicationService());
    }

    private RoomRepository getRoomRepository() {
        return ServiceLocator.getInstance().resolve(RoomRepository.class);
    }

    private RoomRequestRepository getRoomRequestRepository() {
        return ServiceLocator.getInstance().resolve(RoomRequestRepository.class);
    }

    private List<User> getAttendees(int numberOfAttendees) {
        List<User> attendees = new LinkedList<>();
        for (int i = 0; i < numberOfAttendees; i++) {
            attendees.add(new User(String.format(EMAIL_ATTENDEES, i)));
        }
        return attendees;
    }

    private Mailbox getMailbox() {
        return ServiceLocator.getInstance().resolve(Mailbox.class);
    }

    private ReservationApplicationService getReservationApplicationService() {
        return ServiceLocator.getInstance().resolve(ReservationApplicationService.class);
    }

    private RoomRequest findReservationWithRequest(RoomRequest roomRequest) {
        RoomRequestRepository roomRequestRepository = getRoomRequestRepository();
        RequestIdentifier identifier = roomRequest.getIdentifier();
        return (RoomRequest) roomRequestRepository.findReservationByIdentifier(identifier);
    }

    protected class MaximizeRoomUsageStepsState extends StepState {

    }
}
