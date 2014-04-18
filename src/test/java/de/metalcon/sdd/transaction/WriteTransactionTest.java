package de.metalcon.sdd.transaction;

import static de.metalcon.sdd.DynamicSddTestBase.closeSdd;
import static de.metalcon.sdd.DynamicSddTestBase.createSdd;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.TestAction;
import de.metalcon.sdd.WriteTransaction;

public class WriteTransactionTest {

    private static final int TEST_VALID_ACTIONS_NUM_ROUNDS = 100;

    private static final int TEST_VALID_ACTIONS_MAX_ACTIONS = 10;

    private Random random = new Random();

    @Test
    public void testValidActions() throws IOException {
        Sdd sdd;
        WriteTransaction tx;
        for (int i = 0; i != TEST_VALID_ACTIONS_NUM_ROUNDS; ++i) {
            sdd = createSdd();
            tx = sdd.createWriteTransaction();

            int numActions = random.nextInt(TEST_VALID_ACTIONS_MAX_ACTIONS) + 1;
            for (int j = 0; j != numActions; ++j) {
                TestAction.performValidAction(tx,
                        random.nextInt(TestAction.NUM_VALID_ACTIONS));
            }

            tx.commit();
            closeSdd(sdd);
        }
    }

    @Test
    public void testEmptyCommit() throws IOException {
        Sdd sdd = createSdd();
        WriteTransaction tx = sdd.createWriteTransaction();
        try {
            tx.commit();
            fail("Expected EmptyTransactionException.");
        } catch (IllegalStateException e) {
        }
        closeSdd(sdd);
    }

    @Test
    public void testAlreadyCommited1() throws IOException {
        Sdd sdd;
        WriteTransaction tx;
        for (int i = 0; i != TestAction.NUM_VALID_ACTIONS; ++i) {
            sdd = createSdd();
            tx = sdd.createWriteTransaction();
            TestAction.performValidAction(tx, i);
            tx.commit();
            try {
                tx.commit();
                fail("Expected AlreadyCommitedException.");
            } catch (IllegalStateException e) {
            }
            closeSdd(sdd);
        }
    }

    @Test
    public void testAlreadyCommited2() throws IOException {
        Sdd sdd;
        WriteTransaction tx;
        for (int i = 0; i != TestAction.NUM_VALID_ACTIONS; ++i) {
            for (int j = 0; j != TestAction.NUM_VALID_ACTIONS; ++j) {
                sdd = createSdd();
                tx = sdd.createWriteTransaction();
                TestAction.performValidAction(tx, i);
                tx.commit();
                try {
                    TestAction.performValidAction(tx, j);
                    fail("Expected AlreadyCommitedException");
                } catch (IllegalStateException e) {
                }
                closeSdd(sdd);
            }
        }
    }
}
