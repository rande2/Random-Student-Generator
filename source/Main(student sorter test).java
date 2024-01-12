import java.util.LinkedList;
import java.util.Arrays;
import java.util.*;



public class Main {





    public static void main(String[] args) {
        Student st1 = new Student("Mohamad", "Halal");
        Student st2 = new Student("Mohamad", "Hall");
        Student st3 = new Student("Rowan", "A");
        Student st4 = new Student("Ing", "O");
        Scanner in = new Scanner(System.in);
        LinkedList<Student> students =
                new LinkedList<Student>(Arrays.asList(st1,st2,st3,st4));

        sort(students);

        System.out.println("What would you like to search for?");

        String searchTerm = in.next();

        int result = binarySearch(students, searchTerm);

        if(result == -1){
            System.out.println("Name not found");
        }else{
            System.out.println("Name found at index: " + result);
        }


    }

     public static int binarySearch(LinkedList<Student> ll, String x  ){
         int l = 0, r = ll.size() - 1;
         while(l<=r){
             int m = l + (r-1)/2;
             int res = x.compareTo(ll.get(m).getFirstName());

             // Check if x is present at mid
             if (res == 0)
                 return m;

             // If x greater, ignore left half
             if (res > 0)
                 l = m + 1;

                 // If x is smaller, ignore right half
             else
                 r = m - 1;
         }

         return -1;
     }
    public static void sort(LinkedList<Student> list) {

        list.sort((o1, o2)
                -> o1.getFirstName().compareTo(
                o2.getFirstName()));
    }
}


