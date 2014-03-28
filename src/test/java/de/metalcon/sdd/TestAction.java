package de.metalcon.sdd;

import java.util.HashMap;
import java.util.Map;

public class TestAction {

    public static final int NUM_VALID_ACTIONS = 4;

    public static final long NODE_ID = 1L;

    public static final String NODE_TYPE = "Node1";

    public static final Map<String, String> PROPERTIES;
    static {
        PROPERTIES = new HashMap<String, String>();
        PROPERTIES.put("property1", "foo");
    }

    public static final String RELATION = "relation2";

    public static final String RELATIONS = "relation1";

    public static final long TO_ID = 2L;

    public static final long[] TO_IDS = new long[] {
        3L, 4L
    };

    public static void performValidAction(WriteTransaction tx, int action) {
        switch (action) {
            case 0:
                tx.setProperties(NODE_ID, NODE_TYPE, PROPERTIES);
                break;

            case 1:
                tx.setRelation(NODE_ID, NODE_TYPE, RELATION, TO_ID);
                break;

            case 2:
                tx.setRelations(NODE_ID, NODE_TYPE, RELATIONS, TO_IDS);
                break;

            case 3:
                tx.addRelations(NODE_ID, NODE_TYPE, RELATIONS, TO_IDS);
                break;

            //            case 4:
            //                tx.delete(NODE_ID);
            //                break;
            //
            //            case 5:
            //                tx.deleteRelations(NODE_ID, NODE_TYPE, RELATIONS, TO_IDS);
            //                break;

            case NUM_VALID_ACTIONS:
                throw new IllegalStateException("Unkown action number.");

        }
    }

}
