package ca.ulaval.ift6002.sputnik.uat.steps;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.domain.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.request.Priority;
import ca.ulaval.ift6002.sputnik.domain.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequestRepository;
import ca.ulaval.ift6002.sputnik.domain.room.Room;
import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.room.RoomRepository;
import ca.ulaval.ift6002.sputnik.domain.user.User;
import ca.ulaval.ift6002.sputnik.strategy.sorting.PriorityStrategy;
import ca.ulaval.ift6002.sputnik.uat.steps.AssignRoomsPeriodicallyToRoomRequestSteps.AssignRoomsStepsState;
import ca.ulaval.ift6002.sputnik.uat.steps.state.StatefulStep;
import ca.ulaval.ift6002.sputnik.uat.steps.state.StepState;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.LinkedList;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class AssignRoomsPeriodicallyToRoomRequestSteps extends StatefulStep<AssignRoomsStepsState> {

    private static final String EMAIL = "valid@uat.com";
    private static final int FIVE = 5;

    protected AssignRoomsStepsState getInitialState() {
        return new AssignRoomsStepsState();
    }

    @Given("a first room request")
    public void givenAFirstRoomRequest() {
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        state().firstRequest = new RoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(EMAIL), new LinkedList<>());
        reservationApplicationService.addRequest(state().firstRequest);
    }

    @Given("a second room request")
    public void givenASecondRoomRequest() {
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        state().secondRequest = new RoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(EMAIL), new LinkedList<>());
        reservationApplicationService.addRequest(state().secondRequest);
    }

    @Given("multiple room requests")
    public void givenAnAmountOfRoomRequest() {
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        for (int i = 0; i < FIVE; i++) {
            RoomRequest roomRequest = new RoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(EMAIL), new LinkedList<>());
            reservationApplicationService.addRequest(roomRequest);
        }
    }

    @Given("multiple available rooms")
    public void givenAnAmountOfFiveRooms() {
        RoomRepository repository = getRoomRepository();
        for (int i = 0; i < FIVE; i++) {
            repository.persist(new Room(new RoomNumber(String.format("SPUT-%s", i)), 10));
        }
    }

    @Given("a room request with low priority")
    public void givenARoomRequestWithPriorityLow() {
        switchToPrioritySortingStrategy();
        state().lowPriorityRequest = new RoomRequest(RequestIdentifier.create(), Priority.LOW, new User(EMAIL), new LinkedList<>());
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.addRequest(state().lowPriorityRequest);
    }

    @Given("a room request with high priority")
    public void givenARoomRequestWithPriorityHigh() {
        state().highPriorityRequest = new RoomRequest(RequestIdentifier.create(), Priority.HIGH, new User(EMAIL), new LinkedList<>());
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.addRequest(state().highPriorityRequest);
    }

    @When("the room requests are processed")
    public void whenIAssignRoomsToRoomRequest() {
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.assignRoomRequest();
    }

    @Then("all the room request are assigned to a room")
    public void thenTheRoomRequestAreAssignedRooms() {
        RoomRequestRepository roomRequestRepository = getReservationRepository();
        assertThat(roomRequestRepository.findAll().size(), greaterThan(0));
    }

    @Then("the room request with higher priority is assigned before")
    public void thenTheRoomRequestWithHigherPriorityIsAssignBefore() {
        RoomRequest highPriorityRequest = findAssignedRequestWithRequest(state().highPriorityRequest);
        RoomRequest lowPriorityRequest = findAssignedRequestWithRequest(state().lowPriorityRequest);

        assertTrue(highPriorityRequest.getTimeOfAssignation().isBefore(lowPriorityRequest.getTimeOfAssignation()));
    }

    @Then("the first room request is assigned before the second request")
    public void thenTheFirstRoomRequestIsAssignBeforeTheSecondRequest() {
        RoomRequest firstRequest = findAssignedRequestWithRequest(state().firstRequest);
        RoomRequest secondRequest = findAssignedRequestWithRequest(state().secondRequest);

        assertTrue(firstRequest.getTimeOfAssignation().isBefore(secondRequest.getTimeOfAssignation()));
    }

    private RoomRepository getRoomRepository() {
        return ServiceLocator.getInstance().resolve(RoomRepository.class);
    }

    private RoomRequestRepository getReservationRepository() {
        return ServiceLocator.getInstance().resolve(RoomRequestRepository.class);
    }

    private ReservationApplicationService getReservationApplicationService() {
        return ServiceLocator.getInstance().resolve(ReservationApplicationService.class);
    }

    private void switchToPrioritySortingStrategy() {
        ServiceLocator.getInstance().unregister(Mailbox.class);
        ServiceLocator.getInstance().register(Mailbox.class, new Mailbox(new PriorityStrategy()));
        ServiceLocator.getInstance().unregister(ReservationApplicationService.class);
        ServiceLocator.getInstance().register(ReservationApplicationService.class, new ReservationApplicationService());
    }

    private RoomRequest findAssignedRequestWithRequest(RoomRequest roomRequest) {
        RoomRequestRepository roomRequestRepository = getReservationRepository();
        RequestIdentifier identifier = roomRequest.getIdentifier();
        return roomRequestRepository.findReservationByIdentifier(identifier);
    }

    public class AssignRoomsStepsState extends StepState {
        public RoomRequest highPriorityRequest;
        public RoomRequest lowPriorityRequest;
        public RoomRequest firstRequest;
        public RoomRequest secondRequest;
    }
}
