import java.util.Scanner;

public class Task0217 {
    public static double sumSinSeries(double x, double eps) {
        double sum = 0;
        double term = x;
        int k = 0;
        while (Math.abs(term) > eps) {
            sum += term;
            k++;
            term = Math.pow(-1, k) * Math.pow(x, 2 * k + 1) / factorial(2 * k + 1);
        }
        return sum;
    }

    private static long factorial(int n) {
        long res = 1;
        for (int i = 2; i <= n; i++) res *= i;
        return res;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double x = scanner.nextDouble();
        double eps = scanner.nextDouble();
        System.out.printf("%.10f\n", sumSinSeries(x, eps));
    }
}
