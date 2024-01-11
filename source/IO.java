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
public class IO {

    //methods that allow files to be read correctly when
    //running the program from an IDE
    //and when running the jar file
    //limitations: must create a new output stream each time you want to write
    private static String programDir;
    private static String userHome;

    public static void init() {
        //get path to the JAR file or build folder
        programDir = new File(System.getProperty("java.class.path")).getAbsolutePath();
        //check if the location is to a JAR file
        //if so, remove the jar file's name from the path
        String jar = ".jar";
        int jarLength = jar.length();
        int progDirLen = programDir.length();
        int lengthDiff = progDirLen - jarLength;
        boolean isJar = true;

        //check of the path ends with ".jar"
        for (int i = 0; i < jarLength; i++) {
            if (jar.charAt(i) != (programDir.charAt(i + lengthDiff))) {
                isJar = false;
                break;
            }
        }
        //remove the JAR file from the path
        if (isJar) {
            String temp = programDir;
            for (int i = progDirLen - 5; i > 0; i--) {
                if (programDir.charAt(i) == System.getProperty("file.separator").charAt(0)) {
                    programDir = programDir.substring(0, i + 1);
                    break;
                }
            }
            //directory is [name].jar
            if (programDir.equals(temp)) {
                programDir = "";
            }
        } else {
            programDir += System.getProperty("file.separator");
        }

        userHome = new File(System.getProperty("user.home")).getAbsolutePath() + System.getProperty("file.separator");
    }

    public static String getProgramDir() {
        return programDir;
    }

    public static String getUserHome() {
        return userHome;
    }

    public static File getFile(String filename, Location location) {
        File result = null;
        try {
            if (location != Location.JAR) {
                result = new File((location == Location.PROGRAM ? programDir : userHome) + filename);
            } else {
                result = new File(IO.class.getResource(filename).toURI());
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static InputStream getInputStream(String filename, Location location) {
        InputStream result = null;
        try {
            if (location != Location.JAR) {
                //use newInputStream to create an input stream to the file
                result = Files.newInputStream(
                        Paths.get((location == Location.PROGRAM ? programDir : userHome) + filename)
                );
            } else {
                //use getResourceAsStream to create an input stream to the file in the JAR
                result = IO.class.getResourceAsStream(filename);
            }
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static LinkedList<String> readLines(String fileName, Location location) {
        String line;
        LinkedList<String> result = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getInputStream(fileName, location))
        )) {
            while ((line = reader.readLine()) != null) {
                //add each line as a separate element of the linked list
                result.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static String readString(String fileName, Location location) {
        String result = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getInputStream(fileName, location))
        )) {
            //merge the lines as a single string
            result = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static OutputStream getOutputStream(String filename, Location location, OpenOption... o) {
        OutputStream result = null;
        //can't write to files in a JAR
        if (location != Location.JAR) {
            try {
                result = Files.newOutputStream(Paths.get((location == Location.PROGRAM ? programDir : userHome) + filename), o);
            } catch (IOException ex) {
                Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public static boolean writeLines(String fileName, Location location, List<String> data, OpenOption... o) {
        try (BufferedOutputStream bufferedOut = new BufferedOutputStream(
                getOutputStream(fileName, location, o))) {
            for (String i : data) {
                bufferedOut.write(i.getBytes());
                bufferedOut.write(System.lineSeparator().getBytes());
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean writeString(String fileName, Location location, String data, OpenOption... o) {
        try (BufferedOutputStream bufferedOut = new BufferedOutputStream(
                getOutputStream(fileName, location, o)
        )) {
            bufferedOut.write(data.getBytes());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static Image readImage(String fileName, Location location) {
        Image image = null;
        try {
            //return the image from the file
            image = javax.imageio.ImageIO.read(getInputStream(fileName, location));
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
}
