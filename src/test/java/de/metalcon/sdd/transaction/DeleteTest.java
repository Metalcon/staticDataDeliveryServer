package de.metalcon.sdd.transaction;

import org.junit.Test;

import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.EmptyIdException;

public class DeleteTest extends ActionTestBase {

    private static final long NODE_ID = 1L;

    @Test
    public void testValidArguments() throws AlreadyCommitedException,
            EmptyIdException {
        tx.delete(NODE_ID);
    }

}
