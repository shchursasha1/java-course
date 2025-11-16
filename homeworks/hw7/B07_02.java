import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Toy implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private double price;
    private int minAge;
    private int maxAge;
    
    public Toy(String name, double price, int minAge, int maxAge) {
        this.name = name;
        this.price = price;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getMinAge() {
        return minAge;
    }
    
    public int getMaxAge() {
        return maxAge;
    }
    
    public boolean isSuitableForAge(int age) {
        return age >= minAge && age <= maxAge;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%.2f UAH, age %d-%d)", name, price, minAge, maxAge);
    }
}

public class B07_02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.println("Creating toys file...");
            System.out.print("Enter number of toys: ");
            int count = scanner.nextInt();
            scanner.nextLine();
            
            List<Toy> toys = new ArrayList<>();
            
            for (int i = 0; i < count; i++) {
                System.out.println("\nToy " + (i + 1) + ":");
                System.out.print("Name: ");
                String name = scanner.nextLine();
                
                System.out.print("Price (UAH): ");
                double price = scanner.nextDouble();
                
                System.out.print("Min age: ");
                int minAge = scanner.nextInt();
                
                System.out.print("Max age: ");
                int maxAge = scanner.nextInt();
                scanner.nextLine();
                
                toys.add(new Toy(name, price, minAge, maxAge));
            }
            
            createToysFile("toys.dat", toys);
            System.out.println("\nFile toys.dat created successfully.");
            
            List<Toy> readToys = readToysFile("toys.dat");
            System.out.println("\nToys from file:");
            for (Toy toy : readToys) {
                System.out.println(toy);
            }
            
            System.out.print("\nEnter child's age: ");
            int childAge = scanner.nextInt();
            
            List<Toy> suitableToys = new ArrayList<>();
            for (Toy toy : readToys) {
                if (toy.isSuitableForAge(childAge)) {
                    suitableToys.add(toy);
                }
            }
            
            createToysFile("suitable_toys.dat", suitableToys);
            System.out.println("\nSuitable toys for age " + childAge + ":");
            for (Toy toy : suitableToys) {
                System.out.println(toy);
            }
            
            System.out.println("\nFile suitable_toys.dat created successfully.");
            
        } catch (IOException e) {
            System.err.println("Error working with files: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error reading objects: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    public static void createToysFile(String filename, List<Toy> toys) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(toys);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Toy> readToysFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Toy>) ois.readObject();
        }
    }
}

