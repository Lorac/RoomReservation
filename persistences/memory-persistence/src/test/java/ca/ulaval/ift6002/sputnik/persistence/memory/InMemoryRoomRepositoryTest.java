package ca.ulaval.ift6002.sputnik.persistence.memory;

import ca.ulaval.ift6002.sputnik.domain.core.room.Room;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNotFoundException;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import ca.ulaval.ift6002.sputnik.domain.core.room.RoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.willReturn;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryRoomRepositoryTest {

    private static final int THREE_ROOMS = 3;
    private RoomRepository roomRepository;

    @Mock
    private Room room;

    @Mock
    private Room anotherRoom;

    @Mock
    private Room thirdRoom;

    @Mock
    private RoomNumber roomNumberForRoom;

    @Mock
    private RoomNumber roomNumberForAnotherRoom;

    @Before
    public void setUp() {
        roomRepository = new InMemoryRoomRepository();
        willReturn(true).given(room).hasSameNumber(room);
        willReturn(false).given(room).hasSameNumber(anotherRoom);
        willReturn(true).given(room).hasNumber(roomNumberForRoom);
        willReturn(true).given(anotherRoom).hasNumber(roomNumberForAnotherRoom);
        willReturn(roomNumberForRoom).given(room).getRoomNumber();
        willReturn(roomNumberForAnotherRoom).given(anotherRoom).getRoomNumber();
    }

    @Test
    public void whenFindARoomByNumberShouldReturnTheRoomWithThatNumber() {
        roomRepository.persist(room);

        Room roomReturned = (Room) roomRepository.findRoomByNumber(roomNumberForRoom);

        assertNotNull(roomReturned);
    }

    @Test
    public void givenThreeRoomWhenFindAllShouldReturnThreeAddedRooms() {
        givenThreeRooms();

        List<Room> rooms = roomRepository.findAll();

        assertEquals(THREE_ROOMS, rooms.size());
    }

    @Test(expected = RoomNotFoundException.class)
    public void whenFindARoomByNumberWithWrongNumberShouldThrow() {
        roomRepository.findRoomByNumber(roomNumberForAnotherRoom);
    }

    private void givenThreeRooms() {
        roomRepository.persist(room);
        roomRepository.persist(anotherRoom);
        roomRepository.persist(thirdRoom);
    }

}
