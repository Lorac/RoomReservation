package ca.ulaval.ift6002.sputnik.domain.mailbox;

import ca.ulaval.ift6002.sputnik.domain.observable.Observable;
import ca.ulaval.ift6002.sputnik.domain.request.RequestIdentifier;
import ca.ulaval.ift6002.sputnik.domain.request.RoomRequest;
import ca.ulaval.ift6002.sputnik.strategy.sorting.SortingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Mailbox implements Observable {

    private Queue<RoomRequest> roomRequests;

    private List<MailboxObserver> mailboxObservers;
    private SortingStrategy sortingStrategy;

    public Mailbox(SortingStrategy sortingStrategy) {
        roomRequests = new ConcurrentLinkedQueue<>();
        mailboxObservers = new ArrayList<>();
        this.sortingStrategy = sortingStrategy;
    }

    public void add(RoomRequest roomRequest) {
        roomRequests.offer(roomRequest);
        notifyObservers();
    }

    public List<RoomRequest> collect() {
        List<RoomRequest> requests = new ArrayList<>();
        while (!roomRequests.isEmpty()) {
            requests.add(roomRequests.poll());
        }
        sortingStrategy.sortRoomRequest(requests);
        return requests;
    }

    public boolean isEmpty() {
        return roomRequests.isEmpty();
    }

    public int count() {
        return roomRequests.size();
    }

    @Override
    public void addObserver(MailboxObserver mailboxObserver) {
        mailboxObservers.add(mailboxObserver);
    }

    @Override
    public void notifyObservers() {
        for (MailboxObserver mailboxObserver : mailboxObservers) {
            mailboxObserver.notifyNewRoomRequest();
        }
    }

    public RoomRequest getRoomRequestByIdentifier(RequestIdentifier identifier) {
        return roomRequests.stream().filter(r -> r.hasIdentifier(identifier)).findFirst().get();
    }
}
