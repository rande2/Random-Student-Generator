/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rande2
 */
public class PathGetter {
    private static final String PROGRAM_DIR = findProgramDir();
    private static final String USER_HOME = findUserHome();

    public enum Location{PROGRAM,HOME};
    
    private static String findProgramDir() {
        //get path to the JAR file or build folder
        String dir = new File(System.getProperty("java.class.path")).getAbsolutePath();
        Path path = Paths.get(dir);
        if(!Files.isDirectory(path))
            return path.getParent().toString()+System.getProperty("file.separator");
        return path.toString();
        
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
    
    public static Path getPath(String name, Location l) {
        return Paths.get((l == Location.PROGRAM ? PROGRAM_DIR : USER_HOME) + name);
    }
    
    public static void ensureExistance(Path path){
        if(!Files.exists(path))
            try {
                Path parent = path.getParent();
                if(!Files.exists(parent))
                    Files.createDirectories(parent);
                Files.createFile(path);
            } catch (IOException ex) {
                Logger.getLogger(PathGetter.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
