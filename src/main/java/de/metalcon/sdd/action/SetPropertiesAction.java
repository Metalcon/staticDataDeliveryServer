package de.metalcon.sdd.action;

import java.util.Map;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.SddException;

public class SetPropertiesAction extends Action {

    private long nodeId;

    private String nodeType;

    private Map<String, String> properties;

    public SetPropertiesAction(
            long nodeId,
            String nodeType,
            Map<String, String> properties) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.properties = properties;
    }

    @Override
    public void runAction(Sdd sdd) throws SddException {
        sdd.actionSetProperties(nodeId, nodeType, properties);
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
