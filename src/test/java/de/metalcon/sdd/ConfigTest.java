package de.metalcon.sdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.ConfigNode;
import de.metalcon.sdd.config.RelationType;
import de.metalcon.sdd.config.TransactionMode;
import de.metalcon.sdd.config.XmlConfig;
import de.metalcon.sdd.exception.InvalidConfigException;

public class ConfigTest {

    private Config config;

    @Before
    public void setUp() throws InvalidConfigException, SAXException,
            IOException, ParserConfigurationException {
        config = new XmlConfig("src/test/resources/testConfig.xml");
    }

    @Test
    public void testGetLeveldbPath() {
        assertEquals("leveldbpath", config.getLeveldbPath());
    }

    @Test
    public void testGetNeo4jPath() {
        assertEquals("neo4jpath", config.getNeo4jPath());
    }

    @Test
    public void testGetTransactionMode() {
        assertEquals(TransactionMode.SINGLE, config.getTransactionMode());
    }

    @Test
    public void testGetDetails() {
        Set<String> details = new HashSet<String>();
        details.add("detail1");
        details.add("detail2");
        assertEquals(details, config.getDetails());
    }

    @Test
    public void testIsDetail() {
        assertTrue(config.isDetail("detail1"));
        assertTrue(config.isDetail("detail2"));
        assertFalse(config.isDetail("detail3"));
    }

    @Test
    public void getNodeTypes() {
        Set<String> nodeTypes = new HashSet<String>();
        nodeTypes.add("Node1");
        nodeTypes.add("Node2");
        nodeTypes.add("Node3");
        assertEquals(nodeTypes, config.getNodeTypes());
    }

    @Test
    public void testIsNodeType() {
        assertTrue(config.isNodeType("Node1"));
        assertTrue(config.isNodeType("Node2"));
        assertTrue(config.isNodeType("Node3"));
        assertFalse(config.isNodeType("Node4"));
    }

    @Test
    public void testGetNode() {
        assertNotNull(config.getNode("Node1"));
        assertNotNull(config.getNode("Node2"));
        assertNotNull(config.getNode("Node3"));
        assertNull(config.getNode("Node4"));
    }

    @Test
    public void testGetProperties() {
        ConfigNode node1 = config.getNode("Node1");
        Set<String> node1Properties = new HashSet<String>();
        node1Properties.add("property1");
        node1Properties.add("property2");
        assertEquals(node1Properties, node1.getProperties());

        ConfigNode node2 = config.getNode("Node2");
        Set<String> node2Properties = new HashSet<String>();
        node2Properties.add("property1");
        assertEquals(node2Properties, node2.getProperties());

        ConfigNode node3 = config.getNode("Node3");
        Set<String> node3Properties = new HashSet<String>();
        assertEquals(node3Properties, node3.getProperties());
    }

    @Test
    public void testIsProperty() {
        ConfigNode node1 = config.getNode("Node1");
        assertTrue(node1.isProperty("property1"));
        assertTrue(node1.isProperty("property2"));
        assertFalse(node1.isProperty("property3"));
        ConfigNode node2 = config.getNode("Node2");
        assertTrue(node1.isProperty("property1"));
        assertFalse(node2.isProperty("property2"));
        ConfigNode node3 = config.getNode("Node3");
        assertFalse(node3.isProperty("property1"));
    }

    @Test
    public void testGetPropertyType() {
        ConfigNode node1 = config.getNode("Node1");
        assertEquals("String", node1.getPropertyType("property1"));
        assertEquals("String", node1.getPropertyType("property2"));
        assertNull(node1.getPropertyType("property3"));
        ConfigNode node2 = config.getNode("Node2");
        assertEquals("String", node2.getPropertyType("property1"));
        assertNull(node2.getPropertyType("property2"));
        ConfigNode node3 = config.getNode("Node3");
        assertNull(node3.getPropertyType("property1"));
    }

    @Test
    public void testGetRelations() {
        ConfigNode node1 = config.getNode("Node1");
        Set<String> node1Relations = new HashSet<String>();
        node1Relations.add("relation1");
        node1Relations.add("relation2");
        assertEquals(node1Relations, node1.getRelations());

        ConfigNode node2 = config.getNode("Node2");
        Set<String> node2Relations = new HashSet<String>();
        node2Relations.add("relation1");
        assertEquals(node2Relations, node2.getRelations());

        ConfigNode node3 = config.getNode("Node3");
        Set<String> node3Relations = new HashSet<String>();
        assertEquals(node3Relations, node3.getRelations());
    }

    @Test
    public void testIsRelation() {
        ConfigNode node1 = config.getNode("Node1");
        assertTrue(node1.isRelation("relation1"));
        assertTrue(node1.isRelation("relation2"));
        assertFalse(node1.isRelation("relation3"));
        ConfigNode node2 = config.getNode("Node2");
        assertTrue(node1.isRelation("relation1"));
        assertFalse(node2.isRelation("relation2"));
        ConfigNode node3 = config.getNode("Node3");
        assertFalse(node3.isRelation("relation1"));
    }

    @Test
    public void testGetRelationType() {
        RelationType relNode1Arr = new RelationType("Node1[]");
        RelationType relNode2Arr = new RelationType("Node2[]");
        RelationType relNode3 = new RelationType("Node3");

        ConfigNode node1 = config.getNode("Node1");
        assertEquals(relNode2Arr, node1.getRelationType("relation1"));
        assertEquals(relNode3, node1.getRelationType("relation2"));
        assertNull(node1.getRelationType("relation3"));
        ConfigNode node2 = config.getNode("Node2");
        assertEquals(relNode1Arr, node2.getRelationType("relation1"));
        assertNull(node2.getRelationType("relation2"));
        ConfigNode node3 = config.getNode("Node3");
        assertNull(node3.getRelationType("relation1"));
    }

    @Test
    public void testValidate() throws InvalidConfigException {
        config.validate();
    }

    // TODO: test ConfigNodeOutput
}
