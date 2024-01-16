/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package randomPicker;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO use Collections.shuffle() and poll() ?
/**
 *
 * @author rande2
 */
public class Main {

    public static boolean getBool(String prompt, String t, String f) {
        Scanner in = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print(prompt);
            input = in.nextLine();
            if (input.equalsIgnoreCase(t)) {
                return true;
            } else if (input.equalsIgnoreCase(f)) {
                return false;
            }
            System.out.println("Invalid input.");
        }
    }
    
    public static String sep = System.getProperty("path.separator");

    public static boolean search(LinkedList<String> classes, String name) {
        for (String i : classes) {
            if (i.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void load(LinkedList<String> classes) {
        
    }

    public static void create(File classesFile,LinkedList<String> classes) {
        Scanner in = new Scanner(System.in);
        String name;
        while (true) {
            System.out.print("Enter a name for the class, \"cancel\" to cancel: ");
            name = in.nextLine();
            if (!name.equals("cancel")) {
                if (!search(classes, name)) {
                    if (getBool("Create new class named " + name + "?(Y/N): ", "y", "n")) {
                        classes.add(name);
                        FileIO.writeString(
                                classesFile,
                                name,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND
                        );
                        break;
                    }
                } else {
                    System.out.println("Class already exists.");
                }
            } else {
                return;
            }
        }
        File newClassFile = FileIO.getFile("resources"+sep+name,FileIO.Location.PROGRAM);
        try {
            newClassFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        LinkedList<String> names = new LinkedList<>();
        boolean running = true;
        OUTER:
        while (running) {
            System.out.print("Enter a new name, \"end\" to stop, \"delete\" to delete the previous name: ");
            name = in.nextLine();
            switch (name) {
                case "end":
                    break OUTER;
                case "delete":
                    if (!names.isEmpty()) {
                        System.out.println(names.pollLast());
                        if(!names.isEmpty()){
                            System.out.println("Remaining names:");
                            for(String i:names)
                                System.out.println(i);
                        }else
                            System.out.println("Class is now empty");
                    } else {
                        System.out.println("Could not delete name: class is empty");
                    }
                    break;
                default:
                    System.out.println("Added: "+name);
                    names.add(name);
            }
        }
        FileIO.writeLines(newClassFile, names, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input;
        File files = FileIO.getFile("resources/files.txt", FileIO.Location.PROGRAM);
        LinkedList<String> classes;
        if (files.canRead()) {
            classes = FileIO.readLines(files);
        } else {
            classes = new LinkedList<>();
        }
        OUTER:
        while (true) {
            System.out.print("Enter \"list\" to list all classes, \"load\" to load one, \"create\" to create one, \"quit\" to quit: ");
            input = in.nextLine();
            switch (input) {
                case "list":
                    System.out.println("All classes: ");
                    for (String i : classes) {
                        System.out.println(i);
                    }
                    break;
                case "l":
                    load(classes);
                    break;
                case "c":
                    create(files,classes);
                    break;
                case "quit":
                    break OUTER;
                default:
                    System.out.println("Invalid input.");
            }
        }

        
    }

}
