import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Task0314 {
    // Subscriber class
    static class Subscriber {
        private String fullName;
        private double credit;
        private double localCallTime;
        private double longDistanceCallTime;
        
        public Subscriber(String fullName, double credit, double localCallTime, double longDistanceCallTime) {
            this.fullName = fullName;
            this.credit = credit;
            this.localCallTime = localCallTime;
            this.longDistanceCallTime = longDistanceCallTime;
        }
        
        // Getters
        public String getFullName() {
            return fullName;
        }
        
        public double getCredit() {
            return credit;
        }
        
        public double getLocalCallTime() {
            return localCallTime;
        }
        
        public double getLongDistanceCallTime() {
            return longDistanceCallTime;
        }
        
        // String representation
        @Override
        public String toString() {
            return String.format("Subscriber{name='%s', credit=%.2f, localTime=%.2f, longDistanceTime=%.2f}", 
                                fullName, credit, localCallTime, longDistanceCallTime);
        }
    }
    
    // Method that returns array of subscribers sorted by credit where local call time exceeds limit
    public static Subscriber[] getSubscribersExceedingLocalLimit(Subscriber[] subscribers, double localTimeLimit) {
        List<Subscriber> filtered = new ArrayList<>();
        
        // Filter subscribers whose local call time exceeds the limit
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getLocalCallTime() > localTimeLimit) {
                filtered.add(subscriber);
            }
        }
        
        // Convert to array
        Subscriber[] result = filtered.toArray(new Subscriber[0]);
        
        // Sort by credit
        Arrays.sort(result, Comparator.comparingDouble(Subscriber::getCredit));
        
        return result;
    }
    
    // Method that returns array of subscribers who used long distance calls
    public static Subscriber[] getSubscribersWithLongDistanceCalls(Subscriber[] subscribers) {
        List<Subscriber> filtered = new ArrayList<>();
        
        // Filter subscribers who used long distance calls (time > 0)
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getLongDistanceCallTime() > 0) {
                filtered.add(subscriber);
            }
        }
        
        return filtered.toArray(new Subscriber[0]);
    }
    
    public static void main(String[] args) {
        // Create array of subscribers
        Subscriber[] subscribers = {
            new Subscriber("Іванов Іван Іванович", 150.50, 120.5, 0),
            new Subscriber("Петренко Петро Петрович", 200.75, 200.3, 45.2),
            new Subscriber("Сидоров Сидір Сидорович", 75.25, 95.7, 0),
            new Subscriber("Коваленко Олександр Михайлович", 300.00, 180.4, 15.8),
            new Subscriber("Шевченко Марія Василівна", 120.30, 250.9, 30.5),
            new Subscriber("Мельник Анна Сергіївна", 180.80, 80.2, 0),
            new Subscriber("Бондаренко Володимир Олексійович", 90.15, 160.1, 25.3)
        };
        
        System.out.println("All subscribers:");
        for (int i = 0; i < subscribers.length; i++) {
            System.out.println((i + 1) + ". " + subscribers[i]);
        }
        
        // Test method a) - subscribers sorted by credit where local call time exceeds limit
        double localTimeLimit = 150.0; // Set limit for local call time
        System.out.println("\na) Subscribers sorted by credit where local call time exceeds " + localTimeLimit + " minutes:");
        Subscriber[] exceedingLimit = getSubscribersExceedingLocalLimit(subscribers, localTimeLimit);
        
        if (exceedingLimit.length == 0) {
            System.out.println("No subscribers found exceeding the local call time limit.");
        } else {
            for (int i = 0; i < exceedingLimit.length; i++) {
                System.out.println((i + 1) + ". " + exceedingLimit[i]);
            }
        }
        
        // Test method b) - subscribers who used long distance calls
        System.out.println("\nb) Subscribers who used long distance calls:");
        Subscriber[] longDistanceUsers = getSubscribersWithLongDistanceCalls(subscribers);
        
        if (longDistanceUsers.length == 0) {
            System.out.println("No subscribers found who used long distance calls.");
        } else {
            for (int i = 0; i < longDistanceUsers.length; i++) {
                System.out.println((i + 1) + ". " + longDistanceUsers[i]);
            }
        }
    }
}