package college.admin;

public class Admin {
    public String name;
    public int id;

    public Admin(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void display() {
        System.out.println("Admin Name: " + name + ", ID: " + id);
    }
}
