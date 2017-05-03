package org.thilina.systemx.licencemanager;

import org.bouncycastle.util.encoders.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

import static org.thilina.systemx.licencemanager.DbConnect.addUser;
import static org.thilina.systemx.licencemanager.GenerateLicenseAgent.runProguard;
import static org.thilina.systemx.licencemanager.GenerateLicenseAgent.generateUniqueID;

/**
 * Created by thilina on 4/9/17.
 */
public class RegisterUser extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null){
            response.sendRedirect("/");
        } else {
            response.getWriter().println("<form action=\"/signin\" method=\"POST\">\n" +
                    "    <input type=\"text\" placeholder=\"Enter FullName\" name=\"fullName\" required><br/>\n" +
                    "    <input type=\"text\" placeholder=\"Enter Username\" name=\"username\" required><br/>\n" +
                    "    <input type=\"text\" placeholder=\"Enter E-Mail Address\" name=\"email\" required><br/>\n" +
                    "    <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required><br/>\n" +
                    "    <input type=\"password\" placeholder=\"Re-Enter Password\" name=\"repassword\" required><br/>\n" +
                    "    <button type=\"submit\">Sign In</button>\n" +
                    "</form>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("User: " + username + " trying to sign-in to system.");

        if(fullName != null && email != null && username != null && password != null ){

            String useruuid = new String(Base64.encode(generateUniqueID()));
            addUser(username, password, fullName, email, useruuid);
            //generateCert(useruuid);
            System.out.println("Generating bootloader...");
            runProguard();
            new File("/home/appuser/LicenceAgent/target/LicenceAgent-0.1-small.jar")
                    .renameTo(new File("/home/appuser/LicenceAgent/target/bootstrap-0.1.jar"));
            System.out.println("Bootloader generation done.");
            response.sendRedirect("/login");
        }
        else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("Require all fields.");
            System.out.println("Require all fields.");
        }
    }
}
