package de.metalcon.sdd.config;

import java.lang.invoke.MethodHandles;

public class Options extends de.metalcon.dbhelper.Options {

    static {
        try {
            Options.initialize(
                    "/usr/share/metalcon/staticDataDelivery/config.txt",
                    MethodHandles.lookup().lookupClass());
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String LEVELDB_PATH;

    public static String NEO4J_PATH;

    public static String LISTEN_URI;

}
