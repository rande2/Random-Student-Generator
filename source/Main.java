/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package randomPicker;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO use Collections.shuffle() and poll() ?
/**
 *
 * @author rande2
 */
public class Main {
    public static String getString(String prompt){
        Scanner in = new Scanner(System.in);
        return in.nextLine();
    }
    
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
    public static void main(String[] args) {
        LinkedList<String> students = new LinkedList<String>(Arrays.asList("a","b","c","d","e","f"));
        /*do{
            items.add(getString("Enter a new name: "));
        }while(getBool("Would you like to continue?(Y/N): ","y","n"));
        */
       
        Scanner in = new Scanner(System.in);
        int studentNumber,size;
        
        while((size=students.size())>0){
            studentNumber = (int)(Math.random()*(students.size()));
            System.out.println(students.get(studentNumber));
            students.remove(studentNumber);
            System.out.println("Press enter to continue");
            in.nextLine();
        }

        
    }
    
}
