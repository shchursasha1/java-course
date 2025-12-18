import java.io.*;
import java.net.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

/**
 * B11_03 - Program that reads accurate time from godinnik.com website
 * and compares it with local computer time.
 * 
 * The time is extracted from the <noscript> tag within the page HTML.
 */
public class B11_03 {
    
    private static final String TIME_URL = "https://godinnik.com/time/київ/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    
    /**
     * Encodes URL properly to handle Cyrillic characters
     * @param urlString the URL to encode
     * @return properly encoded URL
     * @throws MalformedURLException if URL is invalid
     * @throws URISyntaxException if URI syntax is invalid
     */
    private static URL encodeUrl(String urlString) throws MalformedURLException, URISyntaxException {
        URI uri = new URI(urlString);
        // Convert URI to ASCII string (properly encoded)
        String asciiString = uri.toASCIIString();
        return new URL(asciiString);
    }
    
    /**
     * Fetches the HTML content from the specified URL
     * @param urlString the URL to fetch
     * @return HTML content as string
     * @throws IOException if network error occurs
     */
    private static String fetchHtmlContent(String urlString) throws IOException {
        URL url;
        try {
            url = encodeUrl(urlString);
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URL format: " + e.getMessage());
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Set user-agent header to get status 200 response
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        
        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " + responseCode);
        
        if (responseCode != 200) {
            throw new IOException("Failed to fetch page. Response code: " + responseCode);
        }
        
        // Read the response
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        return content.toString();
    }
    
    /**
     * Extracts time from HTML content
     * Looks for time in <noscript> tag within <span id="lbl-time">
     * @param html HTML content
     * @return time string in format HH:mm:ss or null if not found
     */
    private static String extractTimeFromHtml(String html) {
        // Pattern to find time in <noscript> tag
        // Looking for: <noscript>HH:mm:ss</noscript>
        Pattern pattern = Pattern.compile("<noscript>\\s*(\\d{1,2}:\\d{2}:\\d{2})\\s*</noscript>");
        Matcher matcher = pattern.matcher(html);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return null;
    }
    
    /**
     * Parses time string in format HH:mm:ss to LocalTime
     * @param timeString time in format HH:mm:ss
     * @return LocalTime object
     */
    private static LocalTime parseTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(timeString, formatter);
    }
    
    /**
     * Compares website time with local computer time
     * @param websiteTime time from website
     * @param localTime local computer time
     */
    private static void compareTime(LocalTime websiteTime, LocalTime localTime) {
        System.out.println("\n=== TIME COMPARISON ===");
        System.out.println("Website time (godinnik.com): " + websiteTime);
        System.out.println("Local computer time:         " + localTime);
        
        // Calculate difference in seconds
        long differenceSeconds = Math.abs(localTime.toSecondOfDay() - websiteTime.toSecondOfDay());
        
        // Handle case when times are on different sides of midnight
        if (differenceSeconds > 12 * 3600) {
            differenceSeconds = 24 * 3600 - differenceSeconds;
        }
        
        System.out.println("Time difference:             " + differenceSeconds + " seconds");
        
        // Determine if times match (within reasonable tolerance)
        int toleranceSeconds = 5; // 5 seconds tolerance
        
        if (differenceSeconds <= toleranceSeconds) {
            System.out.println("\n✓ Times MATCH (within " + toleranceSeconds + " seconds tolerance)");
            System.out.println("  Your local time is accurate.");
        } else {
            System.out.println("\n✗ Times DO NOT MATCH");
            if (localTime.isAfter(websiteTime)) {
                System.out.println("  Your local time is " + differenceSeconds + " seconds AHEAD.");
            } else {
                System.out.println("  Your local time is " + differenceSeconds + " seconds BEHIND.");
            }
        }
    }
    
    /**
     * Main method - fetches time from website and compares with local time
     */
    public static void main(String[] args) {
        System.out.println("B11_03 - Accurate Time Checker");
        System.out.println("===============================\n");
        
        try {
            // Get local time
            LocalTime localTime = LocalTime.now();
            
            // Fetch HTML from website
            System.out.println("Fetching time from: " + TIME_URL);
            String htmlContent = fetchHtmlContent(TIME_URL);
            
            // Extract time from HTML
            String timeString = extractTimeFromHtml(htmlContent);
            
            if (timeString == null) {
                System.err.println("ERROR: Could not find time in <noscript> tag");
                System.err.println("The website structure may have changed.");
                return;
            }
            
            System.out.println("Time found on website: " + timeString);
            
            // Parse website time
            LocalTime websiteTime = parseTime(timeString);
            
            // Compare times
            compareTime(websiteTime, localTime);
            
            // Show full local date-time for reference
            System.out.println("\n=== ADDITIONAL INFO ===");
            System.out.println("Current date-time: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            System.out.println("Time zone: " + ZoneId.systemDefault());
            
        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
            System.err.println("\nPossible causes:");
            System.err.println("- No internet connection");
            System.err.println("- Website is not accessible");
            System.err.println("- Website structure changed");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

