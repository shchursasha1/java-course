import java.util.Stack;
import java.util.Scanner;

public class C08_01 {
    
    public static int reverseNumber(int number) {
        Stack<Integer> stack = new Stack<>();
        boolean isNegative = number < 0;
        number = Math.abs(number);
        
        while (number > 0) {
            stack.push(number % 10);
            number /= 10;
        }
        
        int reversed = 0;
        int multiplier = 1;
        
        while (!stack.isEmpty()) {
            reversed += stack.pop() * multiplier;
            multiplier *= 10;
        }
        
        return isNegative ? -reversed : reversed;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();
        
        int reversed = reverseNumber(number);
        
        System.out.println("Original number: " + number);
        System.out.println("Reversed number: " + reversed);
        
        scanner.close();
    }
}

