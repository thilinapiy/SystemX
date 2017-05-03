package org.thilina.systemx.licencemanager;


import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new RegisterUser()), "/signin");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");
        context.addServlet(new ServletHolder(new UUIDServlet()), "/setuuid");
        context.addServlet(new ServletHolder(new Download()), "/download");
        context.addServlet(new ServletHolder(new HelloServlet()), "/*");

        /*
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context});
        */

        //TODO: enable ssl
        /*
        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        SslContextFactory sslContextFactory = new SslContextFactory("server.jks");
        sslContextFactory.setKeyStorePassword("password");

        ServerConnector httpsConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https_config));
        httpsConnector.setPort(8443);
        httpsConnector.setIdleTimeout(50000);
        server.setConnectors(new Connector[]{ httpsConnector });
        */

        server.setHandler(context);
        server.start();
        server.join();
    }
}
