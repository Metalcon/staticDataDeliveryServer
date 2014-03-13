package de.metalcon.sdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.FileConfig;
import de.metalcon.sdd.exception.InvalidConfigException;

public class FileConfigTest {

    private Config config;

    @Before
    public void setUp() throws InvalidConfigException {
        config =
                new FileConfig(
                        Paths.get("src/test/resources/fileConfigTest.xml"));
    }

    @Test
    public void testLeveldbPath() throws InvalidConfigException {
        assertEquals("leveldbpath", config.getLeveldbPath());
    }

    @Test
    public void testNeo4jPath() throws InvalidConfigException {
        assertEquals("neo4jpath", config.getNeo4jPath());
    }

    @Test
    public void testDetails() {
        assertTrue(config.isValidDetail("detail0"));
        assertTrue(config.isValidDetail("detail1"));
        assertFalse(config.isValidDetail("invaliddetail"));
    }

    @Test
    public void testEntities() {
        assertNotNull(config.getEntity("entity0"));
        assertNotNull(config.getEntity("entity1"));
        assertNotNull(config.getEntity("entity2"));
        assertNotNull(config.getEntity("entity3"));
        assertNull(config.getEntity("invalidentity"));
    }

    @Test
    public void testValidate() throws InvalidConfigException {
        config.validate();
    }

}
