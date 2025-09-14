import java.util.Scanner;

public class Task0211 {
    public static int removeBit(int num, int i) {
        int lower = num & ((1 << i) - 1);
        int higher = (num >> (i + 1)) << i;
        return higher | lower;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        int i = scanner.nextInt();
        System.out.println(removeBit(num, i));
    }
}
