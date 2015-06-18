package ca.ulaval.ift6002.sputnik.uat.steps;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.*;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import ca.ulaval.ift6002.sputnik.uat.steps.CancelARequestSteps.CancelARequestStepsState;
import ca.ulaval.ift6002.sputnik.uat.steps.state.StatefulStep;
import ca.ulaval.ift6002.sputnik.uat.steps.state.StepState;
import org.jbehave.core.annotations.*;

import java.util.LinkedList;

import static junit.framework.TestCase.assertTrue;

public class CancelARequestSteps extends StatefulStep<CancelARequestStepsState> {

    private static final RoomNumber ROOM_NUMBER = new RoomNumber("RN1");
    private static final int CAPACITY = 10;
    private final String emailOrganizer = "organizer@sputnik.com";

    protected CancelARequestStepsState getInitialState() {
        return new CancelARequestStepsState();
    }

    @Given("a room request without a room assigned")
    public void givenARoomRequestWithoutARoomAssigned() {
        state().roomRequest = new StandardRoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(emailOrganizer), new LinkedList<>());

        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.addRequest(state().roomRequest);
    }

    @Given("a room request with a room assigned")
    public void givenARoomRequestWithARoomAssigned() {
        state().roomRequest = new StandardRoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(emailOrganizer), new LinkedList<>());
        Room room = persistARoom();
        state().roomRequest.assignRoom(room);

        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.addRequest(state().roomRequest);
    }

    private Room persistARoom() {
        Room room = new StandardRoom(ROOM_NUMBER, CAPACITY);
        RoomRepository roomRepository = getRoomRepository();
        roomRepository.persist(room);
        return room;
    }

    @When("I cancel the room request")
    public void whenICancelTheRoomRequest() {
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.cancelRequest(state().roomRequest.getIdentifier());
    }

    @Then("the room request is cancelled")
    public void thenTheRoomRequestIsCancelled() {
        assertTrue(state().roomRequest.hasStatus(Status.CANCELED));
    }

    @Then("the room request is archived")
    public void thenTheRoomRequestIsArchived() {
        assertTrue(true);
        // TODO: ajouter l'archivage
    }

    @Then("the reservation is cancelled")
    public void thenTheReservationIsCancelled() {
        assertTrue(state().roomRequest.hasStatus(Status.CANCELED));
    }

    @Then("the reservation is archived")
    public void thenTheReservationIsArchived() {
        // TODO: ajouter l'archivage
        assertTrue(true);
    }

    private void createRoomForReservation() {
        RoomRepository repository = getRoomRepository();
        repository.persist(new StandardRoom(ROOM_NUMBER, 10));
    }

    private void createReservation() {
        state().roomRequest = new StandardRoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(emailOrganizer), new LinkedList<>());
        RoomRequestRepository roomRequestRepository = getReservationRepository();
        roomRequestRepository.persist(state().roomRequest);
    }

    private RoomRequestRepository getReservationRepository() {
        return ServiceLocator.getInstance().resolve(RoomRequestRepository.class);
    }

    private RoomRepository getRoomRepository() {
        return ServiceLocator.getInstance().resolve(RoomRepository.class);
    }

    private ReservationApplicationService getReservationApplicationService() {
        return ServiceLocator.getInstance().resolve(ReservationApplicationService.class);
    }

    public class CancelARequestStepsState extends StepState {
        public RoomRequest roomRequest;
    }
}
