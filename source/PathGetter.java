/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileio;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    
    public static Path getPath(String name, Location l) {
        return Paths.get((l == Location.PROGRAM ? PROGRAM_DIR : USER_HOME) + name);
    }

    
}
