/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileio;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Rowan
 */
public class JarInput {
    public static LinkedList<String> readLines(String fileName) {
        String line;
        LinkedList<String> result = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(JarInput.class.getResourceAsStream(fileName))
        )) {
            while ((line = reader.readLine()) != null) {
                //add each line as a separate element of the linked list
                result.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(JarInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static String readString(String fileName){
        String result = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(JarInput.class.getResourceAsStream(fileName))
        )) {
            //merge the lines as a single string
            result = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception ex) {
            Logger.getLogger(JarInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static Image readImage(String fileName) {
        Image image = null;
        try {
            //return the image from the file
            image = javax.imageio.ImageIO.read(JarInput.class.getResourceAsStream(fileName));
        } catch (IOException ex) {
            Logger.getLogger(JarInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
}
