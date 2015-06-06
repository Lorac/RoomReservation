package ca.ulaval.ift6002.sputnik.interfaces.rest.resources;


import ca.ulaval.ift6002.sputnik.applicationservice.reservations.ReservationApplicationService;
import ca.ulaval.ift6002.sputnik.domain.core.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.core.request.RoomRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/demandes/{email}/{requestIdentifier}")
@Produces(MediaType.APPLICATION_JSON)
public class RoomRequestResource {

    private final ReservationApplicationService service;

    public RoomRequestResource() {
        service = new ReservationApplicationService();
    }

    public RoomRequestResource(ReservationApplicationService service) {
        this.service = service;
    }

    @GET
    public RoomRequest getRoomRequest(
            @PathParam("email") String email,
            @PathParam("requestIdentifier") String requestIdentifierString) {

        RequestIdentifier requestIdentifier = new RequestIdentifier(UUID.fromString(requestIdentifierString));

        return service.getRequest(email, requestIdentifier);
    }
}
