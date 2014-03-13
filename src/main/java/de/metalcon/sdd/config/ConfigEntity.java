package de.metalcon.sdd.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.sdd.exception.InvalidAttrException;
import de.metalcon.sdd.exception.InvalidConfigException;

public class ConfigEntity {

    private Map<String, ConfigType> attrs;

    private Map<String, ConfigEntityOutput> outputs;

    public ConfigEntity() {
        attrs = new HashMap<String, ConfigType>();
        outputs = new HashMap<String, ConfigEntityOutput>();
    }

    public Set<String> getAttrs() {
        return Collections.unmodifiableSet(attrs.keySet());
    }

    public ConfigType getAttr(String name) throws InvalidAttrException {
        ConfigType attr = attrs.get(name);
        if (attr == null) {
            throw new InvalidAttrException("No attr with that name: \"" + name
                    + "\".");
        }
        return attr;
    }

    public boolean isValidAttr(String name) {
        return attrs.containsKey(name);
    }

    public void addAttr(String name, String type) throws InvalidConfigException {
        addAttr(name, new ConfigType(type));
    }

    public void addAttr(String name, ConfigType type)
            throws InvalidConfigException {
        if (name.equals("id") || name.equals("type")
                || name.startsWith("json-")) {
            throw new InvalidConfigException("invalid attr type name \""
                    + type.getType() + "\"");
        }
        attrs.put(name, type);
    }

    public Set<String> getOutputs() {
        return Collections.unmodifiableSet(outputs.keySet());
    }

    public ConfigEntityOutput getOutput(String detail) {
        ConfigEntityOutput output = outputs.get(detail);
        if (output == null) {
            throw new RuntimeException();
        }
        return output;
    }

    public void addOutput(String detail, ConfigEntityOutput output) {
        outputs.put(detail, output);
    }

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
