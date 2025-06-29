package utils;

public class Validator {
    public static boolean isNumber(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public static boolean isValidName(String name) {
        return name.length() > 0 && Character.isUpperCase(name.charAt(0));
    }
}
