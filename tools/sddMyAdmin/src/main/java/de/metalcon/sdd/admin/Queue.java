package de.metalcon.sdd.admin;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Queue extends Servlet {

    private static final long serialVersionUID = -8712411377313278203L;

    @Override
    protected StringWriter run(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StringWriter c = new StringWriter();
        c.append("<h2>Queue</h2>\n");

        StringWriter w = new StringWriter();
        w.append(printHtml(printHead("Queue"), printBody(c)).toString());
        return w;
    }

}
