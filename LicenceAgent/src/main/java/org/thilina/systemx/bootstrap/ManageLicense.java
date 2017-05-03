package org.thilina.systemx.bootstrap;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.http.HttpHeaders.USER_AGENT;
/**
 * Created by thilina on 3/13/17.
 */
public class ManageLicense {


    protected ManageLicense(){
        String uuid = getSystemUUID();
        sendDetails(uuid);
        applyPatches();
    }

    private String getSystemUUID(){

        String uuid = "";
        String file_path = "/etc/machine-id";

        try {
            uuid = new String(Files.readAllBytes(Paths.get(file_path)));
        } catch (IOException e) {
            System.out.println(e.toString());
            uuid = "-1";
            System.exit(-100);
        }
        return uuid;
    }

    private void applyPatches(){

        File jars = new File("../patches");
        Collection<File> fileList = FileUtils.listFiles(jars,
                new RegexFileFilter("^(.*jar)"), DirectoryFileFilter.DIRECTORY);
                //new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);

        if(fileList.isEmpty()) {
            System.out.println("No patches to apply.");
        } else {
            System.out.println("Applying patches to system...");
            for (File patch : fileList) {
                String sourceName = patch.getName();
                String parent = patch.getParent();
                Path source = patch.getAbsoluteFile().toPath();
                Path target = new File("../libs/" + sourceName).toPath();
                try {
                    Files.move(source, target, REPLACE_EXISTING);
                    System.out.println("Applied " + parent + "/" + sourceName + " to system.");
                } catch (IOException e) {
                    System.out.println("Error copping patches: " + e.toString());
                    System.exit(-124);
                }
            }
            System.out.println("=============================");
            System.out.println("Patch application successful.");
            System.out.println("Restart the system.");
            System.out.println("=============================");
            System.exit(0);
        }
    }

    private void sendDetails(String uuid){

        String cookie = "";
        CookieHandler.setDefault(new CookieManager());
        HttpClient client = HttpClientBuilder.create().build();

        //String server_host = "lm.systemx.thilina.org";
        //String server_host = "dummy2744-licensemanager-1-0-1.wso2apps.com";
        String server_host = "localhost:8080";

        //TODO: enable ssl
        String login_url = "http://" + server_host + "/login";
        HttpPost login_post = new HttpPost(login_url);

        String uuid_url = "http://" + server_host + "/setuuid";
        HttpPost uuid_post = new HttpPost(uuid_url);
        uuid_post.setHeader("User-Agent", USER_AGENT);

        Console console = System.console();        String username = "";
        String password = "";
        try {
            System.out.print("Enter username : ");
            username = console.readLine();
            System.out.print("Enter password : ");
            password = String.valueOf(console.readPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<NameValuePair> loginParameters = new ArrayList<NameValuePair>();
        //TODO: Change username/password authentication to something else.
        loginParameters.add(new BasicNameValuePair("username", username));
        loginParameters.add(new BasicNameValuePair("password", password));

        List<NameValuePair> uuidParameters = new ArrayList<NameValuePair>();
        uuidParameters.add(new BasicNameValuePair("uuid",  uuid));

        try {
            login_post.setEntity(new UrlEncodedFormEntity(loginParameters));
            HttpResponse login_response = client.execute(login_post);
            int login_response_code = login_response.getStatusLine().getStatusCode();
            //System.out.println("Login response Code : " + login_response_code);

            cookie = login_response.getFirstHeader("Set-Cookie") == null ? "" :
                    login_response.getFirstHeader("Set-Cookie").toString();

            if (login_response_code == 302 || login_response_code == 200) {
                uuid_post.setHeader("Cookie", cookie);

                uuid_post.setEntity(new UrlEncodedFormEntity(uuidParameters));
                HttpResponse uuid_response = client.execute(uuid_post);
                int uuid_response_code = uuid_response.getStatusLine().getStatusCode();
                //System.out.println("UUID response Code : " + uuid_response_code);

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(uuid_response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                //System.out.println(result.toString());
            } else {
                System.out.println("Login failed.");
                System.exit(-100);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-100);
        }

    }

    private void dummyMethod1(){
        String dummy1 = "";
        String dummy2 = "";

    }
}
