package ca.ulaval.ift6002.sputnik.domain.core.mailbox;

@FunctionalInterface
public interface MailboxObserver {

    void notifyNewRoomRequest();
}
