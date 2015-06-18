package ca.ulaval.ift6002.sputnik.domain.core.room;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class RoomNumberTest {

    @Test
    public void whenComparingSameRoomNumberShouldReturnTrue() {
        RoomNumber roomNumber = new RoomNumber("1234");
        assertTrue(roomNumber.isSame(roomNumber));
    }

    @Test
    public void whenComparingTwoDifferentInstanceOfRoomNumberWithSameNumberShouldReturnTrue() {
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
