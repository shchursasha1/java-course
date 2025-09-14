import java.util.Scanner;

public class Task0104 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        double result = Math.cbrt(a * b * c);
        System.out.printf("%.4f\n", result);
    }
}
