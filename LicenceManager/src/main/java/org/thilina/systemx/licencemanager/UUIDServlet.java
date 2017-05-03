package org.thilina.systemx.licencemanager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by thilina on 3/15/17.
 */
public class UUIDServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        String user = "";

        if (session.getAttribute("user") == null){
            response.sendRedirect("/login");
        } else {
            String uuid = request.getParameter("uuid");
            //new UUID().setUuid(request.getParameter("uuid").toString());
            session.setAttribute("uuid", uuid);
            System.out.println("Receive UUID: " + uuid);
        }
    }
}
