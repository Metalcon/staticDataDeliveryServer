package de.metalcon.sdd.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.sdd.exception.InvalidAttrException;
import de.metalcon.sdd.exception.InvalidConfigException;

/**
 * Helper class that stores config for one Entity tag.
 */
public class ConfigEntity {

    /**
     * Map from attribute name to attribute type.
     */
    private Map<String, ConfigType> attrs;

    /**
     * Map from detail name to output detail config.
     */
    private Map<String, ConfigEntityOutput> outputs;

    /**
     * Constructs empty entity config.
     */
    public ConfigEntity() {
        attrs = new HashMap<String, ConfigType>();
        outputs = new HashMap<String, ConfigEntityOutput>();
    }

    /**
     * @return Set of all attribute names of this entity.
     */
    public Set<String> getAttrs() {
        return Collections.unmodifiableSet(attrs.keySet());
    }

    /**
     * Get the ConfigType for a specific attribute.
     * 
     * @param name
     *            The attribute's name.
     * @return The ConfigType for that attribute.
     * @throws InvalidAttrException
     *             In case no attribute exists with that name.
     */
    public ConfigType getAttr(String name) throws InvalidAttrException {
        ConfigType attr = attrs.get(name);
        if (attr == null) {
            throw new InvalidAttrException("No attr with that name: \"" + name
                    + "\".");
        }
        return attr;
    }

    /**
     * Check whether a specific attribute exists for this entity.
     * 
     * @param name
     *            The attribute's name.
     * @return <code>True</code> if that attribute exists, <code>false</code> if
     *         not.
     */
    public boolean isValidAttr(String name) {
        return attrs.containsKey(name);
    }

    /**
     * Adds an attribute to this entity.
     * 
     * This is just a convenience function for
     * <code>addAttr(String name, ConfigType type)</code>, the type attribute
     * will simple be used as constructor argument to ConfigType.
     * 
     * @param name
     *            The attribute's name.
     * @param type
     *            String describing a ConfigType.
     * @throws InvalidConfigException
     *             If an invalid attribute name is passed.
     */
    public void addAttr(String name, String type) throws InvalidConfigException {
        addAttr(name, new ConfigType(type));
    }

    /**
     * Adds an attribute to this entity.
     * 
     * @param name
     *            The attribute's name.
     * @param type
     *            The attribute's ConfigType.
     * @throws InvalidConfigException
     *             If an invalid attribute name is passed.
     */
    public void addAttr(String name, ConfigType type)
            throws InvalidConfigException {
        if (name.equals("id") || name.equals("type")
                || name.startsWith("json-")) {
            throw new InvalidConfigException("invalid attr type name \""
                    + type.getType() + "\"");
        }
        attrs.put(name, type);
    }

    /**
     * @return Set of all outputs of this entity.
     */
    public Set<String> getOutputs() {
        return Collections.unmodifiableSet(outputs.keySet());
    }

    /**
     * Get the ConfigEntityOutput for a specific output.
     * 
     * @param detail
     *            The output's detail level.
     * @return The ConfigEntityOutput for that output.
     */
    public ConfigEntityOutput getOutput(String detail) {
        ConfigEntityOutput output = outputs.get(detail);
        if (output == null) {
            throw new RuntimeException();
        }
        return output;
    }

    /**
     * Adds an output to the entity.
     * 
     * @param detail
     *            The output's detail level.
     * @param output
     *            The ConfigEntityOutput for that output.
     */
    public void addOutput(String detail, ConfigEntityOutput output) {
        outputs.put(detail, output);
    }

    /**
     * Checks whether any output of this entity depends on a given combination
     * of entity type and set of details. This means to check if if any
     * attribute that is referenced in any output has a type that is updated
     * in the entities output with any of the details specified.
     * 
     * This method is used to check whether an entity has to be updated after
     * one its referencing entities has been updated.
     * 
     * @param type
     *            The entity type.
     * @param modifiedDetails
     *            Set of details.
     * @return <code>true</code> if any output of the entity depends on that
     *         type and detail combination, <code>false</code> if not.
     */
    public boolean dependsOn(String type, Set<String> modifiedDetails) {
        for (ConfigEntityOutput output : outputs.values()) {
            for (String oattrName : output.getOattrs()) {
                String oattrDetail = output.getOattr(oattrName);
                ConfigType oattrType = attrs.get(oattrName);
                if (type.equals(oattrType.getType())
                        && modifiedDetails.contains(oattrDetail)) {
                    return true;
                }
            }
        }
        return false;
    }

}
