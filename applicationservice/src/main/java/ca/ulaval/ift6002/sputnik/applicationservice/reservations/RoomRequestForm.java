package ca.ulaval.ift6002.sputnik.applicationservice.reservations;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class RoomRequestForm {

    @XmlElement(name = "nombrePersonne")
    public int numberOfPeople;
    @XmlElement(name = "courrielOrganisateur")
    public String organizerEmail;
    @XmlElement(name = "priorite")
    public int priority;
    @XmlElement(name = "participantsCourriels")
    public List<String> attendees;
}
