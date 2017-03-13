package org.thilina.systemx.licencemanager;

import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Endpoint;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(8080);

        HttpConfiguration http_config = new HttpConfiguration();
        http_config.setSecureScheme("https");
        http_config.setSecurePort(8443);
        HttpConfiguration https_config = new HttpConfiguration(http_config);
        https_config.addCustomizer(new SecureRequestCustomizer());

        ContextHandler context = new ContextHandler("/");
        context.setContextPath("/");
        context.setHandler(new HelloHandler());

        ContextHandler login = new ContextHandler("/login");
        login.setHandler(new LoginHandler());

        ContextHandler hb = new ContextHandler("/hb");
        hb.setHandler(new HBHandler());

        ContextHandler admin = new ContextHandler("/admin");
        admin.setVirtualHosts(new String[] { "127.0.0.1" });
        admin.setHandler(new AdminHandler());

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context, login, hb, admin });

        server.setHandler(contexts);

        SslContextFactory sslContextFactory = new SslContextFactory("server.jks");
        sslContextFactory.setKeyStorePassword("password");

        ServerConnector httpsConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https_config));
        httpsConnector.setPort(8443);
        httpsConnector.setIdleTimeout(50000);

        //TODO: enable ssl
        //server.setConnectors(new Connector[]{ httpsConnector });
        server.start();
        server.join();
    }
}
