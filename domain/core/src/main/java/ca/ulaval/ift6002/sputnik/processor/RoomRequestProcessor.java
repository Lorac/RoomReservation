package ca.ulaval.ift6002.sputnik.processor;

import ca.ulaval.ift6002.sputnik.domain.core.mailbox.Mailbox;
import ca.ulaval.ift6002.sputnik.domain.core.mailbox.MailboxObserver;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RoomRequestProcessor implements MailboxObserver {

    private final Mailbox mailbox;
    private Runnable functionToRun;

    private int thresholdMailboxCount;

    private ScheduledFuture<?> future;
    private ScheduledExecutorService executor;
    private TimeUnit timeUnit;
    private long period;

    public RoomRequestProcessor(Mailbox mailbox, Runnable functionToRun,
                                ScheduledExecutorService executor, long period, TimeUnit timeUnit, int thresholdMailboxCount) {
        this.mailbox = mailbox;
        this.functionToRun = functionToRun;
        this.period = period;
        this.timeUnit = timeUnit;
        this.executor = executor;
        this.thresholdMailboxCount = thresholdMailboxCount;
    }

    public void startAssignationAtFixedRate(long delay) {
        future = executor.scheduleAtFixedRate(functionToRun, delay, period, timeUnit);
    }

    private void startAssignationOnce() {
        executor.execute(functionToRun);
    }

    private void restartAssignation() {
        if (future != null) {
            future.cancel(false);
        }
        startAssignationOnce();

        startAssignationAtFixedRate(period);
    }

    @Override
    public void notifyNewRoomRequest() {
        if (mailbox.count() >= thresholdMailboxCount) {
            restartAssignation();
        }
    }

}
