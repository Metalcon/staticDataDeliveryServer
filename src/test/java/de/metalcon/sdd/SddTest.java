package de.metalcon.sdd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;
import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.EmptyTransactionException;
import de.metalcon.sdd.exception.InvalidConfigException;
import de.metalcon.sdd.exception.InvalidDetailException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidPropertyException;
import de.metalcon.sdd.exception.InvalidRelationException;

public class SddTest {

    private Config config;

    private Sdd sdd;

    @Before
    public void setUp() throws InvalidConfigException, IOException {
        config = new XmlConfig("src/test/resources/testConfig.xml");
        config.makeTemporary();

        sdd = new Sdd(config);
    }

    @After
    public void tearDown() throws IOException {
        sdd.close();
    }

    @Test
    public void testSdd() throws InvalidNodeTypeException,
            InvalidPropertyException, AlreadyCommitedException,
            InvalidRelationException, EmptyTransactionException,
            InvalidDetailException {
        WriteTransaction tx = sdd.createWriteTransaction();
        Map<String, String> emptyprops = new HashMap<String, String>();
        tx.setProperties(31L, "Node1", emptyprops);
        tx.setProperties(32L, "Node2", emptyprops);
        tx.setProperties(42L, "Node2", emptyprops);
        tx.setProperties(52L, "Node2", emptyprops);
        tx.setProperties(33L, "Node3", emptyprops);
        tx.setRelations(31L, "Node1", "relation1", new long[] {
            32L, 42L
        });
        tx.addRelations(31L, "Node1", "relation1", new long[] {
            52L
        });
        tx.setRelation(31L, "Node1", "relation2", 33L);
        tx.commit();

        sdd.waitUntilQueueEmpty();

        System.out.println("sdd.read(31L, \"detail1\") = "
                + sdd.read(31L, "detail1"));
    }

}
