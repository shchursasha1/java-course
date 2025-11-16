import java.util.Scanner;

public class B05_02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string (letters and digits only): ");
        String input = scanner.nextLine();
        scanner.close();

        System.out.println("\nProperty a: " + checkPropertyA(input));
        System.out.println("Property b: " + checkPropertyB(input));
        System.out.println("Property c: " + checkPropertyC(input));
    }

    public static boolean checkPropertyA(String str) {
        if (str.isEmpty()) {
            return false;
        }

        char firstChar = str.charAt(0);
        if (!Character.isDigit(firstChar) || firstChar == '0') {
            return false;
        }

        int digitValue = Character.getNumericValue(firstChar);
        String remaining = str.substring(1);

        if (remaining.length() != digitValue) {
            return false;
        }

        for (char c : remaining.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkPropertyB(String str) {
        if (str.isEmpty()) {
            return false;
        }

        int digitCount = 0;
        int digitValue = -1;

        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
                digitValue = Character.getNumericValue(c);
            } else if (!Character.isLetter(c)) {
                return false;
            }
        }

        return digitCount == 1 && digitValue == str.length();
    }

    public static boolean checkPropertyC(String str) {
        if (str.isEmpty()) {
            return false;
        }

        int digitSum = 0;

        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                digitSum += Character.getNumericValue(c);
            } else if (!Character.isLetter(c)) {
                return false;
            }
        }

        return digitSum == str.length();
    }
}

