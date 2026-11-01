package bai5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient {
    /** Lớp callback nằm ở client, được xuất ra để server gọi ngược vào. */
    static class Listener extends UnicastRemoteObject implements ChatCallback {
        private final String nickname;

        Listener(String nickname) throws RemoteException {
            super(0);
            this.nickname = nickname;
        }

        @Override
        public void receiveMessage(String message) {
            System.out.println("  [" + nickname + " nhận] " + message);
        }
    }

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1106);
        ChatService chat = (ChatService) registry.lookup("Chat");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.print("Nhập biệt danh: ");
        String nickname = in.readLine().trim();
        chat.register(nickname, new Listener(nickname));
        System.out.println("[" + nickname + "] đã online. Nhập q để thoát.");
        while (true) {
            System.out.print("> ");
            String line = in.readLine();
            if (line == null || line.trim().equalsIgnoreCase("q")) break;
            if (line.isBlank()) continue;
            System.out.println("  " + chat.sendMessage(nickname, line));
        }
        // Thoát hẳn: RMI giữ thread không phải daemon nên phải đóng tiến trình.
        System.exit(0);
    }
}
