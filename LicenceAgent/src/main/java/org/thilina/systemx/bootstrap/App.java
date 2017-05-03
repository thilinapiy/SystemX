package org.thilina.systemx.bootstrap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.thilina.systemx.core.Division;
import org.thilina.systemx.web.WebServer;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Hello world!
 *
 */
public class App extends URLClassLoader
{
    public App(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public static void main( String[] args ) {
        System.out.println( "Starting Licence agent ..." + args[0]);

        if (args[0].equalsIgnoreCase("init")) {
            new ManageLicense();

            try {
                File jars = new File("../libs/");
                Collection<File> fileList = FileUtils.listFiles(jars,
                        new RegexFileFilter("^(.*jar)"), DirectoryFileFilter.DIRECTORY);

                if (!fileList.isEmpty()) {
                    URL[] urls = new URL[fileList.size()];
                    int jarCount = 0;

                    for (File jar : fileList) {
                        String name = jar.getAbsoluteFile().toString();
                        //System.out.println(name);
                        urls[jarCount] = new URL("jar:file:" + name + "!/");
                        jarCount++;
                    }

                    App cl = new App(urls, App.class.getClassLoader());
                    System.out.println("Pre : " + Thread.currentThread().getContextClassLoader());
                    Thread.currentThread ().setContextClassLoader (cl);
                    System.out.println("Pos : " + Thread.currentThread().getContextClassLoader());

                    for (File jar : fileList) {
                        JarFile jarFile = new JarFile(jar);
                        Enumeration<JarEntry> e = jarFile.entries();
                        while (e.hasMoreElements()) {
                            JarEntry je = e.nextElement();
                            if (je.isDirectory() || !je.getName().endsWith(".class") || je.getName().contains("jetty")) {
                                continue;
                            }
                            String className = je.getName().substring(0, je.getName().length() - 6);
                            className = className.replace('/', '.');
                            //System.out.println("------------------------------------------------" + className);
                            Class c = cl.loadClass(className);
                        }
                    }
                }

                final ClassLoader cl = Thread.currentThread().getContextClassLoader();
                final Class app = cl.loadClass("org.thilina.systemx.bootstrap.App");
                final Method appmain = app.getMethod("main", new Class[]{String[].class});
                final String [] appargs = new String [args.length - 1];
                System.arraycopy (args, 1, appargs, 0, appargs.length);
                System.out.println("before invoke : " + Thread.currentThread().getContextClassLoader());
                appmain.invoke (null, new Object [] {appargs});

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            System.out.println("After invoke : " + Thread.currentThread().getContextClassLoader());
            Division d = new Division();
            System.out.println("++++++++++++++++++++++" + d.getDivision(10.0, 2.0).toString());

            try {
                new WebServer().startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public Class loadClass(final String name, boolean resolve)
            throws ClassNotFoundException {
        //System.out.println("loadClass (" + name + ", " + resolve + ")");

        Class c = null;

        // first, check if this class has already been defined by this classloader
        // instance:
        c = findLoadedClass(name);

        if (c == null) {
            Class parentsVersion = null;
            try {
                // this is slightly unorthodox: do a trial load via the
                // parent loader and note whether the parent delegated or not;
                // what this accomplishes is proper delegation for all core
                // and extension classes without my having to filter on class name:
                parentsVersion = getParent().loadClass(name);

                if (parentsVersion.getClassLoader() != getParent())
                    c = parentsVersion;
            } catch (ClassNotFoundException ignore) {
            } catch (ClassFormatError ignore) {
            }

            if (c == null) {
                try {
                    // ok, either 'c' was loaded by the system (not the bootstrap
                    // or extension) loader (in which case I want to ignore that
                    // definition) or the parent failed altogether; either way I
                    // attempt to define my own version:
                    c = findClass(name);
                } catch (ClassNotFoundException ignore) {
                    // if that failed, fall back on the parent's version
                    // [which could be null at this point]:
                    c = parentsVersion;
                }
            }
        }

        if (c == null)
            throw new ClassNotFoundException(name);

        resolve=true;
        if (resolve)
            resolveClass(c);
        return c;
    }


    protected Class findClass(final String name) throws ClassNotFoundException {
        //System.out.println("findClass (" + name + ")");
        final String classResource = name.replace('.', '/') + ".class";
        final URL classURL = getResource(classResource);

        if (classURL == null)
            throw new ClassNotFoundException(name);
        else {
            byte classBytes[];
            InputStream input = null;
            try {
                input = classURL.openStream();
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                int nextValue = input.read();
                while (-1 != nextValue) {
                    byteStream.write(nextValue);
                    nextValue = input.read();
                }
                classBytes = byteStream.toByteArray();
                // "decrypt":
                // TODO: Generate patched classes when applying patchs.
                if (name.equalsIgnoreCase("org.thilina.systemx.core.Division") ||
                        name.equalsIgnoreCase("org.thilina.systemx.core.App")) {
                    crypt(classBytes);
                    // to generate encyrpted classes
                    //dumpClassFile(name,classBytes);
                    //crypt(classBytes);
                    //
                    System.out.println("decrypted [" + name + "]");
                }
                return defineClass(name, classBytes, 0, classBytes.length);
            } catch (IOException ioe) {
                throw new ClassNotFoundException(name);
            } finally {
                if (input != null) try {
                    input.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    private static void crypt(final byte[] data) {
        for (int i = 8; i < data.length; ++i) data[i] ^= 0x11;
    }

    private static void dumpClassFile(String name,byte classBytes[]){
        System.out.println("======================================");

        OutputStream out = null;
        try{
            out = new FileOutputStream(new File(name));
            out.write(classBytes);
        }
        catch (Exception e){
            System.out.println("File write error : " + e.toString());
        }
        finally{
            if (out != null) try { out.close (); } catch (Exception ignore) {}
        }
    }
}
