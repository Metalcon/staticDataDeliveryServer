package de.metalcon.sdd.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Config implementation where directories are located in a temporary location.
 * 
 * Primary use of this class is for testing.
 * 
 * Temporary directories are not deleted on shutdown. You will have to do that
 * manually. Most likely the directory will be located in <code>/tmp</code>.
 */
public class TempConfig {

    /**
     * Constructs a new empty Configuration with only LevelDbPath and Neo4jPath
     * set to temporary directories.
     * 
     * @throws IOException
     *             If no temporary directory could be created.
     */

    public static void makeConfigTemporary(Config config) throws IOException {
        Path tmpDir = Files.createTempDirectory("sddTest");

        Path leveldbPath = tmpDir.resolve("leveldb");
        leveldbPath.toFile().mkdir();
        config.setLeveldbPath(leveldbPath.toString());

        Path neo4jPath = tmpDir.resolve("neo4j");
        neo4jPath.toFile().mkdir();
        config.setNeo4jPath(neo4jPath.toString());
    }

}
