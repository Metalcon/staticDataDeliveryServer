package de.metalcon.sdd;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;
import de.metalcon.sdd.exception.InvalidConfigException;
import de.metalcon.sdd.exception.InvalidDetailException;

public class SddTest {

    private Config config;

    private Sdd sdd;

    @Before
    public void setUp() throws InvalidConfigException, IOException,
            InvalidDetailException {
        config = new XmlConfig("src/test/resources/testConfig.xml");
        config.makeTemporary();

        sdd = new Sdd(config);

        System.out.println(sdd.read(32L, "detail1"));
    }

    @After
    public void tearDown() throws IOException {
        sdd.close();
    }

    @Test
    public void testSdd() {
    }

}
