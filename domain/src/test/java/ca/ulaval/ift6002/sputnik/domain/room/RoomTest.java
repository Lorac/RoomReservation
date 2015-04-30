package ca.ulaval.ift6002.sputnik.domain.room;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RoomTest {

    private static final String GOOD_ROOMNUMBER = "GOOD";
    private static final String BAD_ROOMNUMBER = "BAD";

    private static final int HIGH_CAPACITY_ROOM = 500;
    private static final int LOW_CAPACITY_ROOM = 10;

    private Room room;
    private Room anotherRoom;

    @Before
    public void setUp() {
        RoomNumber goodRoomNumber = new RoomNumber(GOOD_ROOMNUMBER);
        RoomNumber badRoomNumber = new RoomNumber(BAD_ROOMNUMBER);

        room = new Room(goodRoomNumber, HIGH_CAPACITY_ROOM);
        anotherRoom = new Room(badRoomNumber, LOW_CAPACITY_ROOM);
    }

    @Test
    public void whenRoomIsInitializedShouldBeAvailable() {
        assertFalse(room.isReserved());
    }

    @Test
    public void whenRoomIsReleasedShouldBeAvailable() {
        room.release();
        assertFalse(room.isReserved());
    }

    @Test
    public void WhenRoomIsReservedShouldNotBeAvailable() {
        room.reserve();
        assertTrue(room.isReserved());
    }

    @Test
    public void whenAskedWithGoodRoomNumberWillReturnTrue() {
        assertTrue(room.hasSameNumber(room));
    }

    @Test
    public void whenAskedWithWrongRoomNumberWillReturnFalse() {
        assertFalse(room.hasSameNumber(anotherRoom));
    }

    @Test
    public void whenRoomIsReserveThenReleasedShouldNotBeReserved() {
        room.reserve();

        room.release();

        assertFalse(room.isReserved());
    }

    @Test
    public void whenRoomComparedWithSameRoomNumberShouldReturnTrue() {
        assertTrue(room.hasNumber(room.getRoomNumber()));
    }

    @Test
    public void whenRoomComparedWithDifferentRoomShouldReturnFalse() {
        assertFalse(room.hasNumber(anotherRoom.getRoomNumber()));
    }

    @Test
    public void whenRoomIsComparedWithHigherRequestedCapacityShouldReturnFalse() {
        assertFalse(anotherRoom.hasCapacity(HIGH_CAPACITY_ROOM));
    }

    @Test
    public void whenRoomIsComparedWithLowerRequestedCapacityShouldReturnTrue() {
        assertTrue(anotherRoom.hasCapacity(LOW_CAPACITY_ROOM));
    }
}
