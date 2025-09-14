import java.util.Scanner;

public class Task0105 {
    public static void main(String[] args) {
        int N, M;
        if (args.length == 2) {
            try {
                N = Integer.parseInt(args[0]);
                M = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Arguments must be integers");
                return;
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            N = scanner.nextInt();
            M = scanner.nextInt();
        }
        if (N <= 0 || M <= 0) {
            System.out.println("Numbers must be positive");
            return;
        }
        for (int i = 0; i < N; i++) {
            System.out.println((int) (Math.random() * M));
        }
    }
}
