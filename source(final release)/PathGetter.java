/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package randomPicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rande2
 */
public class PathGetter {

    //private static final Path PROGRAM_DIR = findProgramDir();
    private static final Path USER_HOME = Paths.get(System.getProperty("user.home"));
    private static final char sep = System.getProperty("file.separator").charAt(0);
    private static final Path PROGRAM_DIR = findProgDir();

    private static Path findProgDir() {
        //get the location of the compiled version of this class
        String path = PathGetter.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        //if run from jar, the path ends with the jar file
        String jar = "raj.";
        boolean isJar = false;
        char character;
        int sim = 0;
        for (int i = path.length() - 1; i > 0; i--) {
            character = path.charAt(i);
            if (isJar) {
                if (character == '/') {
                    path = path.substring(0, i);
                    break;
                }
            } else {
                if (character == jar.charAt(sim++)) {
                    if (sim == jar.length()) {
                        isJar = true;
                    }
                } else {
                    break;
                }
            }
        }
        return Paths.get(path);
    }

    public static Path getProgramDir() {
        return PROGRAM_DIR;
    }

    public static Path getUserHome() {
        return USER_HOME;
    }

    public static Path getFromHome(String name) {
        //get file from user's home directory
        Path result;
        try {
            result= USER_HOME.resolve(name);
        } catch (Exception ex) {
            result= null;
        }
        return result;
    }

    public static Path programResource(String name) {
        //get file from next to the executing JAR file
        Path result;
        try {
            result= PROGRAM_DIR.resolve(name);
        } catch (Exception ex) {
            result= null;
        }
        return result;
    }

    public static void ensureExistance(Path path) {
        //if the file does not exist
        if (!Files.exists(path))
            try {
            //get directory containing the file
            Path parent = path.getParent();
            //if the directory does not exist, create it
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        } catch (IOException ex) {
            Logger.getLogger(PathGetter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<String> readLines(Path path) {
        //read all lines from a file
        ArrayList<String> lines = new ArrayList<>(25);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            //add lines to list of lines
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(PathGetter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
}
