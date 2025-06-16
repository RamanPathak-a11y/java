package college.students;

public class Student {
    public String name;
    public int id;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void display() {
        System.out.println("Student Name: " + name + ", ID: " + id);
    }
}
