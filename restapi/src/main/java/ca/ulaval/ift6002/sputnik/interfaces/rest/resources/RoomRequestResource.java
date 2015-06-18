package ca.ulaval.ift6002.sputnik.interfaces.rest.resources;

import ca.ulaval.ift6002.sputnik.applicationservice.reservations.*;
import ca.ulaval.ift6002.sputnik.domain.core.request.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

@Path("/demandes/")
@Produces(MediaType.APPLICATION_JSON)
public class RoomRequestResource {

    private final ReservationApplicationService service;

    public RoomRequestResource() {
        service = new ReservationApplicationService();
    }

    public RoomRequestResource(ReservationApplicationService service) {
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRoomRequest(RoomRequestForm roomRequestForm) {
        try {
            RequestIdentifier roomRequestIdentifier = service.addRequest(roomRequestForm);
            return Response.created(URI.create(convertToUrl(roomRequestForm.organizerEmail, roomRequestIdentifier)))
                    .build();
        } catch (InvalidRoomRequestFormApplicationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{email}/{roomRequestIdentifier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequest(@PathParam("email") String email, @PathParam("roomRequestIdentifier") String roomRequestIdentifierString) {
        RequestIdentifier roomRequestIdentifier = new RequestIdentifier(UUID.fromString(roomRequestIdentifierString));
        RoomRequest roomRequest;
        try {
            roomRequest = service.getRoomRequest(email, roomRequestIdentifier);
        } catch (RoomRequestNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }
        return Response.accepted(roomRequest).build();
    }

    private String convertToUrl(String patientIdentifier,
                                RequestIdentifier prescriptionIdentifier) {
        return String.format("/demandes/%s/%s", patientIdentifier,
                prescriptionIdentifier.describe());
    }
}
