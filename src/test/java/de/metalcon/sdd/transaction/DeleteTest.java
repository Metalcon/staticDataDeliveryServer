package de.metalcon.sdd.transaction;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.EmptyIdException;

public class DeleteTest extends ActionTestBase {

    private static final long NODE_ID = 1L;

    @Test
    public void testValidArguments() throws EmptyIdException {
        tx.delete(NODE_ID);
    }

    @Test
    public void testEmptyId() {
        try {
            tx.delete(Sdd.EMPTY_ID);
            fail("Expected EmptyIdException.");
        } catch (EmptyIdException e) {
        }
    }

}
