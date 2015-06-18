package ca.ulaval.ift6002.sputnik.applicationservice.reservations;

import ca.ulaval.ift6002.sputnik.domain.core.request.Priority;

import javax.xml.bind.annotation.XmlElement;

public class RoomRequestForm {

    @XmlElement(name = "nombrePersonne")
    public int numberOfPeople;
    @XmlElement(name = "courrielOrganisateur")
    public String organizerEmail;
    @XmlElement(name = "priorite")
    public Priority priority;
}
