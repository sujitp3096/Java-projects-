import java.io.*;
import java.util.*;

/**
 * Huffman Coding Compressor - Console based Java application
 * Compresses and decompresses text files using Huffman coding.
 *
 * How it works:
 *  1. Build a frequency table of characters in the input file.
 *  2. Build a Huffman tree (min-heap based) from frequencies.
 *  3. Generate binary codes for each character (shorter codes for frequent chars).
 *  4. Write compressed file: header (character->code table) + packed bits.
 *  5. Decompression reads the header, rebuilds codes, and decodes the bits.
 */
public class HuffmanCompressor {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===== Huffman Coding Compressor =====");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    compressFile();
                    break;
                case 2:
                    decompressFile();
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
        System.out.println("1. Compress a text file");
        System.out.println("2. Decompress a .huff file");
        System.out.println("3. Exit");
        System.out.println("--------------------------------");
    }

    // ---------- Huffman Tree Node ----------
    static class Node implements Comparable<Node> {
        char ch;
        int freq;
        Node left, right;

        Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }

        Node(int freq, Node left, Node right) {
            this.ch = '\0';
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(Node other) {
            return this.freq - other.freq;
        }
    }

    // ---------- Compression ----------

    static void compressFile() {
        System.out.print("Enter path of text file to compress: ");
        String inputPath = sc.nextLine().trim();
        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            System.out.println("File not found: " + inputPath);
            return;
        }

        try {
            String content = readFileAsString(inputFile);
            if (content.isEmpty()) {
                System.out.println("File is empty, nothing to compress.");
                return;
            }

            Map<Character, Integer> freqMap = buildFrequencyMap(content);
            Node root = buildHuffmanTree(freqMap);
            Map<Character, String> codes = new HashMap<>();
            buildCodeTable(root, "", codes);

            String outputPath = inputPath + ".huff";
            writeCompressedFile(outputPath, content, freqMap, codes);

            long originalSize = inputFile.length();
            long compressedSize = new File(outputPath).length();
            double savings = 100.0 * (1 - ((double) compressedSize / originalSize));

            System.out.println("Compression complete: " + outputPath);
            System.out.printf("Original size: %d bytes | Compressed size: %d bytes | Space saved: %.1f%%%n",
                    originalSize, compressedSize, savings);

        } catch (IOException e) {
            System.out.println("Error compressing file: " + e.getMessage());
        }
    }

    static Map<Character, Integer> buildFrequencyMap(String content) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : content.toCharArray()) {
            freqMap.merge(c, 1, Integer::sum);
        }
        return freqMap;
    }

    static Node buildHuffmanTree(Map<Character, Integer> freqMap) {
        PriorityQueue<Node> heap = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            heap.add(new Node(entry.getKey(), entry.getValue()));
        }

        // Edge case: file has only one distinct character
        if (heap.size() == 1) {
            Node only = heap.poll();
            return new Node(only.freq, only, null);
        }

        while (heap.size() > 1) {
            Node left = heap.poll();
            Node right = heap.poll();
            heap.add(new Node(left.freq + right.freq, left, right));
        }

        return heap.poll();
    }

    static void buildCodeTable(Node node, String code, Map<Character, String> codes) {
        if (node == null) return;

        if (node.isLeaf()) {
            codes.put(node.ch, code.isEmpty() ? "0" : code);
            return;
        }

        buildCodeTable(node.left, code + "0", codes);
        buildCodeTable(node.right, code + "1", codes);
    }

    static void writeCompressedFile(String outputPath, String content,
                                     Map<Character, Integer> freqMap,
                                     Map<Character, String> codes) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputPath)))) {

            // Write header: number of distinct characters, then each (char, frequency) pair
            out.writeInt(freqMap.size());
            for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
                out.writeChar(entry.getKey());
                out.writeInt(entry.getValue());
            }

            // Build the full bit string for the content
            StringBuilder bitString = new StringBuilder();
            for (char c : content.toCharArray()) {
                bitString.append(codes.get(c));
            }

            // Write total bit count so decoder knows where to stop (avoids padding ambiguity)
            out.writeInt(bitString.length());

            // Pack bits into bytes
            int i = 0;
            while (i < bitString.length()) {
                int b = 0;
                for (int bit = 0; bit < 8; bit++) {
                    b <<= 1;
                    if (i < bitString.length() && bitString.charAt(i) == '1') {
                        b |= 1;
                    }
                    i++;
                }
                out.writeByte(b);
            }
        }
    }

    // ---------- Decompression ----------

    static void decompressFile() {
        System.out.print("Enter path of .huff file to decompress: ");
        String inputPath = sc.nextLine().trim();
        File inputFile = new File(inputPath);

        if (!inputFile.exists()) {
            System.out.println("File not found: " + inputPath);
            return;
        }

        System.out.print("Enter path for decompressed output file: ");
        String outputPath = sc.nextLine().trim();

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(inputFile)))) {

            int distinctChars = in.readInt();
            Map<Character, Integer> freqMap = new HashMap<>();
            for (int i = 0; i < distinctChars; i++) {
                char ch = in.readChar();
                int freq = in.readInt();
                freqMap.put(ch, freq);
            }

            Node root = buildHuffmanTree(freqMap);
            int totalBits = in.readInt();

            StringBuilder bitString = new StringBuilder();
            int bytesToRead = (totalBits + 7) / 8;
            for (int i = 0; i < bytesToRead; i++) {
                int b = in.readUnsignedByte();
                for (int bit = 7; bit >= 0; bit--) {
                    bitString.append((b >> bit) & 1);
                }
            }
            bitString.setLength(totalBits); // trim any padding bits from the last byte

            StringBuilder decoded = new StringBuilder();
            Node current = root;
            for (int i = 0; i < bitString.length(); i++) {
                current = (bitString.charAt(i) == '0') ? current.left : current.right;
                if (current == null) {
                    // Handles single-character-file edge case where left points to the only leaf
                    current = root;
                }
                if (current != null && current.isLeaf()) {
                    decoded.append(current.ch);
                    current = root;
                }
            }

            try (Writer writer = new BufferedWriter(new FileWriter(outputPath))) {
                writer.write(decoded.toString());
            }

            System.out.println("Decompression complete: " + outputPath);

        } catch (IOException e) {
            System.out.println("Error decompressing file: " + e.getMessage());
        }
    }

    static String readFileAsString(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int ch;
            while ((ch = br.read()) != -1) {
                sb.append((char) ch);
            }
        }
        return sb.toString();
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
