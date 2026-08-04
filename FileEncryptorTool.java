
import java.io.*;
import java.util.Scanner;

/**
 * File Encryption/Decryption Tool - Console based Java application
 * Encrypts and decrypts text files using a password-based XOR cipher.
 *
 * Note: XOR cipher here is for educational purposes (demonstrates the
 * encrypt/decrypt symmetry using a repeating key). It is NOT suitable
 * for securing sensitive real-world data.
 */
public class FileEncryptorTool {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===== File Encryption / Decryption Tool =====");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    processFile(true);
                    break;
                case 2:
                    processFile(false);
                    break;
                case 3:
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
        System.out.println("1. Encrypt a file");
        System.out.println("2. Decrypt a file");
        System.out.println("3. Exit");
        System.out.println("--------------------------------");
    }

    static void processFile(boolean encrypt) {
        System.out.print("Enter path of input file: ");
        String inputPath = sc.nextLine().trim();

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.out.println("Input file not found: " + inputPath);
            return;
        }

        System.out.print("Enter path for output file: ");
        String outputPath = sc.nextLine().trim();

        System.out.print("Enter password/key: ");
        String password = sc.nextLine();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        try {
            byte[] data = readAllBytes(inputFile);
            byte[] result = xorTransform(data, password);
            writeAllBytes(new File(outputPath), result);

            System.out.println((encrypt ? "Encryption" : "Decryption") + " complete.");
            System.out.println("Output written to: " + outputPath);
        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
        }
    }

    // XOR each byte of the data with a repeating key derived from the password.
    // Applying this same operation twice with the same password restores the original data.
    static byte[] xorTransform(byte[] data, String password) {
        byte[] keyBytes = password.getBytes();
        byte[] result = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ keyBytes[i % keyBytes.length]);
        }

        return result;
    }

    static byte[] readAllBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    static void writeAllBytes(File file, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        }
    }

    static int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine(); // consume leftover newline
        return val;
    }
}
