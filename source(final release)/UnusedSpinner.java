/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package randomPicker;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
//TODO use Collections.shuffle() and poll() on remaining Names?
/**
 *
 * @author rande2
 */
public class UnusedSpinner {
    private List<String> allNames;
    private List<String> remainingNames;
    
    UnusedSpinner(List<String> n){
        setNames(n);
    }
    
    public List<String> getRemainingNames(){
        return remainingNames;
    }
    
    public List<String> getAllNames(){
        return allNames;
    }
    
    public final void setNames(List<String> n){
        allNames=n;
        try{
            remainingNames=allNames.getClass().getConstructor().newInstance();
            remainingNames.addAll(allNames);
        }catch(Exception ex){
            Logger.getLogger(UnusedSpinner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reset(){
        remainingNames.clear();
        remainingNames.addAll(allNames);
    }
    
    public String spin(){
        //limit: can have a maximum number of names of 2^31-1
        //only read from list when it contains items
        String name;
        if(!remainingNames.isEmpty()){
            int index = (int)(Math.random()*(remainingNames.size()));
            name = remainingNames.get(index);
            remainingNames.remove(index);
        }else{
            name = "No names left";
        }
        return name;
    }
}