package de.metalcon.sdd;

import java.io.IOException;
import java.util.Map;

import net.hh.request_dispatcher.Callback;
import net.hh.request_dispatcher.Dispatcher;
import net.hh.request_dispatcher.RequestException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.zeromq.ZMQ;

import de.metalcon.api.responses.Response;
import de.metalcon.api.responses.errors.InternalServerErrorResponse;
import de.metalcon.api.responses.errors.UsageErrorResponse;
import de.metalcon.domain.Muid;
import de.metalcon.sdd.api.requests.SddReadRequest;
import de.metalcon.sdd.api.requests.SddRequest;
import de.metalcon.sdd.api.responses.SddSucessfulReadResponse;

public class DispatcherTest {

    private ZMQ.Context context;

    private Dispatcher dispatcher;

    @Before
    public void setUp() {
        context = ZMQ.context(1);

        dispatcher = new Dispatcher();
        dispatcher.registerService(SddRequest.class, "tcp://127.0.0.1:1337");
    }

    @After
    public void tearDown() throws IOException {
        dispatcher.shutdown();
        context.term();
    }

    @Test
    @Ignore
    public void test() {
        long t1 = System.currentTimeMillis();
        SddReadRequest readRequest = new SddReadRequest();
        readRequest.read(Muid.createFromID(7L), "nested");
        readRequest.read(Muid.createFromID(7L), "properties");
        readRequest.read(Muid.createFromID(8L), "nested");
        dispatcher.execute(readRequest, new Callback<Response>() {

            @Override
            public void onError(RequestException exception) {
                exception.printStackTrace();
            }

            @Override
            public void onSuccess(Response response) {
                System.out.println(response.getClass());
                if (response instanceof UsageErrorResponse) {
                    System.out.println(((UsageErrorResponse) response)
                            .getErrorMessage());
                } else if (response instanceof InternalServerErrorResponse) {
                } else if (response instanceof SddSucessfulReadResponse) {
                    for (Map.Entry<Muid, Map<String, String>> node : ((SddSucessfulReadResponse) response)
                            .get().entrySet()) {
                        System.out.println(node.getKey() + ":");
                        for (Map.Entry<String, String> output : node.getValue()
                                .entrySet()) {
                            System.out.println("  " + output.getKey() + "="
                                    + output.getValue());
                        }
                    }
                }
            }

        });
        dispatcher.gatherResults();
        long t2 = System.currentTimeMillis();
        System.out.println((t2 - t1) + " ms");
    }
}
