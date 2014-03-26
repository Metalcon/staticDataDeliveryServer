package de.metalcon.sdd.testBandRecordUser;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;
import de.metalcon.sdd.exception.InvalidConfigException;
import de.metalcon.sdd.exception.InvalidDetailException;

public class Read {

    private Sdd sdd;

    @Before
    public void setUp() throws InvalidConfigException, IOException {
        Config config =
                new XmlConfig(
                        "src/test/resources/testBandRecordUser/config.xml");

        sdd = new Sdd(config);
    }

    @After
    public void tearDown() throws IOException {
        sdd.close();
    }

    @Test
    public void read() throws InvalidDetailException, FileNotFoundException {
        long t1 = System.currentTimeMillis();
        System.out.println(sdd.read(Parse.BAND_ID_PREFIX + 1L, "nested"));
        long t2 = System.currentTimeMillis();
        System.out.println("read: " + (t2 - t1) + " ms");
    }

}
