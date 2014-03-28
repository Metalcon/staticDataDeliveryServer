package de.metalcon.sdd.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.metalcon.sdd.api.exception.InvalidConfigException;

public class ConfigNodeOutput {

    private Set<String> outProperties = new HashSet<String>();

    private Map<String, String> outRelations = new HashMap<String, String>();

    /* package */ConfigNodeOutput() {
    }

    public Set<String> getOutProperties() {
        return Collections.unmodifiableSet(outProperties);
    }

    public void addOutPropery(String outProperty) {
        if (outProperty == null) {
            throw new IllegalArgumentException("outProperty was null.");
        }
        if (outProperties.contains(outProperty)) {
            throw new InvalidConfigException(
                    "Duplicate out-property definition \"" + outProperty
                            + "\".");
        }
        outProperties.add(outProperty);
    }

    public Set<String> getOutRelations() {
        return Collections.unmodifiableSet(outRelations.keySet());
    }

    /**
     * @return The detail of the node's output for that relation or
     *         <code>NULL</code> if the node's output doesn't have that
     *         relation.
     */
    public String getOutRelationDetail(String outRelation) {
        if (outRelation == null) {
            throw new IllegalArgumentException("outRelation was null.");
        }
        return outRelations.get(outRelation);
    }

    public void addOutRelation(String outRelation, String outRelationDetail) {
        if (outRelation == null) {
            throw new IllegalArgumentException("outRelation was null.");
        }
        if (outRelationDetail == null) {
            throw new IllegalArgumentException("outRelationDetail was null.");
        }
        if (outRelations.containsKey(outRelation)) {
            throw new InvalidConfigException(
                    "Duplicate out-relation definition \"" + outRelation
                            + "\".");
        }
        outRelations.put(outRelation, outRelationDetail);
    }
}
