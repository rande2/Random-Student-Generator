/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileio;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author rande2
 */
public class FileResource {

    String path;
    Location location;
    OpenOption[] openOptions;
    private static final String PROGRAM_DIR = findProgramDir();
    private static final String USER_HOME = findUserHome();

    FileResource(String n, Location l, OpenOption... o) {
        StringBuilder builder = new StringBuilder();
        if(l!=Location.JAR)
            builder.append(l == Location.PROGRAM ? PROGRAM_DIR : USER_HOME);
        builder.append(n);
        path=builder.toString();
        location = l;
        openOptions = o;
    }

    private static String findProgramDir() {
        //get path to the JAR file or build folder
        String dir;
        dir = new File(System.getProperty("java.class.path")).getAbsolutePath();
        //check if the location is to a JAR file
        //if so, remove the jar file's name from the path
        String jar = ".jar";
        int jarLength = jar.length();
        int progDirLen = dir.length();
        int lengthDiff = progDirLen - jarLength;
        boolean isJar = true;
        //check of the path ends with ".jar"
        for (int i = 0; i < jarLength; i++) {
            if (jar.charAt(i) != (dir.charAt(i + lengthDiff))) {
                isJar = false;
                break;
            }
        }
        //remove the JAR file from the path
        if (isJar) {
            String temp = dir;
            for (int i = progDirLen - 5; i > 0; i--) {
                if (dir.charAt(i) == System.getProperty("file.separator").charAt(0)) {
                    dir = dir.substring(0, i + 1);
                    break;
                }
            }
            //directory is [name].jar
            if (dir.equals(temp)) {
                dir = "";
            }
        } else {
            dir += System.getProperty("file.separator");
        }
        return dir;
    }

    public static String findUserHome() {
        return new File(System.getProperty("user.home")).getAbsolutePath() + System.getProperty("file.separator");
    }

    public static String getProgramDir() {
        return PROGRAM_DIR;
    }

    public static String getUserHome() {
        return USER_HOME;
    }

    public File getFile() {
        File result = null;
        try {
            if (location != Location.JAR) {
                result = new File(path);
            } else {
                result = new File(FileResource.class.getResource(path).toURI());
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(FileResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public InputStream getInputStream() {
        InputStream result = null;
        try {
            if (location != Location.JAR) {
                //use newInputStream to create an input stream to the file
                result = Files.newInputStream(
                        Paths.get(path)
                );
            } else {
                //use getResourceAsStream to create an input stream to the file in the JAR
                result = FileResource.class.getResourceAsStream(path);
            }
        } catch (IOException ex) {
            System.out.println("Whoops!");
            Logger.getLogger(FileResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public LinkedList<String> readLines() {
        String line;
        LinkedList<String> result = new LinkedList<>();
        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader(getInputStream())
        )) {
            while ((line = reader.readLine()) != null) {
                //add each line as a separate element of the linked list
                result.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public String readString() {
        String result = null;
        try ( BufferedReader reader = new BufferedReader(
                new InputStreamReader(getInputStream())
        )) {
            //merge the lines as a single string
            result = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception ex) {
            Logger.getLogger(FileResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public OutputStream getOutputStream() {
        OutputStream result = null;
        //can't write to files in a JAR
        if (location != Location.JAR) {
            try {
                result = Files.newOutputStream(Paths.get(path), openOptions);
            } catch (IOException ex) {
                Logger.getLogger(FileResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean writeLines(String fileName, Location location, List<String> data, OpenOption... o) {
        try ( BufferedOutputStream bufferedOut = new BufferedOutputStream(
                getOutputStream())) {
            for (String i : data) {
                bufferedOut.write(i.getBytes());
                bufferedOut.write(System.lineSeparator().getBytes());
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean writeString(String fileName, Location location, String data, OpenOption... o) {
        try ( BufferedOutputStream bufferedOut = new BufferedOutputStream(
                getOutputStream()
        )) {
            bufferedOut.write(data.getBytes());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Image readImage(String fileName, Location location) {
        Image image = null;
        try {
            //return the image from the file
            image = javax.imageio.ImageIO.read(getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(FileResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
}