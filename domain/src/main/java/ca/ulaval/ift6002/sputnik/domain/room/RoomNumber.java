package ca.ulaval.ift6002.sputnik.domain.room;

import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RoomNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ROOM_NUMBER", nullable = true)
    private String number;

    public RoomNumber(String number) {
        this.number = number;
    }

    public RoomNumber() {
    }

    public boolean isSame(RoomNumber roomNumber) {
        return EqualsBuilder.reflectionEquals(this, roomNumber);
    }

    public String describe() {
        return number;
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomNumber that = (RoomNumber) o;

        return isSame(that);

    }
}
