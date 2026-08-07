import java.io.*;
import java.util.*;

/**
 * URL Shortener - Console based Java application
 * Generates short codes for long URLs and resolves them back.
 * Uses a base-62 encoding of an incrementing counter for short codes,
 * and persists mappings to a file so they survive across runs.
 */
public class URLShortener {

    static final String FILE_NAME = "urls.txt";
    static final String BASE62_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    static final String SHORT_DOMAIN = "http://short.ly/";

    static Map<String, String> codeToUrl = new LinkedHashMap<>(); // shortCode -> longUrl
    static Map<String, String> urlToCode = new LinkedHashMap<>(); // longUrl -> shortCode (avoid duplicates)
    static int counter = 1;

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();

        System.out.println("===== URL Shortener =====");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    shortenUrl();
                    break;
                case 2:
                    expandUrl();
                    break;
                case 3:
                    viewAllUrls();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    static void printMenu() {
        System.out.println("\n--------------------------------");
        System.out.println("1. Shorten a URL");
        System.out.println("2. Expand a short URL");
        System.out.println("3. View all URLs");
        System.out.println("4. Exit");
        System.out.println("--------------------------------");
    }

    static void shortenUrl() {
        System.out.print("Enter the long URL: ");
        String longUrl = sc.nextLine().trim();

        if (longUrl.isEmpty()) {
            System.out.println("URL cannot be empty.");
            return;
        }
        if (!longUrl.startsWith("http://") && !longUrl.startsWith("https://")) {
            longUrl = "http://" + longUrl;
        }

        if (urlToCode.containsKey(longUrl)) {
            String existingCode = urlToCode.get(longUrl);
            System.out.println("This URL was already shortened: " + SHORT_DOMAIN + existingCode);
            return;
        }

        String code = encodeBase62(counter);
        counter++;

        codeToUrl.put(code, longUrl);
        urlToCode.put(longUrl, code);

        saveData();

        System.out.println("Shortened URL: " + SHORT_DOMAIN + code);
    }

    static void expandUrl() {
        System.out.print("Enter the short URL or code: ");
        String input = sc.nextLine().trim();

        String code = input.startsWith(SHORT_DOMAIN) ? input.substring(SHORT_DOMAIN.length()) : input;

        String longUrl = codeToUrl.get(code);
        if (longUrl == null) {
            System.out.println("No matching URL found for that code.");
        } else {
            System.out.println("Original URL: " + longUrl);
        }
    }

    static void viewAllUrls() {
        if (codeToUrl.isEmpty()) {
            System.out.println("No URLs have been shortened yet.");
            return;
        }

        System.out.println("\nShort URL                  | Original URL");
        System.out.println("----------------------------------------------------------------");
        for (Map.Entry<String, String> entry : codeToUrl.entrySet()) {
            System.out.printf("%-27s| %s%n", SHORT_DOMAIN + entry.getKey(), entry.getValue());
        }
    }

    // Encodes a positive integer into a base-62 string (a-z, A-Z, 0-9)
    static String encodeBase62(int num) {
        StringBuilder sb = new StringBuilder();
        int base = BASE62_CHARS.length();

        if (num == 0) return String.valueOf(BASE62_CHARS.charAt(0));

        while (num > 0) {
            int remainder = num % base;
            sb.append(BASE62_CHARS.charAt(remainder));
            num /= base;
        }

        return sb.reverse().toString();
    }

    // ---------- File persistence ----------

    static void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.println("COUNTER|" + counter);
            for (Map.Entry<String, String> entry : codeToUrl.entrySet()) {
                pw.println(entry.getKey() + "|" + entry.getValue());
            }
        } catch (IOException ex) {
            System.out.println("Error saving data: " + ex.getMessage());
        }
    }

    static void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                int sepIndex = line.indexOf('|');
                if (sepIndex == -1) continue;

                String key = line.substring(0, sepIndex);
                String value = line.substring(sepIndex + 1);

                if (key.equals("COUNTER")) {
                    counter = Integer.parseInt(value);
                } else {
                    codeToUrl.put(key, value);
                    urlToCode.put(value, key);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading data: " + ex.getMessage());
        }
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }
}
