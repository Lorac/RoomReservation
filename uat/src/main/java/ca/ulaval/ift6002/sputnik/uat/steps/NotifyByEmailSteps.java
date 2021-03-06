package ca.ulaval.ift6002.sputnik.uat.steps;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.notification.*;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;
import ca.ulaval.ift6002.sputnik.domain.core.room.*;
import ca.ulaval.ift6002.sputnik.domain.core.user.User;
import ca.ulaval.ift6002.sputnik.uat.steps.NotifyByEmailSteps.NotifyByEmailAfterProcessingStepsState;
import ca.ulaval.ift6002.sputnik.uat.steps.state.StatefulStep;
import ca.ulaval.ift6002.sputnik.uat.steps.state.StepState;
import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.jbehave.core.annotations.*;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class NotifyByEmailSteps extends StatefulStep<NotifyByEmailAfterProcessingStepsState> {

    private static final RoomNumber ROOM_NUMBER = new RoomNumber("SPUT-1");
    private static final String EMAIL_ORGANIZER = "organizer@sputnik.com";
    private final int NUMBER_OF_EMAIL_FOR_ORGANIZER_AND_RESERVATION_CLERK = 1;
    private final int NUMBER_OF_EMAIL_FOR_ATTENDEES = 1;
    private final String emailAttendees = "attendees%s@sputnik.com";
    private final Room ASSIGNED_ROOM = new StandardRoom(ROOM_NUMBER, 10);

    protected NotifyByEmailAfterProcessingStepsState getInitialState() {
        return new NotifyByEmailAfterProcessingStepsState();
    }

    @Given("a room request")
    public void givenARoomRequest() {
        state().roomRequest = new StandardRoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(EMAIL_ORGANIZER), new LinkedList<>());
        Mailbox mailbox = getMailbox();
        mailbox.add(state().roomRequest);
    }

    @Given("a room request with an assigned room")
    public void givenRoomRequestWithAnAssignedRoom() {
        RoomRepository repository = getRoomRepository();
        RoomRequestRepository requestRepository = getRoomRequestRepository();
        repository.persist(ASSIGNED_ROOM);

        state().roomRequest = new StandardRoomRequest(RequestIdentifier.create(), Priority.NORMAL, new User(EMAIL_ORGANIZER), new LinkedList<>());
        state().roomRequest.assignRoom(ASSIGNED_ROOM);
        requestRepository.persist(state().roomRequest);
    }

    @Given("$roomCount unreserved rooms")
    public void givenUnreservedRooms(int roomCount) {
        RoomRepository repository = getRoomRepository();
        for (int i = 0; i < roomCount; i++) {
            Room room = new StandardRoom(new RoomNumber(String.format("SPUT-%s", i)), 10);
            repository.persist(room);
        }
    }

    @When("room requests are processed")
    public void whenRoomRequestsAreProcessed() {
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.assignRoomRequest();
    }

    @When("the room request is modified")
    public void whenRoomRequestIsCanceled() {
        ReservationApplicationService reservationApplicationService = getReservationApplicationService();
        reservationApplicationService.cancelRequest(state().roomRequest.getIdentifier());
    }

    @Then("an email is sent to the organizer and reservationClerk")
    public void thenOrganizerReceivesAnEmail() {
        SimpleSmtpServer mailServer = getMailServer();
        assertEquals(1, mailServer.getReceivedEmailSize());
    }

    @Then("the email indicates the room number that was assigned")
    public void thenEmailIndicatesRoomNumber() {
        RoomRequest reservation = findReservationWithRequest(state().roomRequest);

        SimpleSmtpServer mailServer = getMailServer();

        Iterator emailIter = mailServer.getReceivedEmail();
        SmtpMessage email = (SmtpMessage) emailIter.next();

        SuccessNotification notification = new SuccessNotification(reservation.getRoomNumber());

        assertEquals(email.getHeaderValue("Subject"), notification.getSubject());
        assertEquals(email.getBody(), notification.getMessage());
    }

    @Then("the email indicates the insufficient rooms refusal")
    public void thenEmailIndicatesReasonOfRefusal() {
        InsufficientRoomNotification notification = new InsufficientRoomNotification();
        verifyEmailContent(notification);
    }

    @Then("an email is sent to all attendees, organizer and reservation clerk")
    public void thenAnEmailIsSentToAllAttendees() {
        int numberOfEmailSent = NUMBER_OF_EMAIL_FOR_ATTENDEES + NUMBER_OF_EMAIL_FOR_ORGANIZER_AND_RESERVATION_CLERK;
        SimpleSmtpServer mailServer = getMailServer();
        assertEquals(numberOfEmailSent, mailServer.getReceivedEmailSize());
    }

    @Then("the email indicates that the room request with room assigned was canceled")
    public void thenEmailIndicatesReservationIsCanceled() {
        CanceledNotification notificationExpected = new CanceledNotification(ROOM_NUMBER);
        verifyEmailContent(notificationExpected);
    }

    @Then("the email indicates that the room request was canceled")
    public void thenEmailIndicatesRoomRequestIsCanceled() {
        CanceledNotification notificationExpected = new CanceledNotification();
        verifyEmailContent(notificationExpected);
    }

    private void verifyEmailContent(Notification notificationExpected) {
        SimpleSmtpServer mailServer = getMailServer();

        Iterator emailIter = mailServer.getReceivedEmail();
        SmtpMessage email = (SmtpMessage) emailIter.next();

        assertEquals(email.getHeaderValue("Subject"), notificationExpected.getSubject());
        assertEquals(email.getBody(), notificationExpected.getMessage());
    }

    private List<User> getAttendees(int numberOfAttendees) {
        List<User> attendees = new LinkedList<>();
        for (int i = 0; i < numberOfAttendees; i++) {
            attendees.add(new User(String.format(emailAttendees, i)));
        }
        return attendees;
    }

    private Mailbox getMailbox() {
        return ServiceLocator.getInstance().resolve(Mailbox.class);
    }

    private RoomRepository getRoomRepository() {
        return ServiceLocator.getInstance().resolve(RoomRepository.class);
    }

    private ReservationApplicationService getReservationApplicationService() {
        return ServiceLocator.getInstance().resolve(ReservationApplicationService.class);
    }

    private RoomRequestRepository getReservationRepository() {
        return ServiceLocator.getInstance().resolve(RoomRequestRepository.class);
    }

    private SimpleSmtpServer getMailServer() {
        return ServiceLocator.getInstance().resolve(SimpleSmtpServer.class);
    }

    private RoomRequest findReservationWithRequest(RoomRequest roomRequest) {
        RoomRequestRepository roomRequestRepository = getReservationRepository();
        RequestIdentifier identifier = roomRequest.getIdentifier();
        return (RoomRequest) roomRequestRepository.findReservationByIdentifier(identifier);
    }

    public RoomRequestRepository getRoomRequestRepository() {
        return ServiceLocator.getInstance().resolve(RoomRequestRepository.class);
    }

    public class NotifyByEmailAfterProcessingStepsState extends StepState {
    }
}
