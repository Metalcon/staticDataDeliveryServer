package de.metalcon.sdd;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Worker implements Runnable {

    private Sdd sdd;

    private Thread thread = null;

    private boolean running = false;

    private boolean stopping = false;

    private boolean busy = false;

    private BlockingQueue<WriteTransaction> transactions =
            new LinkedBlockingDeque<WriteTransaction>();

    /* package */Worker(
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
                    WriteTransaction transaction = transactions.take();
                    busy = true;

                    Queue<Action> actions = transaction.getActions();
                    sdd.startTransaction();
                    while (!actions.isEmpty()) {
                        Action action = actions.poll();
                        action.runAction(actions);
                    }
                    sdd.endTransaction();
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

    public boolean queueTransaction(WriteTransaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("transaction was null.");
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
                    thread.join();
                } catch (InterruptedException e) {
                    // stopped by server
                }
            }
        }
    }
}
