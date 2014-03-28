package de.metalcon.sdd;

import java.util.Map;
import java.util.Queue;

import de.metalcon.sdd.config.ConfigNode;

public class SetPropertiesAction extends Action {

    private long nodeId;

    private String nodeType;

    private Map<String, String> properties;

    /* package */SetPropertiesAction(
            Sdd sdd,
            long nodeId,
            String nodeType,
            Map<String, String> properties) {
        super(sdd);

        if (nodeType == null) {
            throw new IllegalArgumentException("nodeType was null.");
        }
        if (properties == null) {
            throw new IllegalArgumentException("properties was null.");
        }

        if (nodeId == Sdd.EMPTY_ID) {
            throw ExceptionFactory.createEmptyIdException();
        }
        if (!config.isNodeType(nodeType)) {
            throw ExceptionFactory.createInvalidNodeTypeException();
        }
        ConfigNode configNode = config.getNode(nodeType);
        if (properties.size() == 0) {
            throw ExceptionFactory
                    .createInvalidPropertyException("Properties was empty.");
        }
        for (String property : properties.keySet()) {
            if (!configNode.isProperty(property)) {
                throw ExceptionFactory
                        .createInvalidPropertyException("Property not defined in Configuration.");
            }
        }

        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.properties = properties;
    }

    @Override
    public void runAction(Queue<Action> actions) {
        sdd.actionSetProperties(actions, nodeId, nodeType, properties);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // we define that two distinct SetPropertiesActions are never equal
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 54237;
        int mult = 613;

        hash = hash * mult + ((Long) nodeId).hashCode();
        hash = hash * mult + nodeType.hashCode();
        hash = hash * mult + properties.hashCode();

        return hash;
    }
}
