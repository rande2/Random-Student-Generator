/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankProject;

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

    //private static final Path PROGRAM_DIR = findProgramDir();
    private static final Path USER_HOME = Paths.get(System.getProperty("user.home"));
    private static final char sep = System.getProperty("file.separator").charAt(0);
    private static final Path PROGRAM_DIR = findProgDir();

    public static Path findProgDir() {
        String path = PathGetter.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        //if run from jar, the path ends with the jar file
        String jar = "raj.";
        boolean isJar = false;
        int sim = 0;
        for (int i = path.length() - 1; i > 0; i--) {
            if (isJar) {
                if (path.charAt(i) == '/') {
                    path = path.substring(0,i);
                    break;
                }
            } else {
                if (path.charAt(i) == jar.charAt(sim++)) {
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
        return USER_HOME.resolve(name);
    }

    public static Path programResource(String name) {
        return PROGRAM_DIR.resolve(name);
    }

    public static void ensureExistance(Path path) {
        if (!Files.exists(path))
            try {
            Path parent = path.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            Files.createFile(path);
        } catch (IOException ex) {
            Logger.getLogger(PathGetter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
