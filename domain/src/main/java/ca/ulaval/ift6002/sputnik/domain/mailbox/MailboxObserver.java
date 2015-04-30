package ca.ulaval.ift6002.sputnik.domain.mailbox;

@FunctionalInterface
public interface MailboxObserver {

    void notifyNewRoomRequest();
}
