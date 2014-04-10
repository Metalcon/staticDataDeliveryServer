package de.metalcon.sdd;

import java.io.IOException;

import net.hh.request_dispatcher.server.ZmqWorker;

import org.zeromq.ZMQ;

import de.metalcon.sdd.api.requests.SddRequest;
import de.metalcon.sdd.api.responses.SddResponse;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;

public class StaticDataDelivery implements AutoCloseable {

    public static final String DEFAULT_CONFIG_PATH =
            "/usr/share/sdd/config.xml";

    private Sdd sdd;

    private ZMQ.Context context;

    private ZmqWorker<SddRequest, SddResponse> worker;

    public StaticDataDelivery(
            String configPath) throws IOException {
        super();

        System.out.println("Loading config...");
        Config config = new XmlConfig(configPath);
        System.out.println("Starting Sdd...");
        sdd = new Sdd(config);

        System.out.println("Creating Context...");
        context = ZMQ.context(1);
        System.out.println("Creating Worker...");
        worker =
                new ZmqWorker<SddRequest, SddResponse>(context,
                        "tcp://127.0.0.1:1337", new RequestHandler(sdd));

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
        System.out.println("Closing Worker...");
        if (worker != null) {
            worker.close();
            worker = null;
        }
        System.out.println("Closing Context...");
        if (context != null) {
            context.close();
            context = null;
        }

        System.out.println("Closing Sdd...");
        if (sdd != null) {
            sdd.close();
            sdd = null;
        }
    }

    public void run() {
        System.out.println("Starting worker...");
        worker.start();

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
