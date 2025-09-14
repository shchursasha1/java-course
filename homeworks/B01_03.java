public class Task0103 {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No arguments");
            return;
        }
        int product = 1;
        for (String arg : args) {
            try {
                int num = Integer.parseInt(arg);
                product *= num;
            } catch (NumberFormatException e) {
                System.out.println("Arguments are not integers");
                return;
            }
        }
        System.out.println(product);
    }
}
