/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fileio;

import java.util.Scanner;
import java.nio.file.StandardOpenOption;
/**
 *
 * @author Rowan
 */
public class FileIO {
//java -jar "C:\Users\Rowan\OneDrive\Documents\NetBeansProjects\FileIO\dist\FileIO.jar" 

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner in = new Scanner(System.in);
        IO.init();
        System.out.println("path to jar: "+System.getProperty("java.class.path"));
        System.out.println("path to folder containing jar: "+IO.getProgramDir());
        System.out.println("home directory: "+IO.getUserHome());
        System.out.println("working directory: "+System.getProperty("user.dir"));
        
        System.out.println(IO.readString(IO.getInputStream("resources/file.txt", Location.PROGRAM)));
       
        System.out.println(IO.readString(IO.getInputStream("stuff.txt",Location.JAR)));
        System.out.println("Press \'Enter\' to continue... ");
        in.nextLine();
        System.out.println(IO.writeString(IO.getOutputStream("new_file.txt",Location.PROGRAM, StandardOpenOption.CREATE,StandardOpenOption.APPEND),"some stuff"));
    }

}
