import java.io.*;
import java.util.*;

/**
 * Online Voting System - Console based Java application
 * Features:
 *  - Register candidates (admin)
 *  - Cast a vote using a unique Voter ID (prevents double voting)
 *  - View live results / winner
 *  - Data persists across runs using files (candidates.txt, voters.txt)
 */
public class VotingSystem {

    static final String CANDIDATES_FILE = "candidates.txt";
    static final String VOTERS_FILE = "voters.txt";

    static Map<String, Integer> candidates = new LinkedHashMap<>(); // name -> vote count
    static Set<String> votedIds = new HashSet<>();                 // voter IDs that already voted
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadCandidates();
        loadVoters();

        System.out.println("===== Welcome to the Online Voting System =====");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addCandidate();
                    break;
                case 2:
                    castVote();
                    break;
                case 3:
                    viewResults();
                    break;
                case 4:
                    viewWinner();
                    break;
                case 5:
                    System.out.println("Thank you for using the Voting System. Goodbye!");
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
        System.out.println("1. Add Candidate (Admin)");
        System.out.println("2. Cast Vote");
        System.out.println("3. View Live Results");
        System.out.println("4. View Winner");
        System.out.println("5. Exit");
        System.out.println("--------------------------------");
    }

    static void addCandidate() {
        System.out.print("Enter candidate name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Candidate name cannot be empty.");
            return;
        }
        if (candidates.containsKey(name)) {
            System.out.println("Candidate already exists.");
            return;
        }

        candidates.put(name, 0);
        saveCandidates();
        System.out.println("Candidate \"" + name + "\" added successfully.");
    }

    static void castVote() {
        if (candidates.isEmpty()) {
            System.out.println("No candidates available. Ask admin to add candidates first.");
            return;
        }

        System.out.print("Enter your unique Voter ID: ");
        String voterId = sc.nextLine().trim();

        if (voterId.isEmpty()) {
            System.out.println("Voter ID cannot be empty.");
            return;
        }
        if (votedIds.contains(voterId)) {
            System.out.println("This Voter ID has already been used to vote. Voting is one-time only.");
            return;
        }

        System.out.println("\nCandidates:");
        List<String> names = new ArrayList<>(candidates.keySet());
        for (int i = 0; i < names.size(); i++) {
            System.out.println((i + 1) + ". " + names.get(i));
        }

        int choice = readInt("Enter the number of the candidate you want to vote for: ");
        if (choice < 1 || choice > names.size()) {
            System.out.println("Invalid candidate selection. Vote not counted.");
            return;
        }

        String selected = names.get(choice - 1);
        candidates.put(selected, candidates.get(selected) + 1);
        votedIds.add(voterId);

        saveCandidates();
        saveVoters();

        System.out.println("Vote cast successfully for \"" + selected + "\". Thank you for voting!");
    }

    static void viewResults() {
        if (candidates.isEmpty()) {
            System.out.println("No candidates available.");
            return;
        }

        int totalVotes = candidates.values().stream().mapToInt(Integer::intValue).sum();

        System.out.println("\nLive Results:");
        System.out.println("----------------------------------------");
        System.out.printf("%-20s | %-6s | %s%n", "Candidate", "Votes", "Share");
        System.out.println("----------------------------------------");
        for (Map.Entry<String, Integer> entry : candidates.entrySet()) {
            double share = totalVotes == 0 ? 0 : (entry.getValue() * 100.0 / totalVotes);
            System.out.printf("%-20s | %-6d | %.1f%%%n", entry.getKey(), entry.getValue(), share);
        }
        System.out.println("----------------------------------------");
        System.out.println("Total Votes Cast: " + totalVotes);
    }

    static void viewWinner() {
        if (candidates.isEmpty()) {
            System.out.println("No candidates available.");
            return;
        }

        int totalVotes = candidates.values().stream().mapToInt(Integer::intValue).sum();
        if (totalVotes == 0) {
            System.out.println("No votes have been cast yet.");
            return;
        }

        String winner = null;
        int maxVotes = -1;
        boolean tie = false;

        for (Map.Entry<String, Integer> entry : candidates.entrySet()) {
            if (entry.getValue() > maxVotes) {
                maxVotes = entry.getValue();
                winner = entry.getKey();
                tie = false;
            } else if (entry.getValue() == maxVotes) {
                tie = true;
            }
        }

        if (tie) {
            System.out.println("It's currently a tie at " + maxVotes + " votes! No single winner yet.");
        } else {
            System.out.println("Current Leader: \"" + winner + "\" with " + maxVotes + " votes.");
        }
    }

    // ---------- File persistence ----------

    static void saveCandidates() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CANDIDATES_FILE))) {
            for (Map.Entry<String, Integer> entry : candidates.entrySet()) {
                pw.println(entry.getKey() + "|" + entry.getValue());
            }
        } catch (IOException ex) {
            System.out.println("Error saving candidates: " + ex.getMessage());
        }
    }

    static void loadCandidates() {
        File file = new File(CANDIDATES_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length == 2) {
                    candidates.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading candidates: " + ex.getMessage());
        }
    }

    static void saveVoters() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(VOTERS_FILE))) {
            for (String id : votedIds) {
                pw.println(id);
            }
        } catch (IOException ex) {
            System.out.println("Error saving voters: " + ex.getMessage());
        }
    }

    static void loadVoters() {
        File file = new File(VOTERS_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) votedIds.add(line.trim());
            }
        } catch (IOException ex) {
            System.out.println("Error loading voters: " + ex.getMessage());
        }
    }

    // ---------- Input helpers ----------

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

