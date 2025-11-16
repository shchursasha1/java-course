import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class B06_04 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter text with floating point numbers: ");
        StringBuilder input = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            input.append(line).append("\n");
        }
        
        String text = input.toString();
        
        Pattern pattern = Pattern.compile("[+\\-]?(\\d+\\.\\d+|\\d+\\.|\\.\\d+)");
        Matcher matcher = pattern.matcher(text);
        
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String number = matcher.group();
            String fixed = fixNumber(number);
            matcher.appendReplacement(result, Matcher.quoteReplacement(fixed));
        }
        matcher.appendTail(result);
        
        System.out.println("\nResult:");
        System.out.println(result.toString());
        
        scanner.close();
    }
    
    public static String fixNumber(String number) {
        String sign = "";
        String num = number;
        
        if (num.startsWith("+") || num.startsWith("-")) {
            sign = num.substring(0, 1);
            num = num.substring(1);
        }
        
        if (num.startsWith(".")) {
            num = "0" + num;
        }
        
        if (num.endsWith(".")) {
            num = num + "0";
        }
        
        return sign + num;
    }
}

