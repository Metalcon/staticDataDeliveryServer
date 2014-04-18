package de.metalcon.sdd;

import java.io.IOException;

import net.hh.request_dispatcher.ZmqWorkerProxy;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;

public class StaticDataDelivery implements AutoCloseable {

    public static final String DEFAULT_CONFIG_PATH =
            "/usr/share/metalcon/staticDataDelivery/config.xml";

    private Sdd sdd;

    private ZmqWorkerProxy proxy;

    //private ZmqWorker<SddRequest, SddResponse> worker;

    public StaticDataDelivery(
            String configPath) throws IOException {
        super();

        System.out.println("Loading config...");
        Config config = new XmlConfig(configPath);
        System.out.println("Starting Sdd...");
        sdd = new Sdd(config);

        System.out.println("Creating Proxy...");
        proxy = new ZmqWorkerProxy("tcp://141.26.71.69:1337");
        proxy.add(1, new RequestHandler(sdd));

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                try {
                    close();
                } catch (Exception e) {
                    // Ignore Exceptions we are shutting down
                }
            }

        });
    }

    @Override
    public void close() throws IOException {
        System.out.println("Closing Proxy...");
        if (proxy != null) {
            proxy.shutdown();
            proxy = null;
        }

        System.out.println("Closing Sdd...");
        if (sdd != null) {
            sdd.close();
            sdd = null;
        }
    }

    public void run() {
        System.out.println("Starting Proxy...");
        proxy.startWorkers();

        System.out.println("Ready!");
    }

    public static void main(String[] args) throws IOException {
        String configPath = DEFAULT_CONFIG_PATH;
        if (args.length > 0) {
            configPath = args[0];
        }
        System.out.println("Using configuration path: \"" + configPath + "\".");

        @SuppressWarnings("resource")
        StaticDataDelivery main = new StaticDataDelivery(configPath);
        main.run();
    }

}
