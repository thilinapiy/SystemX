package org.thilina.systemx.licencemanager;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.thilina.systemx.licencemanager.DbConnect.validateUser;

/**
 * Created by thilina on 3/14/17.
 */
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null){
            response.sendRedirect("/");
        } else {
            response.getWriter().println("<form action=\"/login\" method=\"POST\">\n" +
                    "    <input type=\"text\" placeholder=\"Enter Username\" name=\"username\" required><br/>\n" +
                    "    <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required><br/>\n" +
                    "    <button type=\"submit\">Login</button>\n" +
                    "</form>" +
                    "<br/> Or <a href=\"/signin\">SignIn</a>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("User: " + username + " trying to login to system.");

        if(username != null && password != null){
            if (validateUser(username, password)) {
                response.setStatus(HttpServletResponse.SC_OK);
                HttpSession session = request.getSession();
                session.setAttribute("user", username);
                response.getWriter().println("Loged in as: " + username);
                System.out.println("Loged in as: " + username);

                response.sendRedirect("/");
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().println("Login failed.");
                System.out.println("Login failed.");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("Require both fields.");
            System.out.println("Require both fields.");
        }
    }
}
