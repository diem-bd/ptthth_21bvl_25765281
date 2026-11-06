package baitap5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port;
        try {
            port = args.length > 1 ? Integer.parseInt(args[1]) : ChatServer.PORT;
        } catch (NumberFormatException e) {
            System.err.println("Port phải là số nguyên");
            return;
        }

        try (Socket socket = new Socket(host, port);
             BufferedReader serverIn = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter serverOut = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader consoleIn = new BufferedReader(
                     new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            System.out.println("=== Connected to Chat Server (" + host + ":" + port + ") ===");
            System.out.println("Available commands: NICK <name>, USERS, MSG <message>, QUIT");

            // Luồng nền chuyên đọc tin nhắn/broadcast từ server để không chặn việc gõ lệnh.
            Thread listenerThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = serverIn.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException ignored) {
                    // Socket bị đóng khi client thoát: không phải lỗi.
                }
            });
            listenerThread.setDaemon(true);
            listenerThread.start();

            String userInput;
            while ((userInput = consoleIn.readLine()) != null) {
                serverOut.println(userInput);
                if (userInput.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
            // Không sleep để "đoán" thời điểm server xử lý xong:
            // đóng socket ngay, luồng nền sẽ tự kết thúc khi readLine() trả về null.
        } catch (IOException e) {
            System.err.println("Chat client error: " + e.getMessage());
        }
    }
}
