package de.metalcon.sdd.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.api.exception.InvalidConfigException;

public class ConfigNode {

    private Map<String, String> properties = new HashMap<String, String>();

    private Map<String, RelationType> relations =
            new HashMap<String, RelationType>();

    private Map<String, ConfigNodeOutput> outputs =
            new HashMap<String, ConfigNodeOutput>();

    /* package */ConfigNode() {
    }

    public Set<String> getProperties() {
        return Collections.unmodifiableSet(properties.keySet());
    }

    public boolean isProperty(String property) {
        if (property == null) {
            throw new IllegalArgumentException("property was null.");
        }
        return properties.containsKey(property);
    }

    /**
     * @return The type of that property or <code>NULL</code> if that property
     *         is not configured.
     */
    public String getPropertyType(String property) {
        if (property == null) {
            throw new IllegalArgumentException("property was null.");
        }
        return properties.get(property);
    }

    public void addProperty(String property, String propertyType) {
        if (property == null) {
            throw new IllegalArgumentException("property was null.");
        }
        if (propertyType == null) {
            throw new IllegalArgumentException("propertyType was null.");
        }
        checkName(property);
        if (isProperty(property)) {
            throw new InvalidConfigException(
                    "Duplicate property definition: \"" + property + "\".");
        }
        if (!propertyType.equals("String")) {
            throw new InvalidConfigException("Invalid property type: \""
                    + propertyType + "\". "
                    + "Only valid property type is \"String\".");
        }
        properties.put(property, propertyType);
    }

    public Set<String> getRelations() {
        return Collections.unmodifiableSet(relations.keySet());
    }

    public boolean isRelation(String relation) {
        if (relation == null) {
            throw new IllegalArgumentException("relation was null.");
        }
        return relations.containsKey(relation);
    }

    /**
     * @return The type of that relation or <code>NULL</code> if that relation
     *         is not configured.
     */
    public RelationType getRelationType(String relation) {
        if (relation == null) {
            throw new IllegalArgumentException("relation was null.");
        }
        return relations.get(relation);
    }

    public void addRelation(String relation, RelationType relationType) {
        if (relation == null) {
            throw new IllegalArgumentException("relation was null.");
        }
        if (relationType == null) {
            throw new IllegalArgumentException("relationType was null.");
        }
        checkName(relation);
        if (isRelation(relation)) {
            throw new InvalidConfigException(
                    "Duplicate relation definition: \"" + relation + "\".");
        }
        relations.put(relation, relationType);
    }

    public Set<String> getOutputDetails() {
        return Collections.unmodifiableSet(outputs.keySet());
    }

    /**
     * @return The node's output for that detail or <code>NULL</code> if the
     *         node doesn't have output for that detail configured.
     */
    public ConfigNodeOutput getOutput(String detail) {
        if (detail == null) {
            throw new IllegalArgumentException("detail was null.");
        }
        return outputs.get(detail);
    }

    public void addOutput(String detail, ConfigNodeOutput output) {
        if (detail == null) {
            throw new IllegalArgumentException("detail was null.");
        }
        if (output == null) {
            throw new IllegalArgumentException("output was null.");
        }

        if (outputs.containsKey(detail)) {
            throw new InvalidConfigException("Duplicate detail definition: \""
                    + detail + "\".");
        }

        outputs.put(detail, output);
    }

    public boolean dependsOn(
            String modifiedNodeType,
            Set<String> modifiedDetails) {
        for (ConfigNodeOutput output : outputs.values()) {
            for (String outRelation : output.getOutRelations()) {
                String outRelationDetail =
                        output.getOutRelationDetail(outRelation);
                RelationType relationType = relations.get(outRelation);
                if (modifiedNodeType.equals(relationType.getType())
                        && modifiedDetails.contains(outRelationDetail)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkName(String name) {
        if (name.equals(Sdd.NODEDB_ID) || name.equals(Sdd.NODEDB_TYPE)
                || name.startsWith(Sdd.NODEDB_OUTPUT_PREFIX)) {
            throw new InvalidConfigException("Invalid name: \"" + name + "\". "
                    + "It must not be \"" + Sdd.NODEDB_ID + "\", \""
                    + Sdd.NODEDB_TYPE + "\" or start with \""
                    + Sdd.NODEDB_OUTPUT_PREFIX + "\".");
        }
    }

}
