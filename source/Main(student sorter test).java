import java.util.LinkedList;
import java.util.Arrays;
import java.util.*;



public class Main {





    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        ArrayList<Student> students =
                new ArrayList<>(30);


        Student st1 = new Student("Mohamad", "Halal");
        Student st2 = new Student("Mohamad", "H");
        Student st3 = new Student("Rowan", "A");
        Student st4 = new Student("Ing", "O");

        students.add(st1);
        students.add(st2);
        students.add(st3);
        students.add(st4);

        sort(students);


        printList(students);

        System.out.println("First name");

        String searchTerm = in.next();


            System.out.println("Last Name");
        String ln = in.next();

        int result = binarySearch(students, searchTerm,ln);

        if(result == -1){
            System.out.println("Name not found");
        }else{
            System.out.println("Name found at index: " + result);
        }


    }

     public static int binarySearch(ArrayList<Student> ll, String x, String ln){
         int l = 0, r = ll.size() - 1;
         while(l<=r){
             int m = l + (r-1)/2;

             int resLn = ln.compareTo(ll.get(m).getLastName());
             int resFn = x.compareTo(ll.get(m).getFirstName());


             // Check if x is present at mid
             if (resLn == 0 && resFn == 0)
                 return m;

             // If x greater, ignore left half
             if (resLn > 0 && resFn > 0)
                 l = m + 1;

                 // If x is smaller, ignore right half
             else
                 r = m - 1;
         }

         return -1;
     }
    public static void sort(ArrayList<Student> list) {

        list.sort((o1, o2)
                -> o1.getFirstName().compareTo(
                o2.getFirstName()));
    }


    public static void printList(ArrayList<Student> list){
        for(Student student: list){
            System.out.println(student.getFirstName() + "  " + student.getLastName());
        }
    }


}


