import java.io.*;
import java.util.*;

/**
 * B12_01 - University Student Simulation using Visitor Design Pattern
 * 
 * This program models the student life cycle at a university including:
 * - Learning disciplines under teacher supervision
 * - Receiving scholarships and parental support
 * - Paying for hostel accommodation and canteen meals
 * - Checking if student graduates with a diploma
 */
public class B12_01 {
    
    // ===== Visitor Interface =====
    
    /**
     * Visitor interface for operations on students
     */
    interface StudentVisitor {
        void visitHumanitarianStudent(HumanitarianStudent student);
        void visitNaturalStudent(NaturalStudent student);
        void visitNaturalHumanitarianStudent(NaturalHumanitarianStudent student);
    }
    
    // ===== Student Classes =====
    
    /**
     * Abstract base class for all students
     */
    abstract static class Student {
        protected int credits = 0;
        protected int money;
        protected int requiredCredits;
        protected boolean expelled = false;
        
        public Student(int money, int requiredCredits) {
            this.money = money;
            this.requiredCredits = requiredCredits;
        }
        
        public abstract void accept(StudentVisitor visitor);
        
        public void addCredits(int credits) {
            this.credits += credits;
            System.out.printf("  Credits earned: +%d (Total: %d/%d)%n", 
                credits, this.credits, this.requiredCredits);
        }
        
        public void addMoney(int amount) {
            this.money += amount;
            System.out.printf("  Money received: +%d (Total: %d)%n", amount, this.money);
        }
        
        public boolean pay(int amount, String purpose) {
            if (money >= amount) {
                money -= amount;
                System.out.printf("  Paid %d for %s (Remaining: %d)%n", 
                    amount, purpose, this.money);
                return true;
            } else {
                System.out.printf("  INSUFFICIENT FUNDS! Need %d for %s, but only have %d%n", 
                    amount, purpose, this.money);
                expelled = true;
                System.out.println("  >>> STUDENT EXPELLED due to inability to pay! <<<");
                return false;
            }
        }
        
        public boolean hasEnoughCredits() {
            return credits >= requiredCredits;
        }
        
        public boolean isExpelled() {
            return expelled;
        }
        
        public int getCredits() {
            return credits;
        }
        
        public int getMoney() {
            return money;
        }
    }
    
    /**
     * Student of humanitarian direction
     */
    static class HumanitarianStudent extends Student {
        public HumanitarianStudent(int money, int requiredCredits) {
            super(money, requiredCredits);
        }
        
        @Override
        public void accept(StudentVisitor visitor) {
            visitor.visitHumanitarianStudent(this);
        }
    }
    
    /**
     * Student of natural sciences direction
     */
    static class NaturalStudent extends Student {
        public NaturalStudent(int money, int requiredCredits) {
            super(money, requiredCredits);
        }
        
        @Override
        public void accept(StudentVisitor visitor) {
            visitor.visitNaturalStudent(this);
        }
    }
    
    /**
     * Student of natural-humanitarian direction (can be taught by both types of teachers)
     */
    static class NaturalHumanitarianStudent extends Student {
        public NaturalHumanitarianStudent(int money, int requiredCredits) {
            super(money, requiredCredits);
        }
        
        @Override
        public void accept(StudentVisitor visitor) {
            visitor.visitNaturalHumanitarianStudent(this);
        }
    }
    
    // ===== Concrete Visitors =====
    
    /**
     * Teacher visitor - teaches disciplines
     */
    static abstract class Teacher implements StudentVisitor {
        protected int credits;
        protected String profile;
        
        public Teacher(int credits, String profile) {
            this.credits = credits;
            this.profile = profile;
        }
    }
    
    /**
     * Humanitarian teacher - can teach humanitarian and natural-humanitarian students
     */
    static class HumanitarianTeacher extends Teacher {
        public HumanitarianTeacher(int credits) {
            super(credits, "humanitarian");
        }
        
        @Override
        public void visitHumanitarianStudent(HumanitarianStudent student) {
            System.out.println("Action: Humanitarian teacher teaching humanitarian student");
            if (!student.isExpelled()) {
                student.addCredits(credits);
            }
        }
        
        @Override
        public void visitNaturalStudent(NaturalStudent student) {
            System.out.println("Action: Humanitarian teacher CANNOT teach natural sciences student");
            System.out.println("  >>> INCOMPATIBLE: Humanitarian teacher cannot teach natural student! <<<");
        }
        
        @Override
        public void visitNaturalHumanitarianStudent(NaturalHumanitarianStudent student) {
            System.out.println("Action: Humanitarian teacher teaching natural-humanitarian student");
            if (!student.isExpelled()) {
                student.addCredits(credits);
            }
        }
    }
    
    /**
     * Natural sciences teacher - can teach natural and natural-humanitarian students
     */
    static class NaturalTeacher extends Teacher {
        public NaturalTeacher(int credits) {
            super(credits, "natural");
        }
        
        @Override
        public void visitHumanitarianStudent(HumanitarianStudent student) {
            System.out.println("Action: Natural teacher CANNOT teach humanitarian student");
            System.out.println("  >>> INCOMPATIBLE: Natural teacher cannot teach humanitarian student! <<<");
        }
        
        @Override
        public void visitNaturalStudent(NaturalStudent student) {
            System.out.println("Action: Natural teacher teaching natural sciences student");
            if (!student.isExpelled()) {
                student.addCredits(credits);
            }
        }
        
        @Override
        public void visitNaturalHumanitarianStudent(NaturalHumanitarianStudent student) {
            System.out.println("Action: Natural teacher teaching natural-humanitarian student");
            if (!student.isExpelled()) {
                student.addCredits(credits);
            }
        }
    }
    
    /**
     * Accounting department - gives scholarships
     */
    static class Accounting implements StudentVisitor {
        private int amount;
        
        public Accounting(int amount) {
            this.amount = amount;
        }
        
        @Override
        public void visitHumanitarianStudent(HumanitarianStudent student) {
            processScholarship(student);
        }
        
        @Override
        public void visitNaturalStudent(NaturalStudent student) {
            processScholarship(student);
        }
        
        @Override
        public void visitNaturalHumanitarianStudent(NaturalHumanitarianStudent student) {
            processScholarship(student);
        }
        
        private void processScholarship(Student student) {
            System.out.println("Action: Accounting department providing scholarship");
            if (!student.isExpelled()) {
                student.addMoney(amount);
            }
        }
    }
    
    /**
     * Hostel management - collects payment for accommodation
     */
    static class HostelManagement implements StudentVisitor {
        private int amount;
        
        public HostelManagement(int amount) {
            this.amount = amount;
        }
        
        @Override
        public void visitHumanitarianStudent(HumanitarianStudent student) {
            processPayment(student);
        }
        
        @Override
        public void visitNaturalStudent(NaturalStudent student) {
            processPayment(student);
        }
        
        @Override
        public void visitNaturalHumanitarianStudent(NaturalHumanitarianStudent student) {
            processPayment(student);
        }
        
        private void processPayment(Student student) {
            System.out.println("Action: Paying for hostel accommodation");
            if (!student.isExpelled()) {
                student.pay(amount, "hostel");
            }
        }
    }
    
    /**
     * Canteen - collects payment for meals
     */
    static class Canteen implements StudentVisitor {
        private int amount;
        
        public Canteen(int amount) {
            this.amount = amount;
        }
        
        @Override
        public void visitHumanitarianStudent(HumanitarianStudent student) {
            processPayment(student);
        }
        
        @Override
        public void visitNaturalStudent(NaturalStudent student) {
            processPayment(student);
        }
        
        @Override
        public void visitNaturalHumanitarianStudent(NaturalHumanitarianStudent student) {
            processPayment(student);
        }
        
        private void processPayment(Student student) {
            System.out.println("Action: Paying for canteen meals");
            if (!student.isExpelled()) {
                student.pay(amount, "canteen");
            }
        }
    }
    
    /**
     * Parents - provide financial support
     */
    static class Parents implements StudentVisitor {
        private int amount;
        
        public Parents(int amount) {
            this.amount = amount;
        }
        
        @Override
        public void visitHumanitarianStudent(HumanitarianStudent student) {
            provideSupport(student);
        }
        
        @Override
        public void visitNaturalStudent(NaturalStudent student) {
            provideSupport(student);
        }
        
        @Override
        public void visitNaturalHumanitarianStudent(NaturalHumanitarianStudent student) {
            provideSupport(student);
        }
        
        private void provideSupport(Student student) {
            System.out.println("Action: Parents providing financial support");
            if (!student.isExpelled()) {
                student.addMoney(amount);
            }
        }
    }
    
    // ===== Simulation Engine =====
    
    /**
     * Simulates student life based on input file
     */
    static class StudentSimulator {
        private Student student;
        private List<String> log = new ArrayList<>();
        
        public StudentSimulator(String filename) throws IOException {
            processFile(filename);
        }
        
        private void processFile(String filename) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            // Read student direction
            String direction = reader.readLine().trim();
            
            // Read required credits
            int requiredCredits = Integer.parseInt(reader.readLine().trim());
            
            // Read initial money
            int initialMoney = Integer.parseInt(reader.readLine().trim());
            
            // Create appropriate student type
            student = createStudent(direction, initialMoney, requiredCredits);
            
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║        STUDENT LIFE SIMULATION - VISITOR PATTERN          ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            System.out.printf("%nStudent Direction: %s%n", direction);
            System.out.printf("Required Credits: %d%n", requiredCredits);
            System.out.printf("Initial Money: %d%n%n", initialMoney);
            System.out.println("─────────────────────────────────────────────────────────────");
            
            // Process actions
            int stepNumber = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                System.out.printf("%n[Step %d] ", stepNumber++);
                processAction(line);
                
                // Check if student was expelled
                if (student.isExpelled()) {
                    System.out.println("\n─────────────────────────────────────────────────────────────");
                    System.out.println("Simulation terminated: Student expelled!");
                    break;
                }
            }
            
            reader.close();
            
            // Final result
            printFinalResult();
        }
        
        private Student createStudent(String direction, int money, int credits) {
            switch (direction.toLowerCase()) {
                case "humanitarian":
                    return new HumanitarianStudent(money, credits);
                case "natural":
                    return new NaturalStudent(money, credits);
                case "natural-humanitarian":
                    return new NaturalHumanitarianStudent(money, credits);
                default:
                    throw new IllegalArgumentException("Unknown student direction: " + direction);
            }
        }
        
        private void processAction(String line) {
            String[] parts = line.split("\\s+");
            String action = parts[0];
            String subAction = parts[1];
            
            switch (action) {
                case "teach":
                    int credits = Integer.parseInt(parts[2]);
                    if (subAction.equals("humanitarian")) {
                        student.accept(new HumanitarianTeacher(credits));
                    } else if (subAction.equals("natural")) {
                        student.accept(new NaturalTeacher(credits));
                    }
                    break;
                    
                case "obtain":
                    int amount = Integer.parseInt(parts[2]);
                    if (subAction.equals("scholarship")) {
                        student.accept(new Accounting(amount));
                    } else if (subAction.equals("help")) {
                        student.accept(new Parents(amount));
                    }
                    break;
                    
                case "pay":
                    int payment = Integer.parseInt(parts[2]);
                    if (subAction.equals("hostel")) {
                        student.accept(new HostelManagement(payment));
                    } else if (subAction.equals("canteen")) {
                        student.accept(new Canteen(payment));
                    }
                    break;
                    
                default:
                    System.out.println("Unknown action: " + action);
            }
        }
        
        private void printFinalResult() {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║                      FINAL RESULT                          ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            
            System.out.printf("%nFinal Statistics:%n");
            System.out.printf("  Credits earned: %d / %d required%n", 
                student.getCredits(), student.requiredCredits);
            System.out.printf("  Money remaining: %d%n", student.getMoney());
            System.out.printf("  Status: %s%n%n", 
                student.isExpelled() ? "EXPELLED" : "ACTIVE");
            
            boolean graduated = !student.isExpelled() && student.hasEnoughCredits();
            
            if (graduated) {
                System.out.println("╔════════════════════════════════════════════════════════════╗");
                System.out.println("║        ★★★ CONGRATULATIONS! DIPLOMA AWARDED! ★★★          ║");
                System.out.println("╚════════════════════════════════════════════════════════════╝");
                System.out.println("\nThe student successfully completed all requirements and");
                System.out.println("received their diploma!");
            } else if (student.isExpelled()) {
                System.out.println("╔════════════════════════════════════════════════════════════╗");
                System.out.println("║              ✗✗✗ NO DIPLOMA - EXPELLED ✗✗✗                ║");
                System.out.println("╚════════════════════════════════════════════════════════════╝");
                System.out.println("\nThe student was expelled due to inability to pay for");
                System.out.println("accommodation or meals.");
            } else {
                System.out.println("╔════════════════════════════════════════════════════════════╗");
                System.out.println("║         ✗✗✗ NO DIPLOMA - INSUFFICIENT CREDITS ✗✗✗         ║");
                System.out.println("╚════════════════════════════════════════════════════════════╝");
                System.out.printf("%nThe student needs %d more credits to graduate.%n", 
                    student.requiredCredits - student.getCredits());
            }
        }
    }
    
    // ===== Main Method =====
    
    /**
     * Main method - runs simulation for input files
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java B12_01 <input_file>");
            System.out.println("Example: java B12_01 input01.txt");
            return;
        }
        
        String filename = args[0];
        
        try {
            StudentSimulator simulator = new StudentSimulator(filename);
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format in file");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

