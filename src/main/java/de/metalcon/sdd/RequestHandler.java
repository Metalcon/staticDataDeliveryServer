package de.metalcon.sdd;

import de.metalcon.api.responses.Response;
import de.metalcon.api.responses.SuccessResponse;
import de.metalcon.api.responses.errors.InternalServerErrorResponse;
import de.metalcon.api.responses.errors.UsageErrorResponse;
import de.metalcon.sdd.api.requests.SddReadRequest;
import de.metalcon.sdd.api.requests.SddUpdateRequest;
import de.metalcon.sdd.exception.SddUsageException;
import de.metalcon.zmqworker.ZMQRequestHandler;

public class RequestHandler implements ZMQRequestHandler {

    private Sdd sdd;

    public RequestHandler(
            Sdd sdd) {
        this.sdd = sdd;
    }

    @Override
    public Response handleRequest(Object request) {
        try {
            if (request instanceof SddReadRequest) {
                SddReadRequest r = (SddReadRequest) request;

                String response = sdd.readEntity(r.getId(), r.getDetail());
            } else if (request instanceof SddUpdateRequest) {
                SddUpdateRequest r = (SddUpdateRequest) request;

                if (!sdd.updateEntity(r.getId(), r.getType(), r.getAttrs())) {
                    return new InternalServerErrorResponse(
                            "Could not push on queue.", "Maybe try again.");
                }
                return new SuccessResponse();
            }
        } catch (SddUsageException e) {
            return new UsageErrorResponse(e, null);
        }
        // TODO: catch SddInternalServerException when they are thrown

        return new UsageErrorResponse("Unkown Request Type.",
                "Use one of the types defined in staticDataDeliveryServerApi "
                        + "package: de.metalcon.sdd.api.requests");
    }
}
