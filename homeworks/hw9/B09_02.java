import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class B09_02 {
    
    static class Hotel {
        private final int totalRooms;
        private final Semaphore availableRooms;
        private final List<Long> waitingTimes;
        private final Random random;
        
        public Hotel(int totalRooms) {
            this.totalRooms = totalRooms;
            this.availableRooms = new Semaphore(totalRooms, true);
            this.waitingTimes = new ArrayList<>();
            this.random = new Random();
        }
        
        public void checkIn(int clientId, long arrivalTime) throws InterruptedException {
            System.out.println("Client " + clientId + " arrived and looking for a room");
            
            long startWaiting = System.currentTimeMillis();
            availableRooms.acquire();
            long endWaiting = System.currentTimeMillis();
            
            long waitTime = endWaiting - startWaiting;
            synchronized (waitingTimes) {
                waitingTimes.add(waitTime);
            }
            
            System.out.println("Client " + clientId + " checked in (waited " + waitTime + " ms)");
        }
        
        public void checkOut(int clientId) {
            availableRooms.release();
            System.out.println("Client " + clientId + " checked out");
        }
        
        public double getAverageWaitingTime() {
            synchronized (waitingTimes) {
                if (waitingTimes.isEmpty()) {
                    return 0.0;
                }
                long sum = 0;
                for (long time : waitingTimes) {
                    sum += time;
                }
                return (double) sum / waitingTimes.size();
            }
        }
        
        public int getTotalClients() {
            synchronized (waitingTimes) {
                return waitingTimes.size();
            }
        }
    }
    
    static class Client extends Thread {
        private final int clientId;
        private final Hotel hotel;
        private final long arrivalTime;
        private final long stayDuration;
        
        public Client(int clientId, Hotel hotel, long arrivalTime, long stayDuration) {
            this.clientId = clientId;
            this.hotel = hotel;
            this.arrivalTime = arrivalTime;
            this.stayDuration = stayDuration;
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(arrivalTime);
                
                hotel.checkIn(clientId, arrivalTime);
                
                Thread.sleep(stayDuration);
                
                hotel.checkOut(clientId);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        final int TOTAL_ROOMS = 5;
        final int TOTAL_CLIENTS = 20;
        final long MIN_ARRIVAL_INTERVAL = 100;
        final long MAX_ARRIVAL_INTERVAL = 500;
        final long MIN_STAY_DURATION = 1000;
        final long MAX_STAY_DURATION = 3000;
        
        Hotel hotel = new Hotel(TOTAL_ROOMS);
        Random random = new Random();
        List<Client> clients = new ArrayList<>();
        
        System.out.println("Hotel simulation started");
        System.out.println("Total rooms: " + TOTAL_ROOMS);
        System.out.println("Total clients: " + TOTAL_CLIENTS);
        System.out.println("Arrival interval: " + MIN_ARRIVAL_INTERVAL + "-" + MAX_ARRIVAL_INTERVAL + " ms");
        System.out.println("Stay duration: " + MIN_STAY_DURATION + "-" + MAX_STAY_DURATION + " ms");
        System.out.println("-------------------------------------------\n");
        
        long cumulativeArrivalTime = 0;
        
        for (int i = 1; i <= TOTAL_CLIENTS; i++) {
            long arrivalInterval = MIN_ARRIVAL_INTERVAL + 
                (long) (random.nextDouble() * (MAX_ARRIVAL_INTERVAL - MIN_ARRIVAL_INTERVAL));
            
            long stayDuration = MIN_STAY_DURATION + 
                (long) (random.nextDouble() * (MAX_STAY_DURATION - MIN_STAY_DURATION));
            
            cumulativeArrivalTime += arrivalInterval;
            
            Client client = new Client(i, hotel, cumulativeArrivalTime, stayDuration);
            clients.add(client);
            client.start();
        }
        
        for (Client client : clients) {
            client.join();
        }
        
        System.out.println("\n-------------------------------------------");
        System.out.println("Hotel simulation completed");
        System.out.println("Total clients served: " + hotel.getTotalClients());
        System.out.printf("Average waiting time: %.2f ms\n", hotel.getAverageWaitingTime());
    }
}

