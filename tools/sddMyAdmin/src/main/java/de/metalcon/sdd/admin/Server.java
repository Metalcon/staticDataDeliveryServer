package de.metalcon.sdd.admin;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.config.Config;
import de.metalcon.sdd.config.FileConfig;
import de.metalcon.sdd.exception.InvalidConfigException;

public class Server implements ServletContextListener {

    public static final String CONFIG_PATH = "/usr/share/sdd/config.xml";

    private Config config;

    private Sdd sdd;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            config = new FileConfig(Paths.get(CONFIG_PATH));
            sdd = new Sdd(config);
        } catch (IOException | InvalidConfigException e) {
            e.printStackTrace();
        }

        ServletContext context = sce.getServletContext();
        context.setAttribute("sdd-config", config);
        context.setAttribute("sdd", sdd);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            sdd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
