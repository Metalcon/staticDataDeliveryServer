package de.metalcon.sdd.admin;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.metalcon.sdd.config.ConfigEntity;
import de.metalcon.sdd.config.ConfigEntityOutput;
import de.metalcon.sdd.config.ConfigType;

public class Config extends Servlet {

    private static final long serialVersionUID = -8712411377313278203L;

    @Override
    protected StringWriter run(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StringWriter c = new StringWriter();
        c.append("<h2>Config</h2>\n");
        c.append("\n\n");

        c.append("<h3>Miscellaneous</h3>\n");
        c.append("<table>\n");
        c.append("  <tr>\n");
        c.append("    <td>CONFIG_PATH</td>\n");
        c.append("    <td>" + Server.CONFIG_PATH + "</td>\n");
        c.append("  </tr>\n");
        c.append("  <tr>\n");
        c.append("    <td>ConfigClass</td>\n");
        c.append("    <td>" + config.getClass().getCanonicalName() + "</td>\n");
        c.append("  </tr>\n");
        c.append("  <tr>\n");
        c.append("    <td>LeveldbPath</td>\n");
        c.append("    <td>" + config.getLeveldbPath() + "</td>\n");
        c.append("  </tr>\n");
        c.append("  <tr>\n");
        c.append("    <td>Neo4jPath</td>\n");
        c.append("    <td>" + config.getNeo4jPath() + "</td>\n");
        c.append("  </tr>\n");
        c.append("  <tr>\n");
        c.append("    <td>IdDelimiter</td>\n");
        c.append("    <td>" + config.getIdDelimiter() + "</td>\n");
        c.append("  </tr>\n");
        c.append("  <tr>\n");
        c.append("    <td>IdDetailDelimiter</td>\n");
        c.append("    <td>" + config.getIdDetailDelimiter() + "</td>\n");
        c.append("  </tr>\n");
        c.append("</table>\n");
        c.append("\n\n");

        c.append("<h3>Details</h3>\n");
        c.append("<ul>\n");
        for (String detail : config.getDetails()) {
            c.append("  <li>" + detail + "</li>\n");
        }
        c.append("</ul>\n");
        c.append("\n\n");

        c.append("<h3>Entities</h3>\n");
        for (String entity : config.getEntities()) {
            ConfigEntity configEntity = config.getEntity(entity);

            c.append("\n");
            c.append("<h4>" + entity + "</h4>\n");
            c.append("<p>Attributes:</p>\n");
            c.append("<ul>\n");
            for (String attr : configEntity.getAttrs()) {
                ConfigType attrType = configEntity.getAttr(attr);

                c.append("  <li>");
                c.append("<em>" + attr + "</em> : " + attrType.getType());
                if (attrType.isArray()) {
                    c.append(" " + ConfigType.ARRAY_SUFFIX);
                }
                if (attrType.isPrimitive()) {
                    c.append(" (Primitive)");
                }
                c.append("</li>\n");
            }
            c.append("</ul>\n");
            c.append("<p>Outputs:</p>\n");
            c.append("<ul>\n");
            for (String output : configEntity.getOutputs()) {
                ConfigEntityOutput configEntityOutput =
                        configEntity.getOutput(output);

                c.append("  <li>\n");
                c.append("    <h5>" + output + "</h5>\n");
                c.append("    <ul>\n");
                for (String oattr : configEntityOutput.getOattrs()) {
                    String oattrDetail = configEntityOutput.getOattr(oattr);
                    c.append("      <li><em>" + oattr + "</em>");
                    if (!oattrDetail.equals("")) {
                        c.append(" &lt;" + oattrDetail + "&gt;");
                    }
                    c.append("</li>\n");
                }
                c.append("    </ul>\n");
                c.append("  </li>\n");
            }
            c.append("</ul>\n");
        }

        StringWriter w = new StringWriter();
        w.append(printHtml(printHead("Config"), printBody(c)).toString());
        return w;
    }
}
