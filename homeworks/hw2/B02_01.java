import java.util.Scanner;

public class Task0201 {
    public static double findMin(double[] arr) {
        double min = arr[0];
        for (double v : arr) {
            if (v < min) min = v;
        }
        return min;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextDouble();
        }
        System.out.println(findMin(arr));
    }
}
