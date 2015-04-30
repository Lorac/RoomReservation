package ca.ulaval.ift6002.sputnik.domain.room;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class RoomNumber {

    private final String number;

    public RoomNumber(String number) {
        this.number = number;
    }

    public boolean isSame(RoomNumber roomNumber) {
        return EqualsBuilder.reflectionEquals(this, roomNumber);
    }

    public String describe() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomNumber that = (RoomNumber) o;

        return isSame(that);

    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }
}
