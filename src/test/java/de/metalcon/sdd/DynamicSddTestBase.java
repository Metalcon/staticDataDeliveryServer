package de.metalcon.sdd;

import java.io.IOException;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;

public class DynamicSddTestBase {

    public static Config config = null;

    public static Sdd createSdd() throws IOException {
        if (config == null) {
            config = new XmlConfig("src/test/resources/testConfig.xml");
        }
        config.makeTemporary();
        return new Sdd(config);
    }

    public static void closeSdd(Sdd sdd) throws IOException {
        sdd.waitUntilQueueEmpty();
        sdd.close();
    }

}
