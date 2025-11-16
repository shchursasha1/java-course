import java.util.Scanner;
import java.util.regex.Pattern;

public class B06_03 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter arithmetic expression: ");
        String expression = scanner.nextLine();
        
        boolean isValid = checkSyntax(expression);
        
        if (isValid) {
            System.out.println("Expression is syntactically correct.");
        } else {
            System.out.println("Expression is syntactically incorrect.");
        }
        
        scanner.close();
    }
    
    public static boolean checkSyntax(String expression) {
        expression = expression.trim();
        
        if (expression.isEmpty()) {
            return false;
        }
        
        String pattern = "^\\s*[+\\-]?\\s*\\d+\\s*([+\\-*/]\\s*[+\\-]?\\s*\\d+\\s*)*$";
        
        return Pattern.matches(pattern, expression);
    }
}

