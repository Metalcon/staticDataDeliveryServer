package de.metalcon.sdd.request;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONValue;

import de.metalcon.common.JsonPrettyPrinter;
import de.metalcon.common.JsonString;
import de.metalcon.sdd.IdDetail;
import de.metalcon.sdd.error.ReadRequestInvalidQueryError;
import de.metalcon.sdd.error.SddError;
import de.metalcon.sdd.server.Server;

public class ReadRequest extends Request {
    
    final public static char queryDelimeter = ',';

    private String query;
    
    public ReadRequest(Server server) {
        super(server);
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public Map<String, Object> run() {
        Map<String, Object> result;

        try {
            result = runTry();
        } catch (SddError e) {
            result = new LinkedHashMap<String, Object>();
            result.put("error", e.toJson());
        }

        return result;
    }

    public Map<String, Object> runTry() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        if (query == null)
            throw new ReadRequestInvalidQueryError();
        for (String idDetail : query.split(String.valueOf(queryDelimeter)))
            result.put(idDetail, new JsonString(getIdDetail(idDetail)));

        return result;
    }

    public String getIdDetail(String idDetail) {
        IdDetail entity = new IdDetail(idDetail);
        
        String json = server.readEntity(entity);
        if (json == null) {
            // TODO key didn't exists
        }
        
        return json;
    }
    
    @Override
    public void exec() {
        // TODO: You shouldn't push a ReadRequest into the Server queue.
        throw new RuntimeException();
    }

    public static void main(String[] args) throws InterruptedException {
        Server s = new Server();
        s.start(); 
        
        ReadRequest r = new ReadRequest(s);
        r.setQuery("2f364c13c0114e16:line,9568b0efb6ca8e26:symbol,2731c67201ae29ae:line,ce0058ac39a33616:line,11233033e2b36cff:symbol");
        String json = JSONValue.toJSONString(r.run());
        json = JsonPrettyPrinter.prettyPrintJson(json);
        System.out.println(json);
        Thread.sleep(100);
        
        s.stop();
    }

}