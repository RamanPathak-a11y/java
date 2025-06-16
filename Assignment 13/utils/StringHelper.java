package utils;

public class StringHelper {
    public static String reverse(String input) {
        String rev = "";
        for (int i = input.length() - 1; i >= 0; i--) {
            rev += input.charAt(i);
        }
        return rev;
    }

    public static boolean isPalindrome(String input) {
        String rev = reverse(input);
        return input.equalsIgnoreCase(rev);
    }
}
