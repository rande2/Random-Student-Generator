/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package randomPicker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static int search(List<String> classes, String name) {
        int size = classes.size();
        for(int i=0;i<size;i++){
            if (classes.get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public static void load(List<String> classes) {
        Scanner in = new Scanner(System.in);
        System.out.println("All classes:");
        for (String i : classes) {
            System.out.println(i);
        }
        String input;
        while (true) {
            System.out.print("Enter the name of the class you wish to load. Enter \"cancel\" to cancel: ");
            input = in.nextLine();
            if (search(classes, input)!=-1) {
                break;
            } else if (input.equals("cancel")) {
                return;
            } else {
                System.out.println("Class does not exist");
            }
        }
        Path targetClass = PathGetter.programResource("resources/" + input);
        List<String> names = null;
        try {
            names = Files.readAllLines(targetClass);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (names != null) {
            Spinner spinner = new Spinner(names);
            String student;
            while ((student = spinner.spin()) != null) {
                System.out.println(student);
                System.out.println("Press Enter to continue, enter \"stop\" to stop");
                if(in.nextLine().equals("stop"))
                    break;
            }
        } else {
            System.out.println("An error occured");
        }
    }

    public static void create(Path indexFile, List<String> classes) {
        Scanner in = new Scanner(System.in);
        String className;
        while (true) {
            System.out.print("Enter a name for the class, \"cancel\" to cancel: ");
            className = in.nextLine();
            if (!className.equals("cancel")) {
                if (search(classes, className)==-1) {
                    if (getBool("Create new class named " + className + "?(Y/N): ", "y", "n")) {
                        classes.add(className);
                        try ( BufferedWriter writer = Files.newBufferedWriter(
                                indexFile,
                                StandardOpenOption.CREATE,
                                StandardOpenOption.APPEND
                        )) {
                            writer.write(className);
                            writer.newLine();
                            writer.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }
                } else {
                    System.out.println("Class already exists.");
                }
            } else {
                return;
            }
        }
        Path newClassFile = PathGetter.programResource("resources/" + className);
        PathGetter.ensureExistance(newClassFile);
        LinkedList<String> names = new LinkedList<>();
        String name;
        OUTER:
        while (true) {
            System.out.print("Enter a new name, \"end\" to stop, \"list\" to list all names, \"delete\" to delete a name: ");
            name = in.nextLine();
            switch (name) {
                case "list":
                    System.out.println("All names:");
                    for(String i:names)
                        System.out.println(i);
                    break;
                case "end":
                    break OUTER;
                case "delete":
                    if (!names.isEmpty()) {
                        System.out.print("Enter a name to delete: ");
                        deleteStr(in.nextLine(),names);
                        if (!names.isEmpty()) {
                            System.out.println("Remaining names:");
                            for (String i : names) {
                                System.out.println(i);
                            }
                        } else {
                            System.out.println("Class is now empty");
                        }
                    } else {
                        System.out.println("Could not delete name: class is empty");
                    }
                    break;
                default:
                    System.out.println("Added: " + name);
                    names.add(name);
            }
        }
        try ( BufferedWriter writer = Files.newBufferedWriter(
                newClassFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            for (String i : names) {
                writer.write(i);
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("New class created successfully");
    }
    
    public static void deleteStr(String str,List<String> lst){
        int index;
        if((index=search(lst,str))!=-1)
            lst.remove(index);
    }
    
    public static void delete(Path file,List<String> classes){
        Scanner scanner = new Scanner(System.in);
        System.out.println("All classes:");
        for(String i:classes)
            System.out.println(i);
        while(true){
            System.out.println("Enter the name of the class you would like to delte, \"cancel\" to cancel: ");
            String remove = scanner.nextLine();
            if(remove.equals("cancel"))
                return;
            int index=search(classes,remove);
            if(index!=-1){
                classes.remove(index);
                try(BufferedWriter writer = Files.newBufferedWriter(file,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                )){
                for(String i:classes){
                    writer.write(i);
                    writer.newLine();
                }
                writer.close();
                break;
            }catch(IOException ex){
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            }else{
                System.out.println("File not found");
            }
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input;
        Path path = PathGetter.programResource("resources/classes.txt");
        PathGetter.ensureExistance(path);
        List<String> classes = null;
        try {
            classes = Files.readAllLines(path);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        OUTER:
        while (true) {
            System.out.print("Enter \"list\" to list all classes, \"load\" to load a class, \"create\" to create a class, \"delete\" to delete a class, \"quit\" to quit: ");
            input = in.nextLine();
            switch (input) {
                case "list":
                    System.out.println("All classes: ");
                    for (String i : classes) {
                        System.out.println(i);
                    }
                    break;
                case "load":
                    load(classes);
                    break;
                case "create":
                    create(path, classes);
                    break;
                case "delete":
                    delete(path, classes);
                    break;
                case "quit":
                    break OUTER;
                default:
                    System.out.println("Invalid input.");
            }
        }

    }

}
