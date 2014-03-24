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
        // TODO Auto-generated method stub
    }

    @Override
    public boolean equals(Object other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

}
