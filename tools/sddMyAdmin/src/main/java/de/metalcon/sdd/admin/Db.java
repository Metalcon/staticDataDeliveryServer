package de.metalcon.sdd.admin;

import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;

import java.io.StringWriter;
import java.lang.reflect.Field;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iq80.leveldb.DB;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.tooling.GlobalGraphOperations;

import de.metalcon.common.JsonPrettyPrinter;
import de.metalcon.sdd.Sdd;

public class Db extends Servlet {

    private static final long serialVersionUID = -8712411377313278203L;

    private DB jsonDb;

    private GraphDatabaseService entityGraph;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        try {
            Field jsonDbField = Sdd.class.getDeclaredField("jsonDb");
            jsonDbField.setAccessible(true);
            jsonDb = (DB) jsonDbField.get(sdd);

            Field entityGraphField = Sdd.class.getDeclaredField("entityGraph");
            entityGraphField.setAccessible(true);
            entityGraph = (GraphDatabaseService) entityGraphField.get(sdd);
        } catch (NoSuchFieldException | SecurityException
                | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected StringWriter run(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        boolean showProperties = getParam(request, "showProperties");
        boolean showReferenced = getParam(request, "showReferenced");
        boolean showReferencing = getParam(request, "showReferencing");
        boolean showOutputs = getParam(request, "showOutputs");
        boolean showPrettyPrinted = getParam(request, "showPrettyPrinted");

        StringWriter c = new StringWriter();
        c.append("<h2>Db</h2>\n");
        c.append("\n");

        c.append("<form action=\"db\" method=\"GET\">\n");
        c.append("  <ul>\n");
        c.append("    <li><input type=\"checkbox\" name=\"showProperties\" value=\"false\"");
        if (!showProperties) {
            c.append(" checked=\"checked\"");
        }
        c.append(" /> Hide Properties</li>\n");
        c.append("    <li><input type=\"checkbox\" name=\"showReferenced\" value=\"false\"");
        if (!showReferenced) {
            c.append(" checked=\"checked\"");
        }
        c.append(" /> Hide Referenced</li>\n");
        c.append("    <li><input type=\"checkbox\" name=\"showReferencing\" value=\"false\"");
        if (!showReferencing) {
            c.append(" checked=\"checked\"");
        }
        c.append(" /> Hide Referencing</li>\n");
        c.append("    <li><input type=\"checkbox\" name=\"showOutputs\" value=\"false\"");
        if (!showOutputs) {
            c.append(" checked=\"checked\"");
        }
        c.append(" /> Hide Outputs</li>\n");
        c.append("    <li><input type=\"checkbox\" name=\"showPrettyPrinted\" value=\"false\"");
        if (!showPrettyPrinted) {
            c.append(" checked=\"checked\"");
        }
        c.append(" /> Don't Pretty Printed</li>\n");
        c.append("  </ul>\n");
        c.append("  <button type=\"submit\">Filter</button>\n");
        c.append("</form>");

        c.append("<h3>Entities</h3>\n");

        for (Node entity : GlobalGraphOperations.at(entityGraph).getAllNodes()) {
            Long id = (Long) entity.getProperty("id", null);
            c.append("<h4>Node ");
            if (id == null) {
                c.append("without ID");
            } else {
                c.append(id.toString());
            }
            c.append(" (Neo4j-ID=" + entity.getId() + ")");
            c.append("</h4>\n");
            c.append("\n");

            Iterable<String> propertyKeys = entity.getPropertyKeys();
            if (showProperties && propertyKeys.iterator().hasNext()) {
                c.append("<p>Propeties (Neo4j-Node-Properties):</p>\n");
                c.append("<table>\n");
                for (String property : propertyKeys) {
                    if (property.startsWith("json-")) {
                        continue;
                    }
                    Object value = entity.getProperty(property, null);

                    c.append("  <tr>\n");
                    c.append("    <td>" + property + "</td>\n");
                    c.append("    <td>" + value.toString() + "</td>\n");
                    c.append("  </tr>\n");
                }
                c.append("</table>\n");
                c.append("\n");
            }

            Iterable<Relationship> referencedRels =
                    entity.getRelationships(Direction.OUTGOING);
            if (showReferenced && referencedRels.iterator().hasNext()) {
                c.append("<p>Referenced Entities (Neo4j-Outgoing-Relationships):</p>\n");
                c.append("<ul>\n");
                for (Relationship referencedRel : referencedRels) {
                    Node referenced = referencedRel.getEndNode();
                    Long referencedId =
                            (Long) referenced.getProperty("id", null);
                    c.append("  <li>Node ");
                    if (referencedId == null) {
                        c.append(" without ID");
                    } else {
                        c.append(referencedId.toString());
                    }
                    c.append("</li>\n");
                }
                c.append("</ul>\n");
            }

            Iterable<Relationship> referencingRels =
                    entity.getRelationships(Direction.INCOMING);
            if (showReferencing && referencingRels.iterator().hasNext()) {
                c.append("<p>Referencing Entities (Neo4j-Incoming-Relationships):</p>\n");
                c.append("<ul>\n");
                for (Relationship referencingRel : referencingRels) {
                    Node referencing = referencingRel.getStartNode();
                    Long referencingId =
                            (Long) referencing.getProperty("id", null);
                    c.append("  <li>Node ");
                    if (referencingId == null) {
                        c.append(" without ID");
                    } else {
                        c.append(referencingId.toString());
                    }
                    c.append("</li>\n");
                }
                c.append("</ul>\n");
            }

            if (showOutputs && id != null) {
                c.append("<p>Output (LevelDB-Entries):</p>\n");
                c.append("<table>\n");
                for (String detail : config.getDetails()) {
                    String output =
                            asString(jsonDb.get(bytes(id.toString()
                                    + config.getIdDetailDelimiter() + detail)));
                    if (showPrettyPrinted) {
                        output = JsonPrettyPrinter.prettyPrintJson(output);
                    }

                    c.append("  <tr>\n");
                    c.append("    <td>" + detail + "</td>\n");
                    c.append("    <td><pre>" + output + "</pre></td>\n");
                    c.append("  </tr>\n");
                }
                c.append("</table>\n");
                c.append("\n");
            }

            c.append("\n");
        }

        StringWriter w = new StringWriter();
        w.append(printHtml(printHead("Db"), printBody(c)).toString());
        return w;
    }

    private boolean getParam(HttpServletRequest request, String name) {
        String param = request.getParameter(name);
        if (param == null) {
            return true;
        }
        if (param.equals("false") || param.equals("0")) {
            return false;
        }
        return true;
    }
}
