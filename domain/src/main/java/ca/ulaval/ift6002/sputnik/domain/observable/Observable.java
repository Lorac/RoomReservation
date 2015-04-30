package ca.ulaval.ift6002.sputnik.domain.observable;

import ca.ulaval.ift6002.sputnik.domain.mailbox.MailboxObserver;

public interface Observable {

    void addObserver(MailboxObserver mailboxObserver);

    void notifyObservers();
}
