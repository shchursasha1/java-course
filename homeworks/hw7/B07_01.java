import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class B07_01 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("Creating file F with real numbers...");
            System.out.print("Enter count of numbers: ");
            int count = scanner.nextInt();
            
            double[] numbers = new double[count];
            System.out.println("Enter " + count + " real numbers:");
            for (int i = 0; i < count; i++) {
                numbers[i] = scanner.nextDouble();
            }
            
            createBinaryFile("F.dat", numbers);
            System.out.println("File F.dat created successfully.");
            
            double[] readNumbers = readBinaryFile("F.dat");
            System.out.println("\nNumbers from file F:");
            for (double num : readNumbers) {
                System.out.print(num + " ");
            }
            System.out.println();
            
            System.out.print("\nEnter threshold value a: ");
            double a = scanner.nextDouble();
            
            createFilteredFile("F.dat", "G.dat", a);
            System.out.println("File G.dat created with numbers greater than " + a);
            
            double[] filteredNumbers = readBinaryFile("G.dat");
            System.out.println("\nNumbers from file G:");
            for (double num : filteredNumbers) {
                System.out.print(num + " ");
            }
            System.out.println();
            
        } catch (IOException e) {
            System.err.println("Error working with files: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    public static void createBinaryFile(String filename, double[] numbers) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename))) {
            for (double num : numbers) {
                dos.writeDouble(num);
            }
        }
    }
    
    public static double[] readBinaryFile(String filename) throws IOException {
        List<Double> numbers = new ArrayList<>();
        
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filename))) {
            while (dis.available() > 0) {
                numbers.add(dis.readDouble());
            }
        }
        
        double[] result = new double[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            result[i] = numbers.get(i);
        }
        
        return result;
    }
    
    public static void createFilteredFile(String inputFile, String outputFile, double threshold) throws IOException {
        double[] numbers = readBinaryFile(inputFile);
        
        List<Double> filtered = new ArrayList<>();
        for (double num : numbers) {
            if (num > threshold) {
                filtered.add(num);
            }
        }
        
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
            for (double num : filtered) {
                dos.writeDouble(num);
            }
        }
    }
}

