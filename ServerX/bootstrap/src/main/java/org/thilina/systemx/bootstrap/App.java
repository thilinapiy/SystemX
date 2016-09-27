package org.thilina.systemx.bootstrap;

import org.thilina.systemx.web.WebServer;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            new WebServer().startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
