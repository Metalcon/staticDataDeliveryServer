package de.metalcon.sdd.transaction;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.metalcon.sdd.Sdd;

public class DeleteTest extends ActionTestBase {

    private static final long NODE_ID = 1L;

    @Test
    public void testValidArguments() {
        tx.delete(NODE_ID);
    }

    @Test
    public void testEmptyId() {
        try {
            tx.delete(Sdd.EMPTY_ID);
            fail("Expected EmptyIdException.");
        } catch (IllegalArgumentException e) {
        }
    }

}
