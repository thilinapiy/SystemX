package org.thilina.systemx.licencemanager;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by thilina on 1/22/17.
 */
public class LoginHandler extends AbstractHandler
{

    @Override
    public void handle(String s, Request request,
                       HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse)
            throws IOException, ServletException {

        httpServletResponse.setContentType("text/html; charset=utf-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = httpServletResponse.getWriter();
        String username = request.getParameter("uname");
        String password = request.getParameter("passwd");

        out.println("<h1> Login " + username + "... </h1>");

        System.out.println("Login ...");

        request.setHandled(true);
    }
}
