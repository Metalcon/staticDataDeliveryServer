package de.metalcon.sdd;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import de.metalcon.sdd.action.Action;
import de.metalcon.sdd.exception.EmptyTransactionException;

public class Worker implements Runnable {

    private Sdd sdd;

    private Thread thread = null;

    private boolean running = false;

    private boolean stopping = false;

    private boolean busy = false;

    private BlockingQueue<Queue<Action>> transactions =
            new PriorityBlockingQueue<Queue<Action>>();

    private Queue<Action> currentTransaction = null;

    public Worker(
            Sdd sdd) {
        this.sdd = sdd;
    }

    @Override
    public void run() {
        running = true;

        try {
            while (!stopping) {
                try {
                    busy = false;
                    if (currentTransaction == null) {
                        // waits until transaction has been queued
                        currentTransaction = transactions.take();
                    }
                    Action action = currentTransaction.poll();
                    busy = true;

                    action.runAction(sdd);

                    if (currentTransaction.isEmpty()) {
                        sdd.actionCommit();
                        currentTransaction = null;
                    }
                } catch (InterruptedException e) {
                    throw e;
                } catch (Exception e) {
                    // An error in a request does not terminate the server
                    // TODO: Log the error
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            // stopped by server
            // TODO: Store the queue until the server starts up again.
        }

        running = false;
        stopping = false;
    }

    public boolean queueTransaction(Queue<Action> transaction)
            throws EmptyTransactionException {
        if (transaction == null) {
            throw new IllegalArgumentException("transaction was null.");
        }
        if (transaction.isEmpty()) {
            throw new EmptyTransactionException();
        }
        return transactions.offer(transaction);
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if (running) {
            stopping = true;
            thread.interrupt();
        }
    }

    public boolean isIdle() {
        return !busy && transactions.isEmpty();
    }

    public void waitUntilQueueEmpty() {
        while (!isIdle()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // stopped
            }
        }
    }

    public void waitForShutdown() {
        if (stopping) {
            while (running) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // stopped by server
                }
            }
        }
    }
}
