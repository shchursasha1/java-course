import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class C08_02 {
    
    public static void main(String[] args) {
        String filename = "input.txt";
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }
        
        System.out.println("Original lines:");
        for (String line : lines) {
            System.out.println(line);
        }
        
        Collections.sort(lines);
        
        System.out.println("\nSorted lines:");
        for (String line : lines) {
            System.out.println(line);
        }
    }
}

