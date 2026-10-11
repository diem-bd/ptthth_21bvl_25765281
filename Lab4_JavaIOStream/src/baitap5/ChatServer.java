package baitap5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    public static final int PORT = 5015;
    private static final int MAX_CLIENTS = 50;

    // Quản lý người dùng: Nickname -> PrintWriter (thread-safe)
    private static final ConcurrentHashMap<String, PrintWriter> activeClients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTS);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("=== Multi-client TCP Chat Server running on port " + port + " ===");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Chat Server error: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void handleClient(Socket socket) {
        String remote = socket.getRemoteSocketAddress().toString();
        System.out.println("New connection from: " + remote);
        String clientNick = null;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            out.println("INFO Please register your nickname using: NICK <username>");

            String line;
            while ((line = in.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                if (trimmed.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                if (trimmed.startsWith("NICK ")) {
                    String desiredNick = trimmed.substring(5).trim();
                    if (desiredNick.isEmpty() || desiredNick.contains(" ")) {
                        out.println("ERR INVALID_NICKNAME");
                    } else if (activeClients.containsKey(desiredNick)) {
                        out.println("ERR NICK_TAKEN");
                    } else {
                        if (clientNick != null) {
                            activeClients.remove(clientNick);
                        }
                        clientNick = desiredNick;
                        activeClients.put(clientNick, out);
                        out.println("OK NICK_ACCEPTED " + clientNick);
                        broadcast("[SYSTEM] " + clientNick + " joined the chat.", clientNick);
                        System.out.println("Registered user: " + clientNick + " (" + remote + ")");
                    }
                } else if (trimmed.equalsIgnoreCase("USERS")) {
                    if (clientNick == null) {
                        out.println("ERR NOT_REGISTERED");
                    } else {
                        String userList = String.join(", ", activeClients.keySet());
                        out.println("OK USERS: [" + userList + "]");
                    }
                } else if (trimmed.startsWith("MSG ")) {
                    if (clientNick == null) {
                        out.println("ERR NOT_REGISTERED");
                    } else {
                        String content = trimmed.substring(4);
                        broadcast("[" + clientNick + "]: " + content, clientNick);
                        out.println("OK SENT");
                    }
                } else {
                    out.println("ERR UNKNOWN_COMMAND (Commands: NICK, USERS, MSG, QUIT)");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected abruptly: " + remote + " (" + e.getMessage() + ")");
        } finally {
            if (clientNick != null) {
                activeClients.remove(clientNick);
                broadcast("[SYSTEM] " + clientNick + " left the chat.", null);
                System.out.println("User removed: " + clientNick);
            }
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private static void broadcast(String message, String senderNick) {
        for (var entry : activeClients.entrySet()) {
            if (senderNick == null || !entry.getKey().equals(senderNick)) {
                entry.getValue().println(message);
            }
        }
    }
}
