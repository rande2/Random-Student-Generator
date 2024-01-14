/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileio;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.List;
/*
 *Note: This class is intended to read files outside of the JAR file.
 *This class can:
 *Get the program directory and user home directory
 *Locate a file given its location from the program directory or user home directory.
 *Read strings from and write strings to a specified file.
 *Read lines from and write lines to a specified file.
 *Read images.
 */
/**
 *
 * @author rande2
 */
public class FileIO {
    private static final String PROGRAM_DIR = findProgramDir();
    private static final String USER_HOME = findUserHome();

    public enum Location{PROGRAM,HOME};
    
    private static String findProgramDir() {
        //get path to the JAR file or build folder
        String dir = new File(System.getProperty("java.class.path")).getAbsolutePath();
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

    private static String findUserHome() {
        return new File(System.getProperty("user.home")).getAbsolutePath() + System.getProperty("file.separator");
    }

    public static String getProgramDir() {
        return PROGRAM_DIR;
    }

    public static String getUserHome() {
        return USER_HOME;
    }
    
    public static File getFile(String name, Location l) {
        return new File((l == Location.PROGRAM ? PROGRAM_DIR : USER_HOME) + name);
    }

    public static LinkedList<String> readLines(File file) {
        String line;
        LinkedList<String> result = new LinkedList<>();
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            while ((line = reader.readLine()) != null) {
                //add each line as a separate element of the linked list
                result.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static String readString(File file) {
        String result = null;
        try {
            //merge the lines as a single string
            result = Files.newBufferedReader(file.toPath()).lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static boolean writeLines(File file,List<String> data, OpenOption... o) {
        try (BufferedOutputStream bufferedOut = new BufferedOutputStream(Files.newOutputStream(file.toPath(), o))) {
            for (String i : data) {
                bufferedOut.write(i.getBytes());
                bufferedOut.write(System.lineSeparator().getBytes());
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean writeString(File file,String data, OpenOption... o) {
        try (BufferedOutputStream bufferedOut = new BufferedOutputStream(Files.newOutputStream(file.toPath(), o))) {
            bufferedOut.write(data.getBytes());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static Image readImage(File file){
        Image result = null;
        try{
            result = javax.imageio.ImageIO.read(file);
        }catch(IOException ex){
            Logger.getLogger(FileIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
