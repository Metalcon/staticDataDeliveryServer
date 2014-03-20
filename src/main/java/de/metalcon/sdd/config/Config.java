package de.metalcon.sdd.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.metalcon.sdd.exception.InvalidAttrException;
import de.metalcon.sdd.exception.InvalidConfigException;

/**
 * Class that stores all configuration values for SDD.
 */
public class Config {

    /**
     * Path where LevelDB data is stored.
     */
    private String leveldbPath;

    /**
     * Path were Neo4j data is stored.
     */
    private String neo4jPath;

    /**
     * The string used to separate multiple IDs from each other.
     * Defaults to <code>,</code>.
     */
    private String idDelimiter = ",";

    /**
     * The string used in <code>id:detail</code> pairs used to separate id from
     * detail.
     * Defaults to <code>:</code>.
     */
    private String idDetailDelimiter = ":";

    /**
     * Set of all possible Detail values.
     */
    private Set<String> details;

    /**
     * Maps Entity type to ConfigEntity instance.
     */
    private Map<String, ConfigEntity> entities;

    /**
     * Constructs an empty config.
     */
    public Config() {
        leveldbPath = null;
        neo4jPath = null;
        details = new HashSet<String>();
        entities = new HashMap<String, ConfigEntity>();
    }

    /**
     * @param leveldbPath
     *            Path were LevelDB data is stored.
     */
    public void setLeveldbPath(String leveldbPath) {
        if (leveldbPath == null) {
            throw new IllegalArgumentException("leveldbPath was null");
        }
        this.leveldbPath = leveldbPath;
    }

    /**
     * @return Path were LevelDB data is stored.
     * @throws InvalidConfigException
     *             If no path has been set yet.
     */
    public String getLeveldbPath() throws InvalidConfigException {
        if (leveldbPath == null) {
            throw new InvalidConfigException("leveldb path not set.");
        }
        return leveldbPath;
    }

    /**
     * @param neo4jPath
     *            Path were Neo4j data is stored.
     */
    public void setNeo4jPath(String neo4jPath) {
        if (neo4jPath == null) {
            throw new IllegalArgumentException("neo4jPath was null");
        }
        this.neo4jPath = neo4jPath;
    }

    /**
     * @return Path were Neo4j data is stored.
     * @throws InvalidConfigException
     *             If no path has been set yet.
     */
    public String getNeo4jPath() throws InvalidConfigException {
        if (neo4jPath == null) {
            throw new InvalidConfigException("neo4j path is not set.");
        }
        return neo4jPath;
    }

    /**
     * @return The string used to separate multiple IDs from each other.
     *         Defaults to ",".
     */
    public String getIdDelimiter() {
        return idDelimiter;
    }

    /**
     * @return The string used in <code>id:detail</code> pairs used to separate
     *         id from
     *         detail. Defaults to <code>:</code>.
     */
    public String getIdDetailDelimiter() {
        return idDetailDelimiter;
    }

    /**
     * @return Set of all possible Detail values.
     */
    public Set<String> getDetails() {
        return Collections.unmodifiableSet(details);
    }

    /**
     * Checks whether a given detail is set in config.
     * 
     * @param detail
     *            The detail to be checked.
     * @return <code>True</code> if detail is set in config. <code>False</code>
     *         otherwise.
     */
    public boolean isValidDetail(String detail) {
        return details.contains(detail);
    }

    /**
     * Adds a details to the list of all Details.
     * 
     * @param detail
     *            The detail to be added.
     * @see details
     * @see getDetails
     */
    public void addDetail(String detail) {
        details.add(detail);
    }

    /**
     * @return Set of all Entity types set in config.
     */
    public Set<String> getEntities() {
        return Collections.unmodifiableSet(entities.keySet());
    }

    /**
     * @return Returns the ConfigEntity for type or Null if no entity with that
     *         type was configured.
     */
    public ConfigEntity getEntity(String type) {
        return entities.get(type);
    }

    /**
     * Checks whether a given Entity type is set in config.
     * 
     * @param type
     *            The type to be checked.
     * @return <code>True</code> if Entity type is set in config.
     *         <code>False</code> otherwise.
     */
    public boolean isValidEntityType(String type) {
        return entities.containsKey(type);
    }

    /**
     * Adds a ConfigEntity to the config.
     * 
     * @param type
     *            The type of the entity to be added.
     * @param entity
     *            The ConfigEntity instance to be added.
     */
    public void addEntity(String type, ConfigEntity entity) {
        entities.put(type, entity);
    }

    /**
     * Checks if the config is a valid SDD config.
     * 
     * Remains silent if valid, throws on error.
     * 
     * These cases are checked:
     * <ul>
     * <li>Do all Entity attributes have a valid name? (must not start with
     * <code>id</code>, <code>type</code> or <code>json-</code>)</li>
     * <li>Do all non primitive Entity attributes reference existing Entity
     * types?</li>
     * <li>Are all defined output details for entities actually valid details?</li>
     * <li>Do all primitive output attributes have no output detail set?</li>
     * <li>Do all non primitive output attribute define a valid output detail?</li>
     * </ul>
     * 
     * @throws InvalidConfigException
     *             In case the config is invalid.
     */
    public void validate() throws InvalidConfigException {
        // Check paths, these methods throw on their own.
        getLeveldbPath();
        getNeo4jPath();

        // Check entities
        for (String type : getEntities()) {
            ConfigEntity entity = getEntity(type);

            // Check attributes
            for (String attrName : entity.getAttrs()) {
                ConfigType attrType;
                try {
                    attrType = entity.getAttr(attrName);
                } catch (InvalidAttrException e) {
                    // This state should not be reachable.
                    throw new IllegalStateException();
                }

                if (attrType.getType().equals("id")
                        || attrType.getType().equals("type")
                        || attrType.getType().startsWith("json-")) {
                    throw new InvalidConfigException(
                            "invalid attr type name \"" + attrType.getType()
                                    + "\"");
                }

                if (!attrType.isPrimitive()
                        && !isValidEntityType(attrType.getType())) {
                    throw new InvalidConfigException("invalid attr type \""
                            + attrType.getType() + "\" for attr \"" + attrName
                            + "\" on entity type \"" + type + "\"");
                }
            }

            // Check outputs
            for (String outputDetail : entity.getOutputs()) {
                ConfigEntityOutput output = entity.getOutput(outputDetail);

                // Check output detail
                if (!isValidDetail(outputDetail)) {
                    throw new InvalidConfigException("invalid output detail \""
                            + outputDetail + "\" on entity type \"" + type
                            + "\"");
                }

                // Check output attributes
                for (String oattr : output.getOattrs()) {
                    String oattrDetail = output.getOattr(oattr);

                    ConfigType oattrType;
                    try {
                        oattrType = entity.getAttr(oattr);
                    } catch (InvalidAttrException e) {
                        throw new InvalidConfigException(
                                "oattr for invalid attr \"" + oattr
                                        + "\" on entity type \"" + type + "\"",
                                e);
                    }

                    if (oattrType.isPrimitive()) {
                        if (!oattrDetail.equals("")) {
                            throw new InvalidConfigException(
                                    "oattr for primitive attr \"" + oattr
                                            + "\" has detail on entity type \""
                                            + type + "\"");
                        }
                    } else if (!isValidDetail(oattrDetail)) {
                        throw new InvalidConfigException(
                                "oattr for non primitive attr \"" + oattr
                                        + "\" has no detail on entity type \""
                                        + type + "\"");
                    }
                }
            }
        }
    }

}
