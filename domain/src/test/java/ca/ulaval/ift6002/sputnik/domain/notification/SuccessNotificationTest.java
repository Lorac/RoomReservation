package ca.ulaval.ift6002.sputnik.domain.notification;

import ca.ulaval.ift6002.sputnik.domain.room.RoomNumber;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class SuccessNotificationTest {

    private static final String ROOM_NUMBER = "PLT-3453";

    @Test
    public void whenCreatingASuccessNotificationVerifyTheRoomNumberIsGood() throws Exception {
        RoomNumber roomNumber = new RoomNumber(ROOM_NUMBER);
        SuccessNotification successNotification = new SuccessNotification(roomNumber);

        assertThat(successNotification.getMessage(), containsString(ROOM_NUMBER));
    }
}
