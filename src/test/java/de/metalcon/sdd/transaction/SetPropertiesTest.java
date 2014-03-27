package de.metalcon.sdd.transaction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.EmptyIdException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidPropertyException;

public class SetPropertiesTest extends ActionTestBase {

    private static long NODE_ID = 1L;

    private static final List<Map<String, String>> NODE1_PROPERTIES;
    static {
        Map<String, String> p1 = new HashMap<String, String>();
        p1.put("property1", "foo");
        p1.put("property2", "bar");

        Map<String, String> p2 = new HashMap<String, String>();
        p2.put("property1", "foo");

        Map<String, String> p3 = new HashMap<String, String>();
        p3.put("property2", "bar");

        NODE1_PROPERTIES = new LinkedList<Map<String, String>>();
        NODE1_PROPERTIES.add(p1);
        NODE1_PROPERTIES.add(p2);
        NODE1_PROPERTIES.add(p3);
    }

    private static final List<Map<String, String>> NODE2_PROPERTIES;
    static {
        Map<String, String> p1 = new HashMap<String, String>();
        p1.put("property1", "foo");

        NODE2_PROPERTIES = new LinkedList<Map<String, String>>();
        NODE2_PROPERTIES.add(p1);
    }

    @Test
    public void testValidArguments() throws InvalidNodeTypeException,
            InvalidPropertyException, AlreadyCommitedException,
            EmptyIdException {
        for (Map<String, String> properties : NODE1_PROPERTIES) {
            tx.setProperties(NODE_ID, "Node1", properties);
        }

        for (Map<String, String> properties : NODE2_PROPERTIES) {
            tx.setProperties(NODE_ID, "Node2", properties);
        }
    }

    @Test(
            expected = InvalidNodeTypeException.class)
    public void testInvalidNodeType() throws InvalidNodeTypeException,
            InvalidPropertyException, AlreadyCommitedException,
            EmptyIdException {
        tx.setProperties(NODE_ID, "UnkownNodeType", NODE1_PROPERTIES.get(0));
    }

    @Test(
            expected = InvalidPropertyException.class)
    public void testEmptyProperty() throws InvalidNodeTypeException,
            InvalidPropertyException, AlreadyCommitedException,
            EmptyIdException {
        tx.setProperties(NODE_ID, "Node1", new HashMap<String, String>());
    }

    @Test(
            expected = InvalidPropertyException.class)
    public void testInvalidProperty() throws InvalidNodeTypeException,
            InvalidPropertyException, AlreadyCommitedException,
            EmptyIdException {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("UnkownProperty", "foo");
        tx.setProperties(NODE_ID, "Node1", properties);
    }

}
