package de.metalcon.sdd.testBandRecordUser;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.metalcon.sdd.api.exception.InvalidConfigException;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;

public class ConfigTest {

    @Test
    public void testConfig() throws InvalidConfigException, SAXException,
            IOException, ParserConfigurationException {
        Config config =
                new XmlConfig(
                        "src/test/resources/testBandRecordUser/config.xml");
        config.validate();
    }

}
