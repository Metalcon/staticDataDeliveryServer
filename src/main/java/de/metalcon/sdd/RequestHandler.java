package de.metalcon.sdd;

import java.io.Serializable;

import de.metalcon.sdd.api.requests.SddReadRequest;
import de.metalcon.sdd.api.requests.SddUpdateRequest;
import de.metalcon.sdd.exception.InvalidAttrException;
import de.metalcon.sdd.exception.InvalidDetailException;
import de.metalcon.sdd.exception.InvalidTypeException;
import de.metalcon.zmqworker.ZMQRequestHandler;

public class RequestHandler implements ZMQRequestHandler {

    private Sdd sdd;

    public RequestHandler(
            Sdd sdd) {
        this.sdd = sdd;
    }

    @Override
    public Serializable handleRequest(Object request) {
        if (request instanceof SddReadRequest) {
            SddReadRequest r = (SddReadRequest) request;
            try {
                String response = sdd.readEntity(r.getId(), r.getDetail());
                System.out.println(response);
            } catch (InvalidDetailException e) {
                e.printStackTrace();
            }
        } else if (request instanceof SddUpdateRequest) {
            SddUpdateRequest r = (SddUpdateRequest) request;
            try {
                boolean success =
                        sdd.updateEntity(r.getId(), r.getType(), r.getAttrs());
                System.out.println(success);
            } catch (InvalidTypeException | InvalidAttrException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
