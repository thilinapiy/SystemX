package org.thilina.systemx.licencemanager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by thilina on 3/14/17.
 */
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        String user = ((session.getAttribute("user") == null) ? "" : (String) session.getAttribute("user"));
        session.invalidate();
        System.out.println("User " + user + " logged out");
        response.sendRedirect("/login");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        String user = ((session.getAttribute("user") == null) ? "" : (String) session.getAttribute("user"));
        session.invalidate();
        System.out.println("User " + user + " logged out");
        response.sendRedirect("/login");
    }
}
