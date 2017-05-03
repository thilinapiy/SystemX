package org.thilina.systemx.licencemanager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by thilina on 4/30/17.
 */
public class Download extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        String user = "";
        if (session.getAttribute("user") == null){
            response.sendRedirect("/login");
        } else {

            String fileType = request.getParameter("filetype");
            if (fileType.equalsIgnoreCase("bootstrap")) {
                System.out.println("Trying to download bootstrap file...");
                downloadFile("bootstrap-0.1.jar", "/home/appuser/LicenceAgent/target", response);
            } else if (fileType.equalsIgnoreCase("server")) {
                System.out.println("Trying to download server zip...");
                downloadFile("ServerX-0.1.zip", "/home/appuser/ServerX/dist/target", response);
            }
        }
    }

    private static void downloadFile(String filename, String filepath, HttpServletResponse response){

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        System.out.println(filepath + "/" + filename + " is ready to download.");
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath + "/" + filename);

            int i;
            PrintWriter out = response.getWriter();
            while ((i = fileInputStream.read()) != -1) {
                out.write(i);
            }
            fileInputStream.close();
            System.out.println("Bootstrap file download complete.");
        } catch (IOException e){
            System.out.println("Error occurred while trying to download file: " + e.toString());
        }
    }
}
