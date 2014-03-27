package de.metalcon.sdd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;
import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.EmptyIdException;
import de.metalcon.sdd.exception.InvalidConfigException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidPropertyException;
import de.metalcon.sdd.exception.InvalidRelationException;

public abstract class StaticSddTestBase {

    protected static Sdd sdd;

    @BeforeClass
    public static void setUpStatic() throws InvalidConfigException, IOException {
        Config config = new XmlConfig("src/test/resources/testConfig.xml");
        config.makeTemporary();
        sdd = new Sdd(config);
    }

    @AfterClass
    public static void tearDownStatic() throws IOException {
        sdd.waitUntilQueueEmpty();
        sdd.close();
    }

    protected static final int NUM_VALID_ACTIONS = 6;

    protected static final long NODE_ID = 1L;

    protected static final String NODE_TYPE = "Node1";

    protected static final Map<String, String> PROPERTIES;
    static {
        PROPERTIES = new HashMap<String, String>();
        PROPERTIES.put("property1", "foo");
    }

    protected static final String RELATION = "relation2";

    protected static final String RELATIONS = "relation1";

    protected static final long TO_ID = 2L;

    protected static final long[] TO_IDS = new long[] {
        2L, 3L
    };

    protected static void performValidAction(WriteTransaction tx, int action)
            throws InvalidNodeTypeException, InvalidPropertyException,
            AlreadyCommitedException, InvalidRelationException,
            EmptyIdException {
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

            case 4:
                tx.delete(NODE_ID);
                break;

            case 5:
                tx.deleteRelations(NODE_ID, NODE_TYPE, RELATIONS, TO_IDS);
                break;

            case NUM_VALID_ACTIONS:
                throw new IllegalStateException("Unkown action number.");

        }
    }
}
