import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class B06_01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter text with dates (DD.MM.YYYY) or underscores (__.__.____): ");
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            input.append(line).append("\n");
        }
        
        String text = input.toString();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = currentDate.format(formatter);
        
        Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})|(__\\.__\\.____)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        
        String result = matcher.replaceAll(formattedDate);
        
        System.out.println("\nResult:");
        System.out.println(result);
        
        scanner.close();
    }
}

