package de.metalcon.sdd;

import java.io.IOException;

import net.hh.request_dispatcher.server.ZmqWorker;

import org.zeromq.ZMQ;

import de.metalcon.api.responses.Response;
import de.metalcon.sdd.api.requests.SddRequest;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.XmlConfig;

public class StaticDataDelivery {

    public static void main(String[] args) throws IOException {
        System.out.println("Loading config...");
        Config config = new XmlConfig("/usr/share/sdd/config.xml");
        System.out.println("Starting Sdd...");
        Sdd sdd = new Sdd(config);

        System.out.println("Creating Context...");
        ZMQ.Context context = ZMQ.context(1);

        System.out.println("Creating Worker...");
        ZmqWorker<SddRequest, Response> worker =
                new ZmqWorker<>(context, "tcp://127.0.0.1:1337",
                        new RequestHandler(sdd));

        System.out.println("Starting Worker...");
        worker.start();

        System.out.println("Ready!");
    }

}
