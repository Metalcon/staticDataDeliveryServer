package de.metalcon.sdd.testBandRecordUser;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;

public class Read {

    private Sdd sdd;

    @Before
    public void setUp() throws IOException {
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
    @Ignore
    public void read() {
        long t1 = System.currentTimeMillis();
        System.out.println(sdd.read(Parse.BAND_ID_PREFIX + 1L, "nested"));
        long t2 = System.currentTimeMillis();
        System.out.println("read: " + (t2 - t1) + " ms");
    }

}
