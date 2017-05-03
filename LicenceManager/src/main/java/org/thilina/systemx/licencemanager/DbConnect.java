package org.thilina.systemx.licencemanager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;

/**
 * Created by thilina on 4/9/17.
 */
public class DbConnect {

    protected static Connection getConnection() throws SQLException {
        /*
        String host = System.getenv("MYSQLS_HOSTNAME");
        String port = System.getenv("MYSQLS_PORT");
        String database = System.getenv("MYSQLS_DATABASE");
        String username = System.getenv("MYSQLS_USERNAME");
        String password = System.getenv("MYSQLS_PASSWORD");
        String dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        */
        // TODO: remove below
        String username = "userx_AC2rbQAt";
        String password = "4:7facLC_e";
        String dbUrl = "jdbc:mysql://mysql.storage.cloud.wso2.com:3306/LicenseManager_dummy2744";

        return DriverManager.getConnection(dbUrl, username, password);
    }

    protected static void addUser(String username, String password, String fullName, String email, String useruuid){

        try {
            Connection connection = getConnection();
            String insert = "insert into user(username, password, fullname, email, useruuid) values (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(insert);
            stmt.setString(1, username);


            stmt.setString(2, getSha256(password));
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, useruuid);
            stmt.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error :" + e.toString());
        }
    }

    protected static boolean validateUser(String username, String password){
        boolean validUser = false;
        try {
            Connection connection = getConnection();
            String select = "select count(userid) as count from user where username=\"" + username + "\" and password=\"" + getSha256(password) + "\"";
            //System.out.println(select);
            PreparedStatement stmt = connection.prepareStatement(select);
            //stmt.setString(1, username);
            //stmt.setString(2, getSha256(password));
            ResultSet rs = stmt.executeQuery(select);
            if (rs.next()) {
                if (rs.getInt("count") == 1) {
                    validUser = true;
                } else {
                    validUser = false;
                }
            } else {
                System.out.print("Error validating user.");
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error :" + e.toString());
        }
        return validUser;
    }

    private static String getSha256(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuffer sb = new StringBuffer();
            for(byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e){
            System.out.println("Error : " + e.toString());
            return "Error";
        }
    }

}
