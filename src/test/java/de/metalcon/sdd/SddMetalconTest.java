package de.metalcon.sdd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.ConfigEntity;
import de.metalcon.sdd.config.ConfigEntityOutput;
import de.metalcon.sdd.config.TempConfig;
import de.metalcon.sdd.exception.InvalidAttrException;
import de.metalcon.sdd.exception.InvalidConfigException;
import de.metalcon.sdd.exception.InvalidDetailException;
import de.metalcon.sdd.exception.InvalidTypeException;
import de.metalcon.utils.jsonPrettyPrinter.JsonPrettyPrinter;

public class SddMetalconTest {

    private Sdd sdd;

    @Before
    public void setUp() throws IOException, InvalidConfigException {
        Config config = new TempConfig();

        config.addDetail("page");
        config.addDetail("big");
        config.addDetail("small");

        ConfigEntity band = new ConfigEntity();
        band.addAttr("name", "String");
        band.addAttr("records", "Record[]");
        ConfigEntityOutput band_page = new ConfigEntityOutput();
        band_page.addOattr("name", "");
        band_page.addOattr("records", "big");
        band.addOutput("page", band_page);
        ConfigEntityOutput band_big = new ConfigEntityOutput();
        band_big.addOattr("name", "");
        band_big.addOattr("records", "small");
        band.addOutput("big", band_big);
        ConfigEntityOutput band_small = new ConfigEntityOutput();
        band_small.addOattr("name", "");
        band.addOutput("small", band_small);
        config.addEntity("Band", band);

        ConfigEntity record = new ConfigEntity();
        record.addAttr("name", "String");
        record.addAttr("band", "Band");
        record.addAttr("tracks", "Track[]");
        ConfigEntityOutput record_page = new ConfigEntityOutput();
        record_page.addOattr("name", "");
        record_page.addOattr("band", "big");
        record_page.addOattr("tracks", "big");
        record.addOutput("page", record_page);
        ConfigEntityOutput record_big = new ConfigEntityOutput();
        record_big.addOattr("name", "");
        record_big.addOattr("band", "small");
        record_big.addOattr("tracks", "small");
        record.addOutput("big", record_big);
        ConfigEntityOutput record_small = new ConfigEntityOutput();
        record_small.addOattr("name", "");
        record.addOutput("small", record_small);
        config.addEntity("Record", record);

        ConfigEntity track = new ConfigEntity();
        track.addAttr("name", "String");
        track.addAttr("band", "Band");
        track.addAttr("record", "Record");
        ConfigEntityOutput track_page = new ConfigEntityOutput();
        track_page.addOattr("name", "");
        track_page.addOattr("band", "big");
        track_page.addOattr("record", "big");
        track.addOutput("page", track_page);
        ConfigEntityOutput track_big = new ConfigEntityOutput();
        track_big.addOattr("name", "");
        track_big.addOattr("band", "small");
        track_big.addOattr("record", "small");
        track.addOutput("big", track_big);
        ConfigEntityOutput track_small = new ConfigEntityOutput();
        track_small.addOattr("name", "");
        track.addOutput("small", track_small);
        config.addEntity("Track", track);

        //        config = new FileConfig(Paths.get("/usr/share/sdd/config.xml"));

        sdd = new Sdd(config);
    }

    @After
    public void tearDown() throws IOException {
        sdd.close();
    }

    @Test
    public void testExampleSetUp() throws InvalidTypeException,
            InvalidAttrException, IOException, InvalidDetailException {
        Map<String, String> ensiferum = new HashMap<String, String>();
        ensiferum.put("name", "Ensiferum");
        Map<String, String> iron = new HashMap<String, String>();
        iron.put("name", "Iron");
        Map<String, String> victorySongs = new HashMap<String, String>();
        victorySongs.put("name", "Victory Songs");
        Map<String, String> intoBattle = new HashMap<String, String>();
        intoBattle.put("name", "Into Battle");
        Map<String, String> laiLaiHei = new HashMap<String, String>();
        laiLaiHei.put("name", "Lai Lai Hei");
        Map<String, String> ahti = new HashMap<String, String>();
        ahti.put("name", "Ahti");

        long t1 = System.currentTimeMillis();

        sdd.updateEntityAttrs(31L, "Band", ensiferum);
        sdd.updateEntityAttrs(32L, "Record", iron);
        sdd.updateEntityAttrs(33L, "Record", victorySongs);
        sdd.updateEntityAttrs(34L, "Track", intoBattle);
        sdd.updateEntityAttrs(35L, "Track", laiLaiHei);
        sdd.updateEntityAttrs(36L, "Track", ahti);

        sdd.updateEntityRel(31L, "Band", "records", new long[] {
            32L, 33L
        });
        sdd.updateEntityRel(32L, "Record", "band", 31L);
        sdd.updateEntityRel(32L, "Record", "tracks", new long[] {
            34L, 35L
        });
        sdd.updateEntityRel(33L, "Record", "band", 31L);
        sdd.updateEntityRel(33L, "Record", "tracks", new long[] {
            36L
        });
        sdd.updateEntityRel(34L, "Track", "band", 31L);
        sdd.updateEntityRel(34L, "Track", "record", 32L);
        sdd.updateEntityRel(35L, "Track", "band", 31L);
        sdd.updateEntityRel(35L, "Track", "record", 32L);
        sdd.updateEntityRel(36L, "Track", "band", 31L);
        sdd.updateEntityRel(35L, "Track", "record", 33L);

        sdd.waitUntilQueueEmpty();

        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

        System.out.println(JsonPrettyPrinter.prettyPrintJson(sdd.readEntity(
                32L, "page")));
    }

}
