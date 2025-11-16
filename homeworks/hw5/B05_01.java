import java.util.Scanner;

public class B05_01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String input = scanner.nextLine();
        scanner.close();

        try {
            String result = removeTextInParentheses(input);
            System.out.println("Result: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String removeTextInParentheses(String str) {
        if (!areBracketsValid(str)) {
            throw new IllegalArgumentException("Invalid brackets arrangement");
        }

        StringBuilder result = new StringBuilder();
        boolean insideBrackets = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') {
                insideBrackets = true;
            } else if (c == ')') {
                insideBrackets = false;
            } else if (!insideBrackets) {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static boolean areBracketsValid(String str) {
        int openCount = 0;
        int depth = 0;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '(') {
                depth++;
                if (depth > 1) {
                    return false;
                }
                openCount++;
            } else if (c == ')') {
                if (depth == 0) {
                    return false;
                }
                depth--;
            }
        }

        return depth == 0;
    }
}

