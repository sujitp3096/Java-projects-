import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Spell Checker - Console based Java application
 * Checks words against a dictionary and suggests corrections for
 * misspelled words using Levenshtein (edit) distance.
 *
 * If no dictionary file is supplied, a small built-in word list is used.
 * For real use, supply a larger dictionary file (one word per line).
 */
public class SpellChecker {

    static Set<String> dictionary = new HashSet<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===== Spell Checker =====");
        loadDictionary();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    checkText();
                    break;
                case 2:
                    loadCustomDictionary();
                    break;
                case 3:
                    System.out.println("Dictionary size: " + dictionary.size() + " words");
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
        System.out.println("1. Check text for spelling");
        System.out.println("2. Load a custom dictionary file");
        System.out.println("3. Show dictionary size");
        System.out.println("4. Exit");
        System.out.println("--------------------------------");
    }

    static void loadDictionary() {
        // Small built-in dictionary as a fallback so the program works out of the box.
        String[] builtIn = {
                "the", "quick", "brown", "fox", "jumps", "over", "lazy", "dog", "hello", "world",
                "java", "programming", "computer", "science", "algorithm", "student", "teacher",
                "school", "college", "spelling", "checker", "dictionary", "example", "function",
                "variable", "string", "integer", "boolean", "class", "object", "method", "array",
                "list", "map", "set", "loop", "condition", "code", "project", "github", "profile"
        };
        dictionary.addAll(Arrays.asList(builtIn));
        System.out.println("Loaded built-in dictionary (" + dictionary.size() + " words).");
        System.out.println("Tip: use option 2 to load a larger custom dictionary file for better results.");
    }

    static void loadCustomDictionary() {
        System.out.print("Enter path to dictionary file (one word per line): ");
        String path = sc.nextLine().trim();

        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File not found: " + path);
            return;
        }

        try {
            List<String> words = Files.readAllLines(file.toPath());
            int added = 0;
            for (String word : words) {
                String cleaned = word.trim().toLowerCase();
                if (!cleaned.isEmpty()) {
                    dictionary.add(cleaned);
                    added++;
                }
            }
            System.out.println("Loaded " + added + " words. Dictionary now has " + dictionary.size() + " words.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    static void checkText() {
        System.out.print("Enter text to check: ");
        String text = sc.nextLine();

        String[] words = text.split("[^a-zA-Z']+");
        boolean anyErrors = false;

        for (String rawWord : words) {
            if (rawWord.isEmpty()) continue;
            String word = rawWord.toLowerCase();

            if (dictionary.contains(word)) {
                continue;
            }

            anyErrors = true;
            List<String> suggestions = getSuggestions(word, 3);

            System.out.println("\n\"" + rawWord + "\" - not found in dictionary.");
            if (suggestions.isEmpty()) {
                System.out.println("  No close suggestions found.");
            } else {
                System.out.println("  Did you mean: " + String.join(", ", suggestions) + "?");
            }
        }

        if (!anyErrors) {
            System.out.println("\nNo spelling issues found!");
        }
    }

    // Returns up to `limit` dictionary words with the smallest edit distance to the given word.
    static List<String> getSuggestions(String word, int limit) {
        // Only consider dictionary words of similar length for efficiency, then rank by edit distance.
        return dictionary.stream()
                .map(dictWord -> new AbstractMap.SimpleEntry<>(dictWord, levenshteinDistance(word, dictWord)))
                .filter(entry -> entry.getValue() <= 3) // ignore wildly different words
                .sorted(Comparator.comparingInt(AbstractMap.SimpleEntry::getValue))
                .limit(limit)
                .map(AbstractMap.SimpleEntry::getKey)
                .collect(Collectors.toList());
    }

    // Classic dynamic programming edit distance (insertions, deletions, substitutions)
    static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[a.length()][b.length()];
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
