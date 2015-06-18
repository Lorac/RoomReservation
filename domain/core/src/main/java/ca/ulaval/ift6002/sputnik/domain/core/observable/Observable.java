package ca.ulaval.ift6002.sputnik.domain.core.observable;

import ca.ulaval.ift6002.sputnik.domain.core.mailbox.MailboxObserver;

public interface Observable {

    void addObserver(MailboxObserver mailboxObserver);

    void notifyObservers();
}
