package org.thilina.systemx.licencemanager;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

import java.io.File;
import java.security.*;
import java.security.cert.Certificate;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Created by thilina on 4/9/17.
 */
public class GenerateLicenseAgent {

    protected static byte[] generateUniqueID(){

        // Get 256 random bits
        byte[] bytes = new byte[256/8];

        try {
            SecureRandom sr1 = SecureRandom.getInstance("SHA1PRNG");

            int seedByteCount = 10;
            byte[] seed = sr1.generateSeed(seedByteCount);

            sr1.setSeed(seed);
            sr1.nextBytes(bytes);
        } catch (Exception e){
            System.out.println("Error generating unique id: " + e.toString());
        }
        return bytes;
    }

    protected static void generateCert(String useruuid) {
        try {
            //KeyStore keystore = KeyStore.getInstance("PKCS12", "BC");
            //FileInputStream keystoreFile = new FileInputStream(new File(System.getProperty("user.dir")+"/server.jks"));
            //keystore.load(keystoreFile, "password".toCharArray());

            FileInputStream store = new FileInputStream(System.getProperty("user.dir")+"/server.jks");

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(store, "password".toCharArray());
            String alias = "server";
            Key key = keystore.getKey(alias, "password".toCharArray());
            System.out.println(key.toString());
            if (key instanceof PrivateKey) {
                // Get certificate of public key
                Certificate cert = keystore.getCertificate(alias);
                System.out.println(cert.toString());

                // Get public key
                PublicKey publicKey = cert.getPublicKey();
                System.out.println(publicKey.toString());
                // Return a key pair
                new KeyPair(publicKey, (PrivateKey) key);
            }

        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    protected static void runProguard(){
        try {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File("/home/appuser/LicenceAgent/pom.xml"));
            request.setGoals(Arrays.asList("clean", "package"));

            Invoker invoker = new DefaultInvoker();
            invoker.setMavenHome(new File("/opt/maven"));
            invoker.execute(request);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
