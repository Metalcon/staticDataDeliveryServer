package de.metalcon.sdd;

import de.metalcon.api.responses.Response;
import de.metalcon.api.responses.errors.InternalServerErrorResponse;
import de.metalcon.api.responses.errors.UsageErrorResponse;
import de.metalcon.sdd.api.exception.SddInternalServerException;
import de.metalcon.sdd.api.exception.SddUsageException;
import de.metalcon.sdd.api.requests.AddRelationsAction;
import de.metalcon.sdd.api.requests.DeleteAction;
import de.metalcon.sdd.api.requests.DeleteRelationsAction;
import de.metalcon.sdd.api.requests.ReadRequestAction;
import de.metalcon.sdd.api.requests.SddReadRequest;
import de.metalcon.sdd.api.requests.SddRequest;
import de.metalcon.sdd.api.requests.SddWriteRequest;
import de.metalcon.sdd.api.requests.SetPropertiesAction;
import de.metalcon.sdd.api.requests.SetRelationAction;
import de.metalcon.sdd.api.requests.SetRelationsAction;
import de.metalcon.sdd.api.requests.WriteRequestAction;
import de.metalcon.sdd.api.responses.SddCouldNotQueueResponse;
import de.metalcon.sdd.api.responses.SddSucessfulReadResponse;
import de.metalcon.sdd.api.responses.SddSucessfullQueueResponse;

public class RequestHandler implements
        net.hh.request_dispatcher.server.RequestHandler<SddRequest, Response> {

    private static final long serialVersionUID = 5403742746062109485L;

    private Sdd sdd;

    public RequestHandler(
            Sdd sdd) {
        this.sdd = sdd;
    }

    @Override
    public Response handleRequest(SddRequest request) {
        try {
            if (request instanceof SddReadRequest) {
                return handleReadRequest((SddReadRequest) request);
            } else if (request instanceof SddWriteRequest) {
                return handleWriteRequest((SddWriteRequest) request);
            }

            return new UsageErrorResponse("unknown request type: "
                    + request.getClass(),
                    "use requests defined in staticDataDeliveryApi");
        } catch (SddInternalServerException e) {
            return new InternalServerErrorResponse(e, null);
        } catch (SddUsageException e) {
            return new UsageErrorResponse(e, null);
        } catch (RuntimeException e) {
            return new UsageErrorResponse(e, null);
        }
    }

    private Response handleReadRequest(SddReadRequest request) {
        SddSucessfulReadResponse response = new SddSucessfulReadResponse();
        for (ReadRequestAction read : request.getActions()) {
            long nodeId = read.getNodeId();
            String detail = read.getDetail();
            String output = sdd.read(nodeId, detail);
            response.add(nodeId, detail, output);
        }
        return response;
    }

    private Response handleWriteRequest(SddWriteRequest request) {
        WriteTransaction tx = sdd.createWriteTransaction();
        for (WriteRequestAction write : request.getActions()) {
            if (write instanceof SetPropertiesAction) {
                SetPropertiesAction w = (SetPropertiesAction) write;
                tx.setProperties(w.getNodeId(), w.getNodeType(),
                        w.getProperties());
            } else if (write instanceof SetRelationAction) {
                SetRelationAction w = (SetRelationAction) write;
                tx.setRelation(w.getNodeId(), w.getNodeType(), w.getRelation(),
                        w.getToId());
            } else if (write instanceof SetRelationsAction) {
                SetRelationsAction w = (SetRelationsAction) write;
                tx.setRelations(w.getNodeId(), w.getNodeType(),
                        w.getRelation(), w.getToIds());
            } else if (write instanceof AddRelationsAction) {
                AddRelationsAction w = (AddRelationsAction) write;
                tx.addRelations(w.getNodeId(), w.getNodeType(),
                        w.getRelation(), w.getToIds());
            } else if (write instanceof DeleteAction) {
                DeleteAction w = (DeleteAction) write;
                tx.delete(w.getNodeId());
            } else if (write instanceof DeleteRelationsAction) {
                DeleteRelationsAction w = (DeleteRelationsAction) write;
                tx.deleteRelations(w.getNodeId(), w.getNodeType(),
                        w.getRelation(), w.getToIds());
            } else {
                throw new IllegalStateException("Unkown WriteRequestAction.");
            }
        }
        if (tx.commit()) {
            return new SddSucessfullQueueResponse();
        } else {
            return new SddCouldNotQueueResponse();
        }
    }

}
