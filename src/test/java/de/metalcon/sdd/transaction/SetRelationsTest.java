package de.metalcon.sdd.transaction;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.metalcon.sdd.Sdd;

public class SetRelationsTest extends ActionTestBase {

    private static final long NODE_ID = 1L;

    private static final long[][] TO_IDS = {
        {
            2L
        }, {
            2L, 3L
        }, {}
    };

    @Test
    public void testValidArguments() {
        for (long[] toIds : TO_IDS) {
            tx.setRelations(NODE_ID, "Node1", "relation1", toIds);
            tx.setRelations(NODE_ID, "Node2", "relation1", toIds);
        }
    }

    @Test
    public void testEmptyId1() {
        for (long[] toIds : TO_IDS) {
            try {
                tx.setRelations(Sdd.EMPTY_ID, "Node1", "relation1", toIds);
                fail("Expected EmptyIdException.");
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testEmptyId2() {
        for (long[] toIds : TO_IDS) {
            long[] toIdsWithEmpty = new long[toIds.length + 1];
            System.arraycopy(toIds, 0, toIdsWithEmpty, 0, toIds.length);
            toIdsWithEmpty[toIds.length] = Sdd.EMPTY_ID;
            try {
                tx.setRelations(NODE_ID, "Node1", "relation1", toIdsWithEmpty);
                fail("Expected EmptyIdException.");
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testInvalidNodeType() {
        for (long[] toIds : TO_IDS) {
            try {
                tx.setRelations(NODE_ID, "UnkownNodeType", "relation1", toIds);
                fail("Expected InvalidNodeTypeException.");
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testInvalidRelation1() {
        for (long[] toIds : TO_IDS) {
            try {
                tx.setRelations(NODE_ID, "Node1", "UnkownRelation", toIds);
                fail("Expected InvalidRelationException.");
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testInvalidRelation2() {
        for (long[] toIds : TO_IDS) {
            try {
                tx.setRelations(NODE_ID, "Node1", "relation2", toIds);
                fail("Expected InvalidRelationException.");
            } catch (IllegalArgumentException e) {
            }
        }
    }

}
