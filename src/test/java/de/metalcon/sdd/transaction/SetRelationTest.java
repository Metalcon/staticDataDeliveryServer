package de.metalcon.sdd.transaction;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.metalcon.sdd.Sdd;

public class SetRelationTest extends ActionTestBase {

    private static final long NODE_ID = 1L;

    private static final long TO_ID = 2L;

    @Test
    public void testValidArguments() {
        tx.setRelation(NODE_ID, "Node1", "relation2", TO_ID);
        tx.setRelation(NODE_ID, "Node1", "relation2", Sdd.EMPTY_ID);
    }

    @Test
    public void testEmptyId() {
        try {
            tx.setRelation(Sdd.EMPTY_ID, "Node1", "relation2", TO_ID);
            fail("Expected EmptyIdException.");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testInvalidNodeType() {
        try {
            tx.setRelation(NODE_ID, "UnkownNodeType", "relation2", TO_ID);
            fail("Expected InvalidNodeTypeException.");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testInvalidRelation1() {
        try {
            tx.setRelation(NODE_ID, "Node1", "UnkownRelation", TO_ID);
            fail("Expected InvalidRelationException.");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testInvalidRelation2() {
        try {
            tx.setRelation(NODE_ID, "Node1", "relation1", TO_ID);
            fail("Expected InvalidRelationException.");
        } catch (IllegalArgumentException e) {
        }
    }

}
