package Weather;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Scanner;

public class WeatherApp {

    private static final String API_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Get Temperature\n2. Get Wind Speed\n3. Get Pressure\n0. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("Terminating the program.");
                break;
            } else if (choice >= 1 && choice <= 3) {
                System.out.print("Enter date and time (YYYY-MM-DD HH:MM:SS): ");
                scanner.nextLine(); // Consume the newline character
                String targetDateTime = scanner.nextLine();

                try {
                    JSONObject weatherData = fetchWeatherData(API_URL);
                    switch (choice) {
                        case 1:
                            double temperature = getTemperature(weatherData, targetDateTime);
                            System.out.println("Temperature: " + temperature + " K");
                            break;
                        case 2:
                            double windSpeed = getWindSpeed(weatherData, targetDateTime);
                            System.out.println("Wind Speed: " + windSpeed + " m/s");
                            break;
                        case 3:
                            double pressure = getPressure(weatherData, targetDateTime);
                            System.out.println("Pressure: " + pressure + " hPa");
                            break;
                    }
                } catch (IOException e) {
                    System.out.println("Error fetching weather data: " + e.getMessage());
                }
            } else {
                System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        scanner.close();
    }

    private static JSONObject fetchWeatherData(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        return new JSONObject(response.toString());
    }

    private static double getTemperature(JSONObject data, String targetDateTime) {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject entry = list.getJSONObject(i);
            if (entry.getString("dt_txt").equals(targetDateTime)) {
                JSONObject main = entry.getJSONObject("main");
                return main.getDouble("temp");
            }
        }
        return -1; // Data not found
    }

    private static double getWindSpeed(JSONObject data, String targetDateTime) {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject entry = list.getJSONObject(i);
            if (entry.getString("dt_txt").equals(targetDateTime)) {
                JSONObject wind = entry.getJSONObject("wind");
                return wind.getDouble("speed");
            }
        }
        return -1; // Data not found
    }

    private static double getPressure(JSONObject data, String targetDateTime) {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject entry = list.getJSONObject(i);
            if (entry.getString("dt_txt").equals(targetDateTime)) {
                JSONObject main = entry.getJSONObject("main");
                return main.getDouble("pressure");
            }
        }
        return -1; // Data not found
    }
}

