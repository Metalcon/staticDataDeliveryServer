package de.metalcon.sdd.transaction;

import org.junit.Test;

import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidRelationException;

public class SetRelationTest extends ActionTestBase {

    private static final long NODE_ID = 1L;

    private static final long TO_ID = 2L;

    @Test
    public void testValidArguments() throws InvalidNodeTypeException,
            InvalidRelationException, AlreadyCommitedException {
        tx.setRelation(NODE_ID, "Node1", "relation2", TO_ID);
        tx.setRelation(NODE_ID, "Node1", "relation2", 0L);
    }

    @Test(
            expected = InvalidNodeTypeException.class)
    public void testInvalidNodeType() throws InvalidNodeTypeException,
            InvalidRelationException, AlreadyCommitedException {
        tx.setRelation(NODE_ID, "UnkownNodeType", "relation2", TO_ID);
    }

    @Test(
            expected = InvalidRelationException.class)
    public void testInvalidRelation1() throws InvalidNodeTypeException,
            InvalidRelationException, AlreadyCommitedException {
        tx.setRelation(NODE_ID, "Node1", "UnkownRelation", TO_ID);
    }

    @Test(
            expected = InvalidRelationException.class)
    public void testInvalidRelation2() throws InvalidNodeTypeException,
            InvalidRelationException, AlreadyCommitedException {
        tx.setRelation(NODE_ID, "Node1", "relation1", TO_ID);
    }

}
