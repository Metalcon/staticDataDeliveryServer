package de.metalcon.sdd.admin;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Index extends Servlet {

    private static final long serialVersionUID = 1447112510408174138L;

    @Override
    protected StringWriter run(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StringWriter c = new StringWriter();
        c.append("<h2>Index</h2>\n");

        StringWriter w = new StringWriter();
        w.append(printHtml(printHead(null), printBody(c)).toString());
        return w;
    }

}
