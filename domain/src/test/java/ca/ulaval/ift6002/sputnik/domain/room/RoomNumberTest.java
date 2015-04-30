package ca.ulaval.ift6002.sputnik.domain.room;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RoomNumberTest {

    @Test
    public void whenComparingSameRoomNumberShouldReturnTrue() {
        RoomNumber roomNumber = new RoomNumber("1234");
        assertTrue(roomNumber.isSame(roomNumber));
    }

    @Test
    public void whenComparingTwoDifferentInstanceOfRoomNumberWithEqualNumberShouldReturnTrue() {
        RoomNumber roomNumber = new RoomNumber("12345");
        RoomNumber anotherRoomNumber = new RoomNumber("12345");

        assertTrue(roomNumber.equals(anotherRoomNumber));
    }

    @Test
    public void whenComparingTwoDifferentRoomNumberShouldBeFalse() {
        RoomNumber roomNumber = new RoomNumber("12345");
        RoomNumber anotherRoomNumber = new RoomNumber("123456");

        assertFalse(roomNumber.equals(anotherRoomNumber));
    }
}
