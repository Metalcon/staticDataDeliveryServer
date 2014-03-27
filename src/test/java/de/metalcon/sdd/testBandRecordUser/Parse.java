package de.metalcon.sdd.testBandRecordUser;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.WriteTransaction;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;
import de.metalcon.sdd.exception.AlreadyCommitedException;
import de.metalcon.sdd.exception.EmptyIdException;
import de.metalcon.sdd.exception.EmptyTransactionException;
import de.metalcon.sdd.exception.InvalidConfigException;
import de.metalcon.sdd.exception.InvalidNodeTypeException;
import de.metalcon.sdd.exception.InvalidPropertyException;
import de.metalcon.sdd.exception.InvalidRelationException;

public class Parse {

    public static final int TRANSACTION_LENGTH = 5;

    public static final long BAND_ID_PREFIX = 1000000000L;

    public static final long RECORD_ID_PREFIX = 2000000000L;

    public static final long USER_ID_PREFIX = 3000000000L;

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
    public void parse() throws EmptyTransactionException,
            AlreadyCommitedException, InvalidNodeTypeException,
            InvalidPropertyException, InvalidRelationException,
            EmptyIdException {
        parseBand();
        parseRecord();
        parseUser();
        parseBandRecord();
        parseUserBand();
        parseUserRecord();
    }

    private void parseBand() throws EmptyTransactionException,
            AlreadyCommitedException, InvalidNodeTypeException,
            InvalidPropertyException, EmptyIdException {
        System.out.println("Bands");

        Iterable<String[]> bands =
                new CsvIteratable(
                        Paths.get("src/test/resources/testBandRecordUser/Band.csv"),
                        "\t", "\n###\n");

        long t1 = System.currentTimeMillis();

        WriteTransaction tx = sdd.createWriteTransaction();

        int i = 0;
        int t = 0;
        for (String[] band : bands) {
            ++i;

            long id = BAND_ID_PREFIX + Long.parseLong(band[0]);

            Map<String, String> properties = new HashMap<String, String>();
            properties.put("name", band[1]);
            properties.put("desc", band[2]);
            properties.put("picid", band[3]);

            tx.setProperties(id, "Band", properties);

            ++t;
            if (t == TRANSACTION_LENGTH) {
                t = 0;
                tx.commit();
                tx = sdd.createWriteTransaction();
            }
        }

        if (t != 0) {
            tx.commit();
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Count: " + i);
        System.out.println("filling queue: " + (t2 - t1) + " ms");

        long t3 = System.currentTimeMillis();

        sdd.waitUntilQueueEmpty();

        long t4 = System.currentTimeMillis();

        System.out.println("working queue: " + (t4 - t3) + " ms");
        System.out.println();
    }

    private void parseRecord() throws InvalidNodeTypeException,
            InvalidPropertyException, AlreadyCommitedException,
            EmptyTransactionException, EmptyIdException {
        System.out.println("Records");
        Iterable<String[]> records =
                new CsvIteratable(
                        Paths.get("src/test/resources/testBandRecordUser/Record.csv"),
                        "\t", "\n");

        long t1 = System.currentTimeMillis();

        WriteTransaction tx = sdd.createWriteTransaction();

        int i = 0;
        int t = 0;
        for (String[] record : records) {
            ++i;

            long id = RECORD_ID_PREFIX + Long.parseLong(record[0]);

            Map<String, String> properties = new HashMap<String, String>();
            properties.put("name", record[1]);
            properties.put("release", record[2]);

            tx.setProperties(id, "Record", properties);

            ++t;
            if (t == TRANSACTION_LENGTH) {
                t = 0;
                tx.commit();
                tx = sdd.createWriteTransaction();
            }
        }

        if (t != 0) {
            tx.commit();
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Count: " + i);
        System.out.println("filling queue: " + (t2 - t1) + " ms");

        long t3 = System.currentTimeMillis();

        sdd.waitUntilQueueEmpty();

        long t4 = System.currentTimeMillis();

        System.out.println("working queue: " + (t4 - t3) + " ms");
        System.out.println();
    }

    private void parseUser() throws InvalidNodeTypeException,
            InvalidPropertyException, AlreadyCommitedException,
            EmptyTransactionException, EmptyIdException {
        System.out.println("Users");
        Iterable<String[]> users =
                new CsvIteratable(
                        Paths.get("src/test/resources/testBandRecordUser/User.csv"),
                        "\t", "\n");

        long t1 = System.currentTimeMillis();

        WriteTransaction tx = sdd.createWriteTransaction();

        int i = 0;
        int t = 0;
        for (String[] user : users) {
            ++i;

            long id = USER_ID_PREFIX + Long.parseLong(user[0]);

            Map<String, String> properties = new HashMap<String, String>();
            properties.put("name", user[1]);
            properties.put("picid", user[2]);

            tx.setProperties(id, "User", properties);

            ++t;
            if (t == TRANSACTION_LENGTH) {
                t = 0;
                tx.commit();
                tx = sdd.createWriteTransaction();
            }
        }

        if (t != 0) {
            tx.commit();
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Count: " + i);
        System.out.println("filling queue: " + (t2 - t1) + " ms");

        long t3 = System.currentTimeMillis();

        sdd.waitUntilQueueEmpty();

        long t4 = System.currentTimeMillis();

        System.out.println("working queue: " + (t4 - t3) + " ms");
        System.out.println();
    }

    private void parseBandRecord() throws InvalidRelationException,
            InvalidNodeTypeException, AlreadyCommitedException,
            EmptyTransactionException, EmptyIdException {
        System.out.println("BandRecord");
        Iterable<String[]> bandRecords =
                new CsvIteratable(
                        Paths.get("src/test/resources/testBandRecordUser/BandRecord.csv"),
                        "\t", "\n");

        long t1 = System.currentTimeMillis();

        WriteTransaction tx = sdd.createWriteTransaction();

        int i = 0;
        int t = 0;
        for (String[] bandRecord : bandRecords) {
            ++i;

            long bandId = BAND_ID_PREFIX + Long.parseLong(bandRecord[0]);
            long recordId = RECORD_ID_PREFIX + Long.parseLong(bandRecord[1]);

            tx.addRelations(bandId, "Band", "records", new long[] {
                recordId
            });
            tx.addRelations(recordId, "Record", "bands", new long[] {
                bandId
            });

            ++t;
            if (t == TRANSACTION_LENGTH) {
                t = 0;
                tx.commit();
                tx = sdd.createWriteTransaction();
            }
        }

        if (t != 0) {
            tx.commit();
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Count: " + i);
        System.out.println("filling queue: " + (t2 - t1) + " ms");

        long t3 = System.currentTimeMillis();

        sdd.waitUntilQueueEmpty();

        long t4 = System.currentTimeMillis();

        System.out.println("working queue: " + (t4 - t3) + " ms");
        System.out.println();
    }

    private void parseUserBand() throws InvalidRelationException,
            InvalidNodeTypeException, AlreadyCommitedException,
            EmptyTransactionException, EmptyIdException {
        System.out.println("UserBand");
        Iterable<String[]> userBands =
                new CsvIteratable(
                        Paths.get("src/test/resources/testBandRecordUser/UserBand.csv"),
                        "\t", "\n");

        long t1 = System.currentTimeMillis();

        WriteTransaction tx = sdd.createWriteTransaction();

        int i = 0;
        int t = 0;
        for (String[] userBand : userBands) {
            ++i;

            long userId = USER_ID_PREFIX + Long.parseLong(userBand[0]);
            long bandId = BAND_ID_PREFIX + Long.parseLong(userBand[1]);

            tx.addRelations(userId, "User", "bands", new long[] {
                bandId
            });
            tx.addRelations(bandId, "Band", "users", new long[] {
                userId
            });

            ++t;
            if (t == TRANSACTION_LENGTH) {
                t = 0;
                tx.commit();
                tx = sdd.createWriteTransaction();
            }
        }

        if (t != 0) {
            tx.commit();
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Count: " + i);
        System.out.println("filling queue: " + (t2 - t1) + " ms");

        long t3 = System.currentTimeMillis();

        sdd.waitUntilQueueEmpty();

        long t4 = System.currentTimeMillis();

        System.out.println("working queue: " + (t4 - t3) + " ms");
        System.out.println();
    }

    private void parseUserRecord() throws InvalidRelationException,
            InvalidNodeTypeException, AlreadyCommitedException,
            EmptyTransactionException, EmptyIdException {
        System.out.println("UserRecord");
        Iterable<String[]> userRecords =
                new CsvIteratable(
                        Paths.get("src/test/resources/testBandRecordUser/UserRecord.csv"),
                        "\t", "\n");

        long t1 = System.currentTimeMillis();

        WriteTransaction tx = sdd.createWriteTransaction();

        int i = 0;
        int t = 0;
        for (String[] userRecord : userRecords) {
            ++i;

            long userId = USER_ID_PREFIX + Long.parseLong(userRecord[0]);
            long recordId = RECORD_ID_PREFIX + Long.parseLong(userRecord[1]);

            tx.addRelations(userId, "User", "records", new long[] {
                recordId
            });
            tx.addRelations(recordId, "Record", "users", new long[] {
                userId
            });

            ++t;
            if (t == TRANSACTION_LENGTH) {
                t = 0;
                tx.commit();
                tx = sdd.createWriteTransaction();
            }
        }

        if (t != 0) {
            tx.commit();
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Count: " + i);
        System.out.println("filling queue: " + (t2 - t1) + " ms");

        long t3 = System.currentTimeMillis();

        sdd.waitUntilQueueEmpty();

        long t4 = System.currentTimeMillis();

        System.out.println("working queue: " + (t4 - t3) + " ms");
        System.out.println();
    }

}
