package ca.ulaval.ift6002.sputnik.applicationservice.reservations;

import ca.ulaval.ift6002.sputnik.domain.core.request.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlElement;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomRequestJSONRepresentation {

    @XmlElement(name = "nombrePersonne")
    public int numberOfPeople;
    @XmlElement(name = "courrielOrganisateur")
    public String organizerEmail;
    @XmlElement(name = "statutDemande")
    public Status status;
    @XmlElement(name = "salleAssigne")
    public String roomNumber;
}
