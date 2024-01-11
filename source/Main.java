/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package randomPicker;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Arrays;

/**
 *
 * @author rande2
 */
public class Main {
    //for use only with console versions
    public static String getString(String prompt){
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }
    
    //for use only with console versions
    public static boolean getBool(String prompt,String t,String f){
        Scanner in = new Scanner(System.in);
        String input;
        while(true){
            System.out.print(prompt);
            input=in.nextLine();
            if(input.equalsIgnoreCase(t))
                return true;
            else if(input.equalsIgnoreCase(f))
                return false;
            System.out.println("Invalid input.");
        }
    }
    
    /**
     * @param args the command line arguments
     */
    private static final String fileNames = "files.txt";
 
    public static void main(String[] args) {
        IO.init();
        Scanner in = new Scanner(System.in);
        
        File file = IO.getFile(fileNames,Location.PROGRAM);
        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(IOException ex){
                System.err.println(ex);
            }
        }
        
        LinkedList<String> files = IO.readLines(
                IO.getInputStream(fileNames,Location.PROGRAM)
        );
        
        
        
        LinkedList<String> students = new LinkedList<String>(Arrays.asList("a","b","c","d","e","f"));
        
        Spinner spinner = new Spinner(students);
        String name;
        
        
        
        while((name=spinner.spin())!=null){
            System.out.println(name);
            System.out.println("Press enter to continue");
            in.nextLine();
        }
        spinner.reset();
        while((name=spinner.spin())!=null){
            System.out.println(name);
            System.out.println("Press enter to continue");
            in.nextLine();
        }

    }
    
}
