package de.metalcon.sdd.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.config.Config;

public abstract class Servlet extends HttpServlet {

    private static final long serialVersionUID = -4674178024396584095L;

    protected Config config;

    protected Sdd sdd;

    protected static final StringWriter printHtml(
            StringWriter head,
            StringWriter body) {
        StringWriter w = new StringWriter();
        w.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        w.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n");
        w.append("  \"http://www.w3.org/2002/08/xhtml/xhtml1-strict.dtd\">\n");
        w.append("<html xmlns=\"http://www.w3.org/1999/xhtml\"\n");
        w.append("      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        w.append("      xsi:schemaLocation=\"http://www.w3.org/1999/xhtml\n");
        w.append("                           http://www.w3.org/2002/08/xhtml/xhtml1-strict.xsd\"\n");
        w.append("      lang=\"en\" xml:lang=\"en\">\n");
        w.append(head.toString());
        w.append(body.toString());
        w.append("</html>\n");
        return w;
    }

    protected static final StringWriter printHead(String title) {
        StringWriter w = new StringWriter();
        w.append("  <head>\n");
        w.append("    <title>SddMyAdmin");
        if (title != null) {
            w.append(" - " + title);
        }
        w.append("</title>\n");
        w.append("  </head>\n");
        return w;
    }

    protected static final StringWriter printBody(StringWriter content) {
        StringWriter w = new StringWriter();
        w.append("  <body>\n");
        w.append("    <h1><a href=\".\">SddMyAdmin</a></h1>\n");
        w.append("    <ul>\n");
        w.append("      <li><a href=\"config\">Config</a></li>\n");
        w.append("      <li><a href=\"queue\">Queue</a></li>\n");
        w.append("    </ul>\n");
        w.append(content.toString());
        w.append("  </body>\n");
        return w;
    }

    protected abstract StringWriter run(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            try {
                StringWriter w = run(request, response);
                response.setContentType("application/xhtml+xml");
                writer.println(w);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));
                response.setContentType("text/plain");
                writer.println(stackTrace);
                writer.flush();
            }
        }
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);

        ServletContext context = getServletContext();
        config = (Config) context.getAttribute("sdd-config");
        sdd = (Sdd) context.getAttribute("sdd");
    }

}
