package org.thilina.systemx.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 * Hello world!
 *
 */
public class WebServer{

    public static void startServer() throws Exception
    {
        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(WebServlet.class, "/*");

        server.start();
        server.join();
    }

    @SuppressWarnings("serial")
    public static class WebServlet extends HttpServlet {
        @Override
        protected void doGet( HttpServletRequest request,
                              HttpServletResponse response ) throws ServletException,
                IOException {

            if (request.getParameterMap().isEmpty()) {
                response.setContentType("text/html; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("You have two bugs in this. <br/> " +
                        "1. Its not verifying numbers. <br />" +
                        "2. Its not validating 0/0.");
                response.getWriter().println("<form action=''>" +
                        "A =  <input type='text' name='a' value='0'><br />" +
                        "B =  <input type='text' name='b' value='0'><br />" +
                        "A / B = <input type='submit' value='submit'>" +
                        "</form>");
            } else
                if (request.getParameterMap().containsKey("a") &&
                        request.getParameterMap().containsKey("b") ){
                    response.setContentType("text/html; charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println("A = " +
                            request.getParameter("a") +
                            " B = " +
                            request.getParameter("b"));
            }
            else {
                    response.setContentType("text/html; charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println("Some thing went wrong ...");
                }
        }

    }

}
