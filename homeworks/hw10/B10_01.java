import java.io.*;
import java.net.*;
import java.util.*;

/**
 * B10_01 - Client-Server application for finding min/max values in integer sequences
 * 
 * The client sends strings containing sequences of integers to the server.
 * The server processes each string, finds the maximum and minimum numbers,
 * and returns them to the client.
 */
public class B10_01 {
    
    // Server implementation
    static class MinMaxServer {
        private static final int PORT = 5000;
        private static final int MAX_CLIENTS = 5;
        
        /**
         * Starts the server and listens for client connections
         */
        public void start() {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server started on port " + PORT);
                System.out.println("Waiting for clients...");
                
                // Handle multiple clients
                int clientCount = 0;
                while (clientCount < MAX_CLIENTS) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("\nClient connected: " + clientSocket.getInetAddress());
                    
                    // Handle client in a separate thread
                    Thread clientThread = new Thread(() -> handleClient(clientSocket));
                    clientThread.start();
                    
                    clientCount++;
                }
            } catch (IOException e) {
                System.err.println("Server error: " + e.getMessage());
            }
        }
        
        /**
         * Handles communication with a single client
         * @param clientSocket the socket connected to the client
         */
        private void handleClient(Socket clientSocket) {
            try (
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String inputLine;
                
                // Read lines from client until connection closes
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    
                    // Check for termination signal
                    if (inputLine.equalsIgnoreCase("EXIT")) {
                        System.out.println("Client requested to close connection");
                        break;
                    }
                    
                    // Process the line and find min/max
                    String result = processNumberSequence(inputLine);
                    
                    // Send result back to client
                    out.println(result);
                    System.out.println("Sent to client: " + result);
                }
                
                System.out.println("Client disconnected");
                
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
        
        /**
         * Processes a string containing integers and finds min/max values
         * @param line string containing space-separated integers
         * @return string with min and max values or error message
         */
        private String processNumberSequence(String line) {
            try {
                // Parse integers from the input string
                String[] tokens = line.trim().split("\\s+");
                
                if (tokens.length == 0 || (tokens.length == 1 && tokens[0].isEmpty())) {
                    return "ERROR: Empty sequence";
                }
                
                List<Integer> numbers = new ArrayList<>();
                
                // Parse each token as an integer
                for (String token : tokens) {
                    try {
                        numbers.add(Integer.parseInt(token));
                    } catch (NumberFormatException e) {
                        return "ERROR: Invalid number format: " + token;
                    }
                }
                
                if (numbers.isEmpty()) {
                    return "ERROR: No valid numbers found";
                }
                
                // Find min and max
                int min = Collections.min(numbers);
                int max = Collections.max(numbers);
                
                return "MIN: " + min + ", MAX: " + max;
                
            } catch (Exception e) {
                return "ERROR: " + e.getMessage();
            }
        }
    }
    
    // Client implementation
    static class MinMaxClient {
        private static final String SERVER_ADDRESS = "localhost";
        private static final int SERVER_PORT = 5000;
        
        /**
         * Connects to the server and sends number sequences
         */
        public void start() {
            try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                BufferedReader userInput = new BufferedReader(
                    new InputStreamReader(System.in))
            ) {
                System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
                System.out.println("Enter sequences of integers (space-separated)");
                System.out.println("Type 'EXIT' to quit\n");
                
                String line;
                while (true) {
                    System.out.print("Enter numbers: ");
                    line = userInput.readLine();
                    
                    if (line == null || line.equalsIgnoreCase("EXIT")) {
                        out.println("EXIT");
                        break;
                    }
                    
                    // Send line to server
                    out.println(line);
                    
                    // Read response from server
                    String response = in.readLine();
                    System.out.println("Server response: " + response + "\n");
                }
                
                System.out.println("Disconnected from server");
                
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + SERVER_ADDRESS);
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            }
        }
        
        /**
         * Sends predefined test sequences to the server (for automated testing)
         */
        public void runTest() {
            try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
            ) {
                System.out.println("Connected to server for testing\n");
                
                // Test cases
                String[] testCases = {
                    "5 3 8 1 9 2",
                    "100 -50 25 -75 0",
                    "42",
                    "-10 -20 -30 -5",
                    "1 2 3 4 5 6 7 8 9 10"
                };
                
                for (String testCase : testCases) {
                    System.out.println("Sending: " + testCase);
                    out.println(testCase);
                    
                    String response = in.readLine();
                    System.out.println("Response: " + response + "\n");
                    
                    // Small delay between requests
                    Thread.sleep(100);
                }
                
                // Close connection
                out.println("EXIT");
                System.out.println("Test completed");
                
            } catch (UnknownHostException e) {
                System.err.println("Unknown host: " + SERVER_ADDRESS);
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Test interrupted");
            }
        }
    }
    
    /**
     * Main method - demonstrates the client-server application
     * Usage:
     *   java B10_01 server    - starts the server
     *   java B10_01 client    - starts interactive client
     *   java B10_01 test      - runs automated test
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  java B10_01 server    - Start the server");
            System.out.println("  java B10_01 client    - Start interactive client");
            System.out.println("  java B10_01 test      - Run automated test");
            return;
        }
        
        String mode = args[0].toLowerCase();
        
        switch (mode) {
            case "server":
                MinMaxServer server = new MinMaxServer();
                server.start();
                break;
                
            case "client":
                MinMaxClient client = new MinMaxClient();
                client.start();
                break;
                
            case "test":
                // First, start server in a separate thread
                Thread serverThread = new Thread(() -> {
                    MinMaxServer testServer = new MinMaxServer();
                    testServer.start();
                });
                serverThread.setDaemon(true);
                serverThread.start();
                
                // Wait for server to start
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // Run test client
                MinMaxClient testClient = new MinMaxClient();
                testClient.runTest();
                break;
                
            default:
                System.out.println("Invalid mode: " + mode);
                System.out.println("Use 'server', 'client', or 'test'");
        }
    }
}

