package de.metalcon.sdd.transaction;

import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

import de.metalcon.sdd.StaticSddTestBase;
import de.metalcon.sdd.TestAction;
import de.metalcon.sdd.WriteTransaction;

public class WriteTransactionTest extends StaticSddTestBase {

    // TODO: Test sometimes ends fine with TransactionFailureException printed
    // in output. Test result is still ok. Will probably be fixed if
    // TransactionFailureException in Sdd is fixed.

    private static final int TEST_VALID_ACTIONS_NUM_ROUNDS = 100;

    private static final int TEST_VALID_ACTIONS_MAX_ACTIONS = 10;

    private Random random = new Random();

    private WriteTransaction tx;

    @Test
    public void testValidActions() {
        for (int i = 0; i != TEST_VALID_ACTIONS_NUM_ROUNDS; ++i) {
            tx = sdd.createWriteTransaction();

            int numActions = random.nextInt(TEST_VALID_ACTIONS_MAX_ACTIONS) + 1;
            for (int j = 0; j != numActions; ++j) {
                TestAction.performValidAction(tx,
                        random.nextInt(TestAction.NUM_VALID_ACTIONS));
            }

            tx.commit();
        }
    }

    @Test
    public void testEmptyCommit() {
        tx = sdd.createWriteTransaction();
        try {
            tx.commit();
            fail("Expected EmptyTransactionException.");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testAlreadyCommited1() {
        for (int i = 0; i != TestAction.NUM_VALID_ACTIONS; ++i) {
            tx = sdd.createWriteTransaction();
            TestAction.performValidAction(tx, i);
            tx.commit();
            try {
                tx.commit();
                fail("Expected AlreadyCommitedException.");
            } catch (IllegalStateException e) {
            }
        }
    }

    @Test
    public void testAlreadyCommited2() {
        for (int i = 0; i != TestAction.NUM_VALID_ACTIONS; ++i) {
            for (int j = 0; j != TestAction.NUM_VALID_ACTIONS; ++j) {
                tx = sdd.createWriteTransaction();
                TestAction.performValidAction(tx, i);
                tx.commit();
                try {
                    TestAction.performValidAction(tx, j);
                    fail("Expected AlreadyCommitedException");
                } catch (IllegalStateException e) {
                }
            }
        }
    }
}
