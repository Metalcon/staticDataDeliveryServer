package de.metalcon.sdd;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;
import de.metalcon.sdd.exception.InvalidConfigException;

public abstract class StaticSddTestBase {

    protected static Sdd sdd;

    @BeforeClass
    public static void setUpStatic() throws InvalidConfigException, IOException {
        Config config = new XmlConfig("src/test/resources/testConfig.xml");
        config.makeTemporary();
        sdd = new Sdd(config);
    }

    @AfterClass
    public static void tearDownStatic() throws IOException {
        sdd.waitUntilQueueEmpty();
        sdd.close();
    }

}
