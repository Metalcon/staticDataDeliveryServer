package de.metalcon.sdd;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;
import de.metalcon.sdd.exception.InvalidConfigException;

public class SddTest {

    private Config config;

    private Sdd sdd;

    @Before
    public void setUp() throws InvalidConfigException, SAXException,
            IOException, ParserConfigurationException {
        config = new XmlConfig("src/test/resources/testConfig.xml");
        config.makeTemporary();

        sdd = new Sdd(config);
    }

    @After
    public void tearDown() throws IOException {
        sdd.close();
    }

    @Test
    public void testSdd() {
    }

}
