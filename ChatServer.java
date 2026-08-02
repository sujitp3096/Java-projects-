import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * ChatServer - Multi-client console chat server using Java Sockets.
 * Run this first, then run ChatClient.java from one or more terminals.
 *
 * Usage: java ChatServer [port]   (default port 5000)
 */
public class ChatServer {

    static final int DEFAULT_PORT = 5000;
    static ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat server started on port " + port);
            System.out.println("Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                pool.execute(new ClientHandler(socket));
            }
        }
    }

    static void broadcast(String message, String excludeUser) {
        for (Map.Entry<String, PrintWriter> entry : clients.entrySet()) {
            if (!entry.getKey().equals(excludeUser)) {
                entry.getValue().println(message);
            }
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private String username;
        private PrintWriter out;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Enter your username:");
                username = in.readLine();
                if (username == null || username.trim().isEmpty()) {
                    username = "Guest" + socket.getPort();
                }

                clients.put(username, out);
                System.out.println(username + " joined the chat.");
                broadcast("[Server] " + username + " has joined the chat.", username);
                out.println("[Server] Welcome, " + username + "! Type your messages below. Type 'exit' to leave.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    System.out.println(username + ": " + message);
                    broadcast(username + ": " + message, username);
                }

            } catch (IOException e) {
                System.out.println("Connection error with " + username + ": " + e.getMessage());
            } finally {
                if (username != null) {
                    clients.remove(username);
                    System.out.println(username + " left the chat.");
                    broadcast("[Server] " + username + " has left the chat.", username);
                }
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
            }

