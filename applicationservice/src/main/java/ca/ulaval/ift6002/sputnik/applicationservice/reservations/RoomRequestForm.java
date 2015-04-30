package ca.ulaval.ift6002.sputnik.applicationservice.reservations;

import javax.xml.bind.annotation.XmlElement;

public class RoomRequestForm {

    @XmlElement(name = "nombrePersonne")
    public int numberOfPeople;
    @XmlElement(name = "courrielOrganisateur")
    public String organizerEmail;
    @XmlElement(name = "priorite")
    public int priority;
}
