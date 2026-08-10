import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Mini Search Engine - Console based Java application
 * Indexes all .txt files in a given folder and lets you search for keywords.
 * Uses an inverted index (word -> list of files containing it, with term frequency)
 * and ranks results by a simple TF score (how often the word appears in each file).
 */
public class MiniSearchEngine {

    // word -> (filename -> count of occurrences)
    static Map<String, Map<String, Integer>> invertedIndex = new HashMap<>();
    static Set<String> indexedFiles = new LinkedHashSet<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===== Mini Search Engine =====");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    indexFolder();
                    break;
                case 2:
                    search();
                    break;
                case 3:
                    viewIndexedFiles();
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
        System.out.println("1. Index a folder of .txt files");
        System.out.println("2. Search");
        System.out.println("3. View indexed files");
        System.out.println("4. Exit");
        System.out.println("--------------------------------");
    }

    static void indexFolder() {
        System.out.print("Enter folder path containing .txt files: ");
        String folderPath = sc.nextLine().trim();

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Folder not found: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.out.println("No .txt files found in that folder.");
            return;
        }

        int indexedCount = 0;
        for (File file : files) {
            try {
                indexFile(file);
                indexedCount++;
            } catch (IOException e) {
                System.out.println("Could not read " + file.getName() + ": " + e.getMessage());
            }
        }

        System.out.println("Indexed " + indexedCount + " file(s) from " + folderPath);
    }

    static void indexFile(File file) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));
        String fileName = file.getName();
        indexedFiles.add(fileName);

        // Tokenize: lowercase, split on non-alphanumeric characters
        String[] words = content.toLowerCase().split("[^a-zA-Z0-9]+");

        for (String word : words) {
            if (word.isEmpty()) continue;

            invertedIndex
                    .computeIfAbsent(word, k -> new HashMap<>())
                    .merge(fileName, 1, Integer::sum);
        }
    }

    static void search() {
        if (invertedIndex.isEmpty()) {
            System.out.println("No files indexed yet. Please index a folder first.");
            return;
        }

        System.out.print("Enter search query (one or more words): ");
        String query = sc.nextLine().trim().toLowerCase();

        if (query.isEmpty()) {
            System.out.println("Query cannot be empty.");
            return;
        }

        String[] queryWords = query.split("\\s+");

        // Aggregate scores per file: sum of term frequencies across all query words
        Map<String, Integer> scores = new HashMap<>();
        Map<String, Set<String>> matchedWordsPerFile = new HashMap<>();

        for (String word : queryWords) {
            Map<String, Integer> fileHits = invertedIndex.get(word);
            if (fileHits == null) continue;

            for (Map.Entry<String, Integer> entry : fileHits.entrySet()) {
                scores.merge(entry.getKey(), entry.getValue(), Integer::sum);
                matchedWordsPerFile.computeIfAbsent(entry.getKey(), k -> new TreeSet<>()).add(word);
            }
        }

        if (scores.isEmpty()) {
            System.out.println("No results found for: " + query);
            return;
        }

        List<Map.Entry<String, Integer>> ranked = scores.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .collect(Collectors.toList());

        System.out.println("\nSearch results for \"" + query + "\":");
        System.out.println("----------------------------------------");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : ranked) {
            System.out.println(rank + ". " + entry.getKey() +
                    " (score: " + entry.getValue() +
                    ", matched words: " + matchedWordsPerFile.get(entry.getKey()) + ")");
            rank++;
        }
    }

    static void viewIndexedFiles() {
        if (indexedFiles.isEmpty()) {
            System.out.println("No files indexed yet.");
            return;
        }

        System.out.println("\nIndexed files:");
        for (String fileName : indexedFiles) {
            System.out.println("- " + fileName);
        }
        System.out.println("\nTotal unique words indexed: " + invertedIndex.size());
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
