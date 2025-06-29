package college.faculty;

public class Faculty {
    public String name;
    public int id;

    public Faculty(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void display() {
        System.out.println("Faculty Name: " + name + ", ID: " + id);
    }
}
