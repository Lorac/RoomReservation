package ca.ulaval.ift6002.sputnik.uat.steps.fixtures;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.RoomRequestForm;
import ca.ulaval.ift6002.sputnik.applicationservice.shared.locator.ServiceLocator;
import ca.ulaval.ift6002.sputnik.domain.core.request.Priority;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequestFactory;

public class RoomRequestBuilder {

    private RoomRequestForm form;

    public RoomRequestBuilder() {
        this.form = new RoomRequestForm();
    }

    public RoomRequestBuilder withPriority(Priority priority) {
        form.priority = priority;
        return this;
    }

    public RoomRequestBuilder withOrganizerEmail(String organizerEmail) {
        form.organizerEmail = organizerEmail;
        return this;
    }

    public RoomRequestBuilder withAmountOfPeople(int amountOfPeople) {
        form.numberOfPeople = amountOfPeople;
        return this;
    }

    public RoomRequestForm buildForm() {
        return form;
    }

    public RoomRequest buildRoomRequest() {
        RoomRequestFactory factory = ServiceLocator.getInstance().resolve(RoomRequestFactory.class);
        return factory.create(form.organizerEmail, form.numberOfPeople, form.priority);
    }

    public static RoomRequestBuilder createValid() {
        return createValid(2);
    }

    public static RoomRequestBuilder createValid(int numberOfPeople) {
        return new RoomRequestBuilder().
                withAmountOfPeople(numberOfPeople).
                withOrganizerEmail("someone@someone.com").
                withPriority(Priority.HIGH);
    }

}
