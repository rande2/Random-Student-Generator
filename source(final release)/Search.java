/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package randomPicker;

import java.util.List;

/**
 *
 * @author Rowan
 */
public class Search {
    public static int search(String name,List<String> classes) {
        //linear search to avoid having to sort the list of names each time one is entered
        int index = -1;
        int size = classes.size();
        //for each name
        for (int i = 0; i < size; i++) {
            //if it matches, return the index
            if (classes.get(i).equals(name)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
