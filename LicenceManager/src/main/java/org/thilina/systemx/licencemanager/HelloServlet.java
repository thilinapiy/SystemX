package org.thilina.systemx.licencemanager;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by thilina on 3/14/17.
 */
public class HelloServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        String user = "";
        if (session.getAttribute("user") == null){
            response.sendRedirect("/login");
        } else {
            user = (String) session.getAttribute("user");
            response.getWriter().println("Welcome " + user + "... ");
        }

        File server = new File("/home/appuser/ServerX/dist/target/ServerX-0.1.zip");
        if(server.exists() && !server.isDirectory()) {
            response.getWriter().println("<br/><a href=\"/download?filetype=server\">Download ServerX</a>");
        }
        File bootstrap = new File("/home/appuser/LicenceAgent/target/bootstrap-0.1.jar");
        if(bootstrap.exists() && !bootstrap.isDirectory()) {
            response.getWriter().println("<br/><a href=\"/download?filetype=bootstrap\">Download bootstrap</a>");
        }

    }
}
