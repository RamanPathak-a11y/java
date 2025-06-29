import college.students.Student;
import college.faculty.Faculty;
import college.admin.Admin;
import java.util.Scanner;

public class Program3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Student Name and ID: ");
        Student s = new Student(sc.next(), sc.nextInt());

        System.out.print("Enter Faculty Name and ID: ");
        Faculty f = new Faculty(sc.next(), sc.nextInt());

        System.out.print("Enter Admin Name and ID: ");
        Admin a = new Admin(sc.next(), sc.nextInt());

        s.display();
        f.display();
        a.display();
    }
}
