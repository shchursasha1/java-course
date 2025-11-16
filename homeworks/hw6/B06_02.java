import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class B06_02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter text with phone numbers: ");
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            input.append(line).append("\n");
        }
        
        String text = input.toString();
        
        Pattern pattern = Pattern.compile(
            "\\+?\\d{1,3}[\\s-]?\\(?\\d{1,4}\\)?[\\s-]?\\d{1,4}[\\s-]?\\d{1,4}[\\s-]?\\d{1,9}"
        );
        
        Matcher matcher = pattern.matcher(text);
        
        System.out.println("\nFound phone numbers:");
        int count = 0;
        while (matcher.find()) {
            System.out.println(matcher.group());
            count++;
        }
        
        if (count == 0) {
            System.out.println("No phone numbers found.");
        } else {
            System.out.println("\nTotal found: " + count);
        }
        
        scanner.close();
    }
}

