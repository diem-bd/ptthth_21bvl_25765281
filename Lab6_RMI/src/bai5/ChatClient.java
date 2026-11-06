package bai5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        ChatService chat = (ChatService) registry.lookup("Chat");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));

        System.out.println("=== Chat RMI 1-1 (nhập q để thoát) ===");
        while (true) {
            System.out.print("> ");
            String line = in.readLine();
            if (line == null || line.trim().equalsIgnoreCase("q")) break;
            if (line.isBlank()) continue;
            try {
                chat.sendMessage(line);                     // đề: void sendMessage(String)
                System.out.println("  " + chat.getLastReply()); // hiển thị phản hồi của server
            } catch (RemoteException e) {
                System.out.println("  Lỗi: " + e.getMessage());
            }
        }
        System.out.println("Kết thúc phiên chat.");
    }
}
