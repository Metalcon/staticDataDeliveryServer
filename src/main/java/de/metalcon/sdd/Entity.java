package de.metalcon.sdd;

import java.util.Map;

import org.neo4j.graphdb.Node;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.ConfigEntity;
import de.metalcon.sdd.error.InconsitentTypeException;
import de.metalcon.sdd.error.InvalidTypeException;

public class Entity {

    private Config config;

    private long id;

    private String type;

    private ConfigEntity configEntity;

    private Node node;

    public Entity(
            Config config,
            long id,
            String type) throws InvalidTypeException {
        if (config == null) {
            throw new IllegalArgumentException("config was null");
        }
        if (type == null) {
            throw new IllegalArgumentException("type was null");
        }
        if (!config.isValidEntityType(type)) {
            throw new InvalidTypeException();
        }

        this.config = config;
        this.id = id;
        this.type = type;
        configEntity = null;
        node = null;
    }

    public Entity(
            Config config,
            Node node) throws InconsitentTypeException, InvalidTypeException {
        if (config == null) {
            throw new IllegalArgumentException("config was null");
        }
        if (node == null) {
            throw new IllegalArgumentException("node was null");
        }

        Long id = (Long) node.getProperty("id", null);
        if (id == null) {
            // TODO: create good exception
            throw new RuntimeException();
        }

        String type = (String) node.getProperty("type", null);
        if (type == null) {
            // TODO: create good exception
            throw new RuntimeException();
        }
        if (!config.isValidEntityType(type)) {
            throw new InvalidTypeException();
        }

        this.config = config;
        this.id = id;
        this.type = type;
        configEntity = null;
        setNode(node);
    }

    public long getId() {
        return id;
    }

    public String getType() {
        if (type == null) {
            // TODO: create good exception
            throw new RuntimeException();
        }
        return type;
    }

    public ConfigEntity getConfigEntity() throws InvalidTypeException {
        if (configEntity == null) {
            configEntity = config.getEntity(getType());
            if (configEntity == null) {
                throw new InvalidTypeException();
            }
        }
        return configEntity;
    }

    public void setNode(Node node) throws InconsitentTypeException {
        if (node == null) {
            throw new RuntimeException();
        }
        if (!type.equals(node.getProperty("type", null))) {
            throw new InconsitentTypeException();
        }

        this.node = node;
    }

    public Node getNode() {
        if (node == null) {
            // TODO: create good exception
            throw new RuntimeException();
        }
        return node;
    }

    public void setAttrs(Map<String, String> attrs) {
        for (Map.Entry<String, String> attr : attrs.entrySet()) {
            String attrName = attr.getKey();
            String attrValue = attr.getValue();
            node.setProperty(attrName, attrValue);
        }
    }

}
