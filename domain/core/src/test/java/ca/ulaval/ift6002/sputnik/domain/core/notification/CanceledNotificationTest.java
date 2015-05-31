package ca.ulaval.ift6002.sputnik.domain.core.notification;

import ca.ulaval.ift6002.sputnik.domain.core.room.RoomNumber;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class CanceledNotificationTest {

    private static final String ROOM_NUMBER = "PLT-1111";

    @Test
    public void whenCreatingACanceledNotificationVerifyTheRoomNumberIsGood() {
        RoomNumber roomNumber = new RoomNumber(ROOM_NUMBER);
        CanceledNotification successNotification = new CanceledNotification(roomNumber);

        assertThat(successNotification.getMessage(), containsString(ROOM_NUMBER));
    }

    @Test
    public void whenCreateACancelledNotificationWithoutRoomNumberItShouldNotContainARoomNumber() {
        CanceledNotification successNotification = new CanceledNotification();
        assertThat(successNotification.getMessage(), not(containsString(ROOM_NUMBER)));
    }
}
