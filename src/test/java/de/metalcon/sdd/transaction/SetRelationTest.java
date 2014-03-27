package de.metalcon.sdd.transaction;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.EmptyIdException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidRelationException;

public class SetRelationTest extends ActionTestBase {

    private static final long NODE_ID = 1L;

    private static final long TO_ID = 2L;

    @Test
    public void testValidArguments() throws InvalidNodeTypeException,
            InvalidRelationException, AlreadyCommitedException,
            EmptyIdException {
        tx.setRelation(NODE_ID, "Node1", "relation2", TO_ID);
        tx.setRelation(NODE_ID, "Node1", "relation2", Sdd.EMPTY_ID);
    }

    @Test
    public void testEmptyId() throws InvalidNodeTypeException,
            InvalidRelationException, AlreadyCommitedException {
        try {
            tx.setRelation(Sdd.EMPTY_ID, "Node1", "relation2", TO_ID);
            fail("Expected EmptyIdException.");
        } catch (EmptyIdException e) {
        }
    }

    @Test
    public void testInvalidNodeType() throws InvalidRelationException,
            AlreadyCommitedException, EmptyIdException {
        try {
            tx.setRelation(NODE_ID, "UnkownNodeType", "relation2", TO_ID);
            fail("Expected InvalidNodeTypeException.");
        } catch (InvalidNodeTypeException e) {
        }
    }

    @Test
    public void testInvalidRelation1() throws InvalidNodeTypeException,
            AlreadyCommitedException, EmptyIdException {
        try {
            tx.setRelation(NODE_ID, "Node1", "UnkownRelation", TO_ID);
            fail("Expected InvalidRelationException.");
        } catch (InvalidRelationException e) {
        }
    }

    @Test
    public void testInvalidRelation2() throws InvalidNodeTypeException,
            AlreadyCommitedException, EmptyIdException {
        try {
            tx.setRelation(NODE_ID, "Node1", "relation1", TO_ID);
            fail("Expected InvalidRelationException.");
        } catch (InvalidRelationException e) {
        }
    }

}
