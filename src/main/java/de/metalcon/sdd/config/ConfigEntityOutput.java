package de.metalcon.sdd.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Helper class that stores config for one output of an entity.
 */
public class ConfigEntityOutput {

    /**
     * Map from output attribute name to output attribute detail.
     */
    private Map<String, String> oattrs;

    /**
     * Constructs an empty entity output.
     */
    public ConfigEntityOutput() {
        oattrs = new HashMap<String, String>();
    }

    /**
     * @return A set of the name of all output attributes.
     */
    public Set<String> getOattrs() {
        return Collections.unmodifiableSet(oattrs.keySet());
    }

    /**
     * Get a specific output attribute by name from this output.
     * 
     * @param attr
     *            The output attribute's name.
     * @return The requested output attribute, or <code>null</code> if no output
     *         attribute with that name exists.
     */
    public String getOattr(String attr) {
        return oattrs.get(attr);
    }

    /**
     * Adds an output attribute to the output.
     * 
     * @param attr
     *            The output attribute's name.
     * @param detail
     *            The output attribute's detail.
     */
    public void addOattr(String attr, String detail) {
        oattrs.put(attr, detail);
    }

}
