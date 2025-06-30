import java.sql.*;
import java.util.Scanner;

public class Main {
public static String pad(String str, int length) {
        if (str == null) str = "";
        if (str.length() > length) return str.substring(0, length - 1) + "…";
        return str + " ".repeat(length - str.length());
    }

 public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String url = "jdbc:mysql://localhost:3306/BDS";
    String user = "root";
    String pass = "mysql";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pass);
// ADMIN EmpCODE = 101, PASSWORD = 34453
        while (true) {
            System.out.println("\nWelcome to Bug Tracking System");
            System.out.println("1. Admin");
            System.out.println("2. Manager");
            System.out.println("3. Employee");
            System.out.println("4. Exit");
            System.out.print("Choose your role: ");
            int choice = sc.nextInt();
            sc.nextLine();

            String role = "";
            if (choice == 1) role = "Admin";
            else if (choice == 2) role = "manager";
            else if (choice == 3) {
                System.out.print("Enter your role (developer or Tester): ");
                role = sc.nextLine();
            } else if (choice == 4) {
                System.out.println("Exiting system. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice.");
                continue;
            }

            System.out.print("Enter empCode: ");
            int empCode = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter empPassword: ");
            String password = sc.nextLine();

            String query = "SELECT * FROM Employee WHERE empCode=? AND empPassword=? AND Role=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, empCode);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login Successful. Welcome " + rs.getString("empName") + "!");

                if (role.equals("Admin")) {
                    int option = 0;
                    do {
                        System.out.println("\nAdmin Module");
                        System.out.println("1. Manager");
                        System.out.println("2. Employee");
                        System.out.println("3. View All Project");
                        System.out.println("4. View Bug's Reports");
                        System.out.println("5. Logout to Main Menu");
                        System.out.print("Enter your choice: ");
                        option = sc.nextInt();
                        sc.nextLine();

                        switch (option) {
                            case 1 -> handleManagerSection(con, sc);
                            case 2 -> handleEmployeeSection(con, sc);
                            case 3 -> {
                                System.out.println("All Projects:");
                                System.out.println("projectID  Name                          Start Date       End Date         Description");
                                System.out.println("===============================================================================================");
                                ResultSet pr = con.createStatement().executeQuery("SELECT * FROM Project");
                                while (pr.next()) {
                                    System.out.printf("%-11d%-30s%-17s%-17s%s\n",
                                            pr.getInt("projectID"),
                                            pr.getString("projectName"),
                                            pr.getString("SDate"),
                                            pr.getString("EDate"),
                                            pr.getString("projectDec"));
                                }
                            }
                            case 4 -> {
                                System.out.println("Bug Reports:");
                                System.out.println("BugNo   BugCode  ProjectID  TCode  ECode   Status             Description");
                                System.out.println("=============================================================================");
                                ResultSet br = con.createStatement().executeQuery("SELECT * FROM BugReport");
                                while (br.next()) {
                                    System.out.printf("%-8d%-9d%-11d%-7d%-8d%-19s%s\n",
                                            br.getInt("bugNo"),
                                            br.getInt("bugCode"),
                                            br.getInt("projectID"),
                                            br.getInt("TCode"),
                                            br.getInt("ECode"),
                                            br.getString("status"),
                                            br.getString("bugDes"));
                                }
                            }
                            case 5 -> System.out.println("Logging out from Admin Module...");
                            default -> System.out.println("Invalid option.");
                        }
                    } while (option != 5);

                } else if (role.equals("manager")) {
                    handleManagerModule(con, sc, empCode);
                } else if (role.equalsIgnoreCase("developer") || role.equalsIgnoreCase("Tester")) {
                    handleEmployeeModule(con, sc, empCode, role);
                } else {
                    System.out.println("Invalid employee role.");
                }

            } else {
                System.out.println("Login failed. Please check credentials.");
            }
        }

        con.close();
        sc.close();

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    public static void handleManagerSection(Connection con, Scanner sc) throws SQLException {
    int choice;
    do {
        System.out.println("\nManager Section");
        System.out.println("1. Add Manager Account");
        System.out.println("2. View Manager Account");
        System.out.println("3. Delete Manager");
        System.out.println("4. Update Manager Detail's");
        System.out.println("5. Back");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt(); sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter empCode: ");
                int code = sc.nextInt(); sc.nextLine();
                System.out.print("Enter name: ");
                String name = sc.nextLine();
                System.out.print("Enter email: ");
                String email = sc.nextLine();
                System.out.print("Enter password: ");
                String passw = sc.nextLine();
                System.out.print("Enter gender: ");
                String gender = sc.nextLine();
                System.out.print("Enter DOB: ");
                String dob = sc.nextLine();
                System.out.print("Enter mobile no: ");
                long mob = sc.nextLong(); sc.nextLine();

                String insert = "INSERT INTO Employee VALUES (?, ?, ?, ?, ?, ?, ?, 'manager')";
                PreparedStatement ins = con.prepareStatement(insert);
                ins.setInt(1, code);
                ins.setString(2, name);
                ins.setString(3, email);
                ins.setString(4, passw);
                ins.setString(5, gender);
                ins.setString(6, dob);
                ins.setLong(7, mob);
                ins.executeUpdate();
                System.out.println("Manager added successfully.");
            }
            case 2 -> {
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Employee WHERE Role='manager'");
                System.out.println("empCode  Name                         Email                              Password         Gender   DOB              Mobile        Role");
                System.out.println("=====================================================================================================================================");
                while (rs.next()) {
                    System.out.printf("%-9s%-28s%-34s%-17s%-9s%-16s%-13s%-10s\n",
                            rs.getInt("empCode"),
                            pad(rs.getString("empName"), 28),
                            pad(rs.getString("empEmail"), 34),
                            pad(rs.getString("empPassword"), 17),
                            pad(rs.getString("gender"), 9),
                            pad(rs.getString("DOB"), 16),
                            rs.getLong("mobileNo"),
                            pad(rs.getString("Role"), 10));
                }
            }
            case 3 -> {
                System.out.print("Enter empCode to delete: ");
                int del = sc.nextInt(); sc.nextLine();
                PreparedStatement delstmt = con.prepareStatement("DELETE FROM Employee WHERE empCode=? AND Role='manager'");
                delstmt.setInt(1, del);
                int rows = delstmt.executeUpdate();
                System.out.println(rows > 0 ? "Manager deleted." : "Manager not found.");
            }
            case 4 -> {
                System.out.print("Enter empCode to update: ");
                int up = sc.nextInt(); sc.nextLine();
                System.out.print("Enter new name: ");
                String name = sc.nextLine();
                System.out.print("Enter new email: ");
                String email = sc.nextLine();
                System.out.print("Enter new password: ");
                String pass = sc.nextLine();
                System.out.print("Enter new gender: ");
                String gender = sc.nextLine();
                System.out.print("Enter new DOB: ");
                String dob = sc.nextLine();
                System.out.print("Enter new mobile number: ");
                long mob = sc.nextLong(); sc.nextLine();

                PreparedStatement upd = con.prepareStatement("UPDATE Employee SET empName=?, empEmail=?, empPassword=?, gender=?, DOB=?, mobileNo=? WHERE empCode=? AND Role='manager'");
                upd.setString(1, name);
                upd.setString(2, email);
                upd.setString(3, pass);
                upd.setString(4, gender);
                upd.setString(5, dob);
                upd.setLong(6, mob);
                upd.setInt(7, up);
                int rows = upd.executeUpdate();
                System.out.println(rows > 0 ? "Manager updated." : "Manager not found.");
            }
            case 5 -> System.out.println("Returning to Admin Menu...");
            default -> System.out.println("Invalid option.");
        }
    } while (choice != 5);
}


    public static void handleEmployeeSection(Connection con, Scanner sc) throws SQLException {
    int choice;
    do {
        System.out.println("\nEmployee Section");
        System.out.println("1. Add Employee Account");
        System.out.println("2. View Employee's Account");
        System.out.println("3. Delete Employee Account");
        System.out.println("4. Update Employee Detail's");
        System.out.println("5. Back");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt(); sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter empCode: ");
                int code = sc.nextInt(); sc.nextLine();
                System.out.print("Enter name: ");
                String name = sc.nextLine();
                System.out.print("Enter email: ");
                String email = sc.nextLine();
                System.out.print("Enter password: ");
                String passw = sc.nextLine();
                System.out.print("Enter gender: ");
                String gender = sc.nextLine();
                System.out.print("Enter DOB: ");
                String dob = sc.nextLine();
                System.out.print("Enter mobile no: ");
                long mob = sc.nextLong(); sc.nextLine();
                System.out.print("Enter role (developer/Tester): ");
                String empRole = sc.nextLine();

                String insert = "INSERT INTO Employee VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ins = con.prepareStatement(insert);
                ins.setInt(1, code);
                ins.setString(2, name);
                ins.setString(3, email);
                ins.setString(4, passw);
                ins.setString(5, gender);
                ins.setString(6, dob);
                ins.setLong(7, mob);
                ins.setString(8, empRole);
                ins.executeUpdate();
                System.out.println("Employee added successfully.");
            }
            case 2 -> {
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Employee WHERE Role='developer' OR Role='Tester'");
                System.out.println("empCode  Name                         Email                              Password         Gender   DOB              Mobile        Role");
                System.out.println("=====================================================================================================================================");
                while (rs.next()) {
                    System.out.printf("%-9s%-28s%-34s%-17s%-9s%-16s%-13s%-10s\n",
                            rs.getInt("empCode"),
                            pad(rs.getString("empName"), 28),
                            pad(rs.getString("empEmail"), 34),
                            pad(rs.getString("empPassword"), 17),
                            pad(rs.getString("gender"), 9),
                            pad(rs.getString("DOB"), 16),
                            rs.getLong("mobileNo"),
                            pad(rs.getString("Role"), 10));
                }
            }
            case 3 -> {
                System.out.print("Enter empCode to delete: ");
                int del = sc.nextInt(); sc.nextLine();
                PreparedStatement delstmt = con.prepareStatement(
                        "DELETE FROM Employee WHERE empCode=? AND (Role='developer' OR Role='Tester')");
                delstmt.setInt(1, del);
                int rows = delstmt.executeUpdate();
                System.out.println(rows > 0 ? "Employee deleted." : "Employee not found.");
            }
            case 4 -> {
                System.out.print("Enter empCode to update: ");
                int up = sc.nextInt(); sc.nextLine();
                System.out.print("Enter new name: ");
                String name = sc.nextLine();
                System.out.print("Enter new email: ");
                String email = sc.nextLine();
                System.out.print("Enter new password: ");
                String pass = sc.nextLine();
                System.out.print("Enter  gender: ");
                String gender = sc.nextLine();
                System.out.print("Enter new DOB: ");
                String dob = sc.nextLine();
                System.out.print("Enter new mobile number: ");
                long mob = sc.nextLong(); sc.nextLine();
                System.out.print("Enter new role (developer/Tester): ");
                String role = sc.nextLine();

                PreparedStatement upd = con.prepareStatement(
                        "UPDATE Employee SET empName=?, empEmail=?, empPassword=?, gender=?, DOB=?, mobileNo=?, Role=? WHERE empCode=? AND (Role='developer' OR Role='Tester')");
                upd.setString(1, name);
                upd.setString(2, email);
                upd.setString(3, pass);
                upd.setString(4, gender);
                upd.setString(5, dob);
                upd.setLong(6, mob);
                upd.setString(7, role);
                upd.setInt(8, up);
                int rows = upd.executeUpdate();
                System.out.println(rows > 0 ? "Employee updated." : "Employee not found.");
            }
            case 5 -> System.out.println("Returning to Admin Menu...");
            default -> System.out.println("Invalid option.");
        }

    } while (choice != 5);
}

                public static void handleManagerModule(Connection con, Scanner sc, int empCode) throws SQLException {
    int choice;
    do {
        System.out.println("\nManager Module");
        System.out.println("1. Update Profile");
        System.out.println("2. Manage Project");
        System.out.println("3. Bug's");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt(); sc.nextLine();

        switch (choice) {
            case 1 -> {
    int updateChoice;
    do {
        System.out.println("\nUpdate Profile");
        System.out.println("1. Update Email");
        System.out.println("2. Update Password");
        System.out.println("3. Update Gender");
        System.out.println("4. Update DOB");
        System.out.println("5. Update Mobile No");
        System.out.println("6. Back");
        System.out.print("Enter your choice: ");
        updateChoice = sc.nextInt(); sc.nextLine();

        String updateQuery = "";
        String newValue = "";

        switch (updateChoice) {
            case 1 -> {
                System.out.print("Enter new email: ");
                newValue = sc.nextLine();
                updateQuery = "UPDATE Employee SET empEmail=? WHERE empCode=?";
            }
            case 2 -> {
                System.out.print("Enter new password: ");
                newValue = sc.nextLine();
                updateQuery = "UPDATE Employee SET empPassword=? WHERE empCode=?";
            }
            case 3 -> {
                System.out.print("Enter new gender: ");
                newValue = sc.nextLine();
                updateQuery = "UPDATE Employee SET gender=? WHERE empCode=?";
            }
            case 4 -> {
                System.out.print("Enter new DOB: ");
                newValue = sc.nextLine();
                updateQuery = "UPDATE Employee SET DOB=? WHERE empCode=?";
            }
            case 5 -> {
                System.out.print("Enter new mobile number: ");
                long mob = sc.nextLong(); sc.nextLine();
                PreparedStatement ps = con.prepareStatement("UPDATE Employee SET mobileNo=? WHERE empCode=?");
                ps.setLong(1, mob);
                ps.setInt(2, empCode);
                int rows = ps.executeUpdate();
                System.out.println(rows > 0 ? "Mobile number updated." : "Update failed.");
                continue;
            }
            case 6 -> {
                System.out.println("Returning to Manager Menu...");
                continue;
            }
            default -> {
                System.out.println("Invalid option.");
                continue;
            }
        }

        if (!updateQuery.isEmpty()) {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            ps.setString(1, newValue);
            ps.setInt(2, empCode);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Profile updated." : "Update failed.");
        }
    } while (updateChoice != 6);
}

            case 2 -> handleProjectSection(con, sc);
            case 3 -> handleBugSection(con, sc);
            case 4 -> System.out.println("Exiting Manager Module...");
            default -> System.out.println("Invalid choice.");
        }
    } while (choice != 4);
}

public static void handleProjectSection(Connection con, Scanner sc) throws SQLException {
    int choice;
    do {
        System.out.println("\nManage Project");
        System.out.println("1. Add Project");
        System.out.println("2. View All Projects");
        System.out.println("3. Delete Project");
        System.out.println("4. Update Project");
        System.out.println("5. Back");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt(); sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter projectID: ");
                int id = sc.nextInt(); sc.nextLine();
                System.out.print("Enter projectName: ");
                String name = sc.nextLine();
                System.out.print("Enter Start Date: ");
                String sdate = sc.nextLine();
                System.out.print("Enter End Date: ");
                String edate = sc.nextLine();
                System.out.print("Enter Description: ");
                String desc = sc.nextLine();

                String insert = "INSERT INTO Project VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(insert);
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, sdate);
                ps.setString(4, edate);
                ps.setString(5, desc);
                ps.executeUpdate();
                System.out.println("Project added successfully.");
            }
            case 2 -> {
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Project");
                System.out.println("projectID  Name                          Start Date    End Date      Description");
                System.out.println("=========================================================================================");
                while (rs.next()) {
                    System.out.printf("%-11d%-30s%-13s%-13s%s\n", rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                }
            }
            case 3 -> {
                System.out.print("Enter projectID to delete: ");
                int del = sc.nextInt(); sc.nextLine();
                PreparedStatement ps = con.prepareStatement("DELETE FROM Project WHERE projectID=?");
                ps.setInt(1, del);
                int rows = ps.executeUpdate();
                System.out.println(rows > 0 ? "Project deleted." : "Project not found.");
            }
            case 4 -> {
                System.out.print("Enter projectID to update: ");
int up = sc.nextInt(); sc.nextLine();

// Check if the project exists
PreparedStatement checkStmt = con.prepareStatement("SELECT * FROM Project WHERE projectID=?");
checkStmt.setInt(1, up);
ResultSet rs = checkStmt.executeQuery();

if (!rs.next()) {
    System.out.println("Project not found.");
} else {
    System.out.println("What would you like to update?");
    System.out.println("1. Project Name");
    System.out.println("2. Start Date");
    System.out.println("3. End Date");
    System.out.println("4. Description");
    System.out.print("Enter your choice: ");
    int updateChoice = sc.nextInt(); sc.nextLine();

    String field = "";
    String newValue = "";

    switch (updateChoice) {
        case 1 -> {
            field = "projectName";
            System.out.print("Enter new project name: ");
            newValue = sc.nextLine();
        }
        case 2 -> {
            field = "SDate";
            System.out.print("Enter new start date: ");
            newValue = sc.nextLine();
        }
        case 3 -> {
            field = "EDate";
            System.out.print("Enter new end date: ");
            newValue = sc.nextLine();
        }
        case 4 -> {
            field = "projectDec";
            System.out.print("Enter new description: ");
            newValue = sc.nextLine();
        }
        default -> {
            System.out.println("Invalid choice.");
            return;
        }
    }

    String updateSQL = "UPDATE Project SET " + field + "=? WHERE projectID=?";
    PreparedStatement updateStmt = con.prepareStatement(updateSQL);
    updateStmt.setString(1, newValue);
    updateStmt.setInt(2, up);
    int rows = updateStmt.executeUpdate();
    System.out.println(rows > 0 ? "Project updated successfully." : "Update failed.");
}

            }
            case 5 -> System.out.println("Returning to Manager Menu...");
            default -> System.out.println("Invalid choice.");
        }
    } while (choice != 5);
}

public static void handleBugSection(Connection con, Scanner sc) throws SQLException {
    int choice;
    do {
        System.out.println("\nBug's Section");
        System.out.println("1. Add New Bug");
        System.out.println("2. View All Bug’s");
        System.out.println("3. Update Bug");
        System.out.println("4. Delete Bug");
        System.out.println("5. Back");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt(); sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter bugCode: ");
                int code = sc.nextInt(); sc.nextLine();
                System.out.print("Enter bugCategory: ");
                String cat = sc.nextLine();
                System.out.print("Enter bugSeverity: ");
                String sev = sc.nextLine();
                PreparedStatement ps = con.prepareStatement("INSERT INTO BugType VALUES (?, ?, ?)");
                ps.setInt(1, code);
                ps.setString(2, cat);
                ps.setString(3, sev);
                ps.executeUpdate();
                System.out.println("Bug added.");
            }
            case 2 -> {
                ResultSet rs = con.createStatement().executeQuery("SELECT * FROM BugType");
                System.out.println("bugCode  Category                      Severity");
                System.out.println("=========================================================");
                while (rs.next()) {
                    System.out.printf("%-9d%-30s%-15s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
                }
            }
            case 3 -> {
    System.out.print("Enter bugCode to update: ");
    int up = sc.nextInt(); sc.nextLine();

    // Check if bugCode exists
    PreparedStatement check = con.prepareStatement("SELECT * FROM BugType WHERE bugCode=?");
    check.setInt(1, up);
    ResultSet rs = check.executeQuery();

    if (!rs.next()) {
        System.out.println("Bug not found.");
    } else {
        System.out.println("What would you like to update?");
        System.out.println("1. Bug Category");
        System.out.println("2. Bug Severity");
        System.out.print("Enter your choice: ");
        int updateChoice = sc.nextInt(); sc.nextLine();

        String field = "";
        String newValue = "";

        if (updateChoice == 1) {
            field = "bugCatgory";
            System.out.print("Enter new Category: ");
            newValue = sc.nextLine();
        } else if (updateChoice == 2) {
            field = "bugSeverity";
            System.out.print("Enter new Severity: ");
            newValue = sc.nextLine();
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        String updateSQL = "UPDATE BugType SET " + field + "=? WHERE bugCode=?";
        PreparedStatement ps = con.prepareStatement(updateSQL);
        ps.setString(1, newValue);
        ps.setInt(2, up);
        int rows = ps.executeUpdate();
        System.out.println(rows > 0 ? "Bug updated successfully." : "Update failed.");
    }
}

            case 4 -> {
                System.out.print("Enter bugCode to delete: ");
                int del = sc.nextInt(); sc.nextLine();
                PreparedStatement ps = con.prepareStatement("DELETE FROM BugType WHERE bugCode=?");
                ps.setInt(1, del);
                int rows = ps.executeUpdate();
                System.out.println(rows > 0 ? "Deleted." : "Bug not found.");
            }
            case 5 -> System.out.println("Returning to Manager Menu...");
            default -> System.out.println("Invalid choice.");
        }
    } while (choice != 5);
}


public static void handleEmployeeModule(Connection con, Scanner sc, int empCode, String role) throws SQLException {
    int choice;
    String cleanRole = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase(); // Proper header

    do {
        System.out.println("\n" + cleanRole + " Module");

        if (role.equalsIgnoreCase("Tester")) {
            System.out.println("1. Update Profile");
            System.out.println("2. Add Bug's Report");
            System.out.println("3. Update Bug status");
            System.out.println("4. View Bug's");
            System.out.println("5. Bug Details");
            System.out.println("6. Exit");
        } else {
            System.out.println("1. Update Profile");
            System.out.println("2. Update Bug status");
            System.out.println("3. View Bug's");
            System.out.println("4. Bug Details");
            System.out.println("5. Exit");
        }

        System.out.print("Enter your choice: ");
        choice = sc.nextInt(); sc.nextLine();

        if (choice == 1) {
            System.out.print("Enter new email: ");
            String newEmail = sc.nextLine();
            System.out.print("Enter new password: ");
            String newPassword = sc.nextLine();
            System.out.print("Enter gender: ");
            String gender = sc.nextLine();
            System.out.print("Enter DOB: ");
            String dob = sc.nextLine();
            System.out.print("Enter mobile number: ");
            long mob = sc.nextLong(); sc.nextLine();

            PreparedStatement ps = con.prepareStatement(
                "UPDATE Employee SET empEmail=?, empPassword=?, gender=?, DOB=?, mobileNo=? WHERE empCode=?");
            ps.setString(1, newEmail);
            ps.setString(2, newPassword);
            ps.setString(3, gender);
            ps.setString(4, dob);
            ps.setLong(5, mob);
            ps.setInt(6, empCode);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Profile updated." : "Update failed.");
        }

       else if (role.equalsIgnoreCase("Tester") && choice == 2) {
    System.out.print("Enter bugNo: ");
    int bugNo = sc.nextInt(); sc.nextLine();

    System.out.print("Enter bugCode: ");
    int bugCode = sc.nextInt(); sc.nextLine();

    System.out.print("Enter projectID: ");
    int projID = sc.nextInt(); sc.nextLine();

    System.out.print("Enter TCode (your empCode or another tester): ");
    int tcode = sc.nextInt(); sc.nextLine();

    System.out.print("Enter ECode : ");
    int ecode = sc.nextInt(); sc.nextLine();

    System.out.print("Enter bug status (pending/resolved): ");
    String status = sc.nextLine();

    System.out.print("Enter bug description: ");
    String desc = sc.nextLine();

    PreparedStatement ps = con.prepareStatement("INSERT INTO BugReport VALUES (?, ?, ?, ?, ?, ?, ?)");
    ps.setInt(1, bugNo);
    ps.setInt(2, bugCode);
    ps.setInt(3, projID);
    ps.setInt(4, tcode);   
    ps.setInt(5, ecode);   
    ps.setString(6, status);
    ps.setString(7, desc);
    ps.executeUpdate();
    System.out.println("Bug report added.");
}


        else if ((role.equalsIgnoreCase("Tester") && choice == 3) || (role.equalsIgnoreCase("developer") && choice == 2)) {
            System.out.print("Enter bugNo to update status: ");
            int bugNo = sc.nextInt(); sc.nextLine();
            System.out.print("Enter new status: ");
            String status = sc.nextLine();

            PreparedStatement ps = con.prepareStatement("UPDATE BugReport SET status=? WHERE bugNo=?");
            ps.setString(1, status);
            ps.setInt(2, bugNo);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Status updated." : "Bug not found.");
        }

        else if ((role.equalsIgnoreCase("Tester") && choice == 4) || (role.equalsIgnoreCase("developer") && choice == 3)) {
            String query = "SELECT * FROM BugReport WHERE " + 
                           (role.equalsIgnoreCase("Tester") ? "TCode=?" : "ECode=?");

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, empCode);
            ResultSet rs = ps.executeQuery();

            System.out.println("bugNo  bugCode  projectID  TCode  ECode  status       bugDesc");
            System.out.println("==========================================================================");
            while (rs.next()) {
                System.out.printf("%-7d%-9d%-11d%-7d%-7d%-13s%s\n",
                        rs.getInt("bugNo"),
                        rs.getInt("bugCode"),
                        rs.getInt("projectID"),
                        rs.getInt("TCode"),
                        rs.getInt("ECode"),
                        rs.getString("status"),
                        rs.getString("bugDes"));
            }
        }

        else if ((role.equalsIgnoreCase("Tester") && choice == 5) || (role.equalsIgnoreCase("developer") && choice == 4)) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM BugType");
            ResultSet rs = ps.executeQuery();

            System.out.println("bugCode  bugCategory                  bugSeverity");
            System.out.println("===============================================");
            while (rs.next()) {
                System.out.printf("%-9d%-30s%-20s\n",
                        rs.getInt("bugCode"),
                        rs.getString("bugCatgory"),
                        rs.getString("bugSeverty"));
            }
        }

    } while (!((role.equalsIgnoreCase("Tester") && choice == 6) || (role.equalsIgnoreCase("developer") && choice == 5)));
}


}
