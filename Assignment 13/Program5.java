import java.util.Scanner;
import utils.StringHelper;
import utils.MathHelper;
import utils.Validator;

public class Program5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        if (Validator.isValidName(name)) {
            System.out.println("Valid name.");
        } else {
            System.out.println("Name should start with uppercase letter.");
        }

        System.out.print("Enter a word to check palindrome: ");
        String word = sc.nextLine();
        if (StringHelper.isPalindrome(word)) {
            System.out.println("It's a palindrome.");
        } else {
            System.out.println("Not a palindrome.");
        }

        System.out.print("Enter a number to square and cube: ");
        int num = sc.nextInt();
        System.out.println("Square: " + MathHelper.square(num));
        System.out.println("Cube: " + MathHelper.cube(num));

        System.out.print("Enter two numbers to add:\nFirst: ");
        int a = sc.nextInt();
        System.out.print("Second: ");
        int b = sc.nextInt();
        System.out.println("Sum: " + MathHelper.add(a, b));
    }
}
