import java.io.*;
import java.net.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

/**
 * B11_05 - Program that reads weather forecast from ua.sinoptik.ua website
 * for a specified city (entered in Ukrainian) and displays the current date
 * and min/max temperature forecast for the next 7 days.
 * 
 * Usage: java B11_05 <city_name_in_ukrainian>
 * Example: java B11_05 київ
 */
public class B11_05 {
    
    private static final String BASE_URL = "https://ua.sinoptik.ua/погода-";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    
    /**
     * Represents a daily weather forecast
     */
    static class DayForecast {
        String date;
        String minTemp;
        String maxTemp;
        
        public DayForecast(String date, String minTemp, String maxTemp) {
            this.date = date;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
        }
        
        @Override
        public String toString() {
            return String.format("Date: %s | Min: %s°C | Max: %s°C", date, minTemp, maxTemp);
        }
    }
    
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
     * Fetches HTML content from the specified URL
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
        
        // Set headers
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", "text/html");
        connection.setRequestProperty("Accept-Language", "uk-UA,uk;q=0.9");
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        
        int responseCode = connection.getResponseCode();
        
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
     * Extracts weather forecast for 7 days from HTML content
     * @param html HTML content from sinoptik.ua
     * @return list of daily forecasts
     */
    private static List<DayForecast> extractWeatherForecast(String html) {
        List<DayForecast> forecasts = new ArrayList<>();
        
        // Try to find the forecast data in the HTML
        // Sinoptik.ua typically has forecast in divs with specific classes
        
        // Method 1: Look for temperature data in specific div structure
        // Pattern for finding temperatures in format like: <div class="min">−5°</div> and <div class="max">2°</div>
        Pattern tempPattern = Pattern.compile(
            "<div\\s+class=[\"']min[\"'].*?>([−\\-]?\\d+)°.*?</div>.*?" +
            "<div\\s+class=[\"']max[\"'].*?>([−\\-]?\\d+)°.*?</div>",
            Pattern.DOTALL
        );
        
        // Method 2: Look for data-meteor attribute or similar
        Pattern dayPattern = Pattern.compile(
            "class=[\"'](?:main|day|weatherIco)[\"'][^>]*>.*?" +
            "(?:<div[^>]*class=[\"']min[\"'][^>]*>([−\\-]?\\d+)°.*?" +
            "<div[^>]*class=[\"']max[\"'][^>]*>([−\\-]?\\d+)°|" +
            "температур[ауі].*?([−\\-]?\\d+).*?([−\\-]?\\d+))",
            Pattern.DOTALL | Pattern.CASE_INSENSITIVE
        );
        
        // Method 3: More flexible pattern for temperature extraction
        Pattern flexPattern = Pattern.compile(
            "<div[^>]*class=[\"'][^\"']*(?:temperature|temp|min)[^\"']*[\"'][^>]*>\\s*" +
            "(?:<[^>]+>\\s*)*([−\\-]?\\d+)\\s*°?.*?" +
            "<div[^>]*class=[\"'][^\"']*(?:temperature|temp|max)[^\"']*[\"'][^>]*>\\s*" +
            "(?:<[^>]+>\\s*)*([−\\-]?\\d+)\\s*°?",
            Pattern.DOTALL
        );
        
        // Try to extract using various patterns
        Matcher matcher = tempPattern.matcher(html);
        int count = 0;
        
        while (matcher.find() && count < 7) {
            String minTemp = matcher.group(1).replace("−", "-");
            String maxTemp = matcher.group(2).replace("−", "-");
            
            LocalDate date = LocalDate.now().plusDays(count);
            String dateStr = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            
            forecasts.add(new DayForecast(dateStr, minTemp, maxTemp));
            count++;
        }
        
        // If first method didn't work, try alternative extraction
        if (forecasts.isEmpty()) {
            // Look for any pattern with temperature numbers
            Pattern altPattern = Pattern.compile("([−\\-]?\\d+)°");
            Matcher altMatcher = altPattern.matcher(html);
            
            List<String> temps = new ArrayList<>();
            while (altMatcher.find() && temps.size() < 14) {
                String temp = altMatcher.group(1).replace("−", "-");
                temps.add(temp);
            }
            
            // Pair temperatures as min/max
            for (int i = 0; i < Math.min(temps.size() / 2, 7); i++) {
                LocalDate date = LocalDate.now().plusDays(i);
                String dateStr = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                
                String minTemp = temps.get(i * 2);
                String maxTemp = temps.get(i * 2 + 1);
                
                // Ensure min is actually less than max
                try {
                    int min = Integer.parseInt(minTemp);
                    int max = Integer.parseInt(maxTemp);
                    if (min > max) {
                        String temp = minTemp;
                        minTemp = maxTemp;
                        maxTemp = temp;
                    }
                } catch (NumberFormatException e) {
                    // Keep as is
                }
                
                forecasts.add(new DayForecast(dateStr, minTemp, maxTemp));
            }
        }
        
        return forecasts;
    }
    
    /**
     * Alternative method: Extract forecast from JSON data embedded in HTML
     * Many weather sites embed data in JavaScript variables
     */
    private static List<DayForecast> extractFromJson(String html) {
        List<DayForecast> forecasts = new ArrayList<>();
        
        // Look for JSON data patterns
        Pattern jsonPattern = Pattern.compile(
            "\"temperature\"\\s*:\\s*\\{\\s*\"min\"\\s*:\\s*([−\\-]?\\d+)\\s*,\\s*\"max\"\\s*:\\s*([−\\-]?\\d+)"
        );
        
        Matcher matcher = jsonPattern.matcher(html);
        int count = 0;
        
        while (matcher.find() && count < 7) {
            String minTemp = matcher.group(1).replace("−", "-");
            String maxTemp = matcher.group(2).replace("−", "-");
            
            LocalDate date = LocalDate.now().plusDays(count);
            String dateStr = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            
            forecasts.add(new DayForecast(dateStr, minTemp, maxTemp));
            count++;
        }
        
        return forecasts;
    }
    
    /**
     * Displays the weather forecast
     */
    private static void displayForecast(String city, List<DayForecast> forecasts) {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║   WEATHER FORECAST FOR " + city.toUpperCase() + "   ");
        System.out.println("╚═══════════════════════════════════════════════╝\n");
        
        System.out.println("Current date: " + LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        System.out.println("\n7-Day Forecast:\n");
        
        if (forecasts.isEmpty()) {
            System.out.println("No forecast data available.");
            return;
        }
        
        System.out.println("┌────────────┬──────────┬──────────┐");
        System.out.println("│    Date    │ Min Temp │ Max Temp │");
        System.out.println("├────────────┼──────────┼──────────┤");
        
        for (DayForecast forecast : forecasts) {
            System.out.printf("│ %s │   %3s°C  │   %3s°C  │%n",
                forecast.date, forecast.minTemp, forecast.maxTemp);
        }
        
        System.out.println("└────────────┴──────────┴──────────┘");
        
        // Display in simple format as well
        System.out.println("\nDetailed format:");
        for (int i = 0; i < forecasts.size(); i++) {
            System.out.println((i + 1) + ". " + forecasts.get(i));
        }
    }
    
    /**
     * Main method
     */
    public static void main(String[] args) {
        System.out.println("B11_05 - Weather Forecast Reader");
        System.out.println("=================================\n");
        
        // Check if city name is provided
        if (args.length == 0) {
            System.err.println("ERROR: City name is required!");
            System.err.println("\nUsage: java B11_05 <city_name_in_ukrainian>");
            System.err.println("Example: java B11_05 київ");
            System.err.println("         java B11_05 львів");
            System.err.println("         java B11_05 одеса");
            return;
        }
        
        String city = args[0].toLowerCase();
        String url = BASE_URL + city + "/";
        
        try {
            System.out.println("Fetching weather forecast from: " + url);
            System.out.println("Please wait...\n");
            
            // Fetch HTML content
            String htmlContent = fetchHtmlContent(url);
            
            // Extract weather forecast
            List<DayForecast> forecasts = extractWeatherForecast(htmlContent);
            
            // If first method didn't work, try JSON extraction
            if (forecasts.isEmpty()) {
                forecasts = extractFromJson(htmlContent);
            }
            
            // If still no data, try to save HTML for debugging
            if (forecasts.isEmpty()) {
                System.err.println("WARNING: Could not extract forecast data.");
                System.err.println("The website structure may have changed.");
                System.err.println("\nSaving HTML content to debug.html for inspection...");
                
                try (PrintWriter writer = new PrintWriter("debug.html", "UTF-8")) {
                    writer.println(htmlContent);
                    System.err.println("HTML saved. Please check the website manually.");
                } catch (IOException e) {
                    System.err.println("Could not save debug file: " + e.getMessage());
                }
                
                return;
            }
            
            // Display the forecast
            displayForecast(city, forecasts);
            
        } catch (MalformedURLException e) {
            System.err.println("ERROR: Invalid URL format");
            System.err.println("City name: " + city);
        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
            System.err.println("\nPossible causes:");
            System.err.println("- No internet connection");
            System.err.println("- Website is not accessible");
            System.err.println("- Invalid city name");
            System.err.println("\nPlease check the city name and try again.");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

