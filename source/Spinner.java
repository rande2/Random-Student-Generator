/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package randomPicker;
import java.util.List;
/**
 *
 * @author rande2
 */
public class Spinner {
    private List<String> allNames;
    private List<String> remainingNames;
    
    Spinner(List<String> n){
        allNames=n;
        remainingNames=n;
    }
    
    public List<String> getRemainingNames(){
        return remainingNames;
    }
    
    public List<String> getAllNames(){
        return allNames;
    }
    
    public void setNames(List<String> n){
        allNames=n;
        remainingNames=n;
    }
    
    public void reset(){
        remainingNames=allNames;
    }
    
    public String spin(){
        //limit: can have a maximum number of names of 2^31-1
        int numNames = remainingNames.size();
        //only read from list when it contains items
        if(numNames>0){
            int index = (int)(Math.random()*(remainingNames.size()));
            String name = remainingNames.get(index);
            remainingNames.remove(index);
            return name;
        }else{
            return null;
        }
    }
}
