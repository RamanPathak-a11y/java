import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

public class Program4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> items = new ArrayList<>();

        System.out.print("Enter number of items: ");
        int count = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < count; i++) {
            System.out.print("Enter item " + (i + 1) + ": ");
            String item = sc.nextLine();
            items.add(item);
        }

        System.out.println("\nItems Entered:");
        for (String item : items) {
            System.out.println("- " + item);
        }

        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = current.format(formatter);
        System.out.println("\nCurrent Date and Time: " + formattedDate);
    }
}
