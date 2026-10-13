package BaiKiemTra_ChatTCP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Tách riêng file xử lý luồng cho từng Client ở phía Server (implements Runnable).
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private ChatRoom currentRoom;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public String getClientName() {
        return clientName;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);

            // 1. Nhập tên người dùng
            out.println("Vui lòng nhập tên của bạn:");
            clientName = in.readLine();
            if (clientName == null || clientName.trim().isEmpty()) {
                return;
            }
            clientName = clientName.trim();
            out.println("Xin chào " + clientName + "!");

            // 2. Vòng lặp chọn phòng và chat
            while (true) {
                out.println("\n--- DANH SÁCH PHÒNG CHAT ---");
                out.println("1. Study");
                out.println("2. Game");
                out.println("3. TraiNghiem");
                out.println("Chọn phòng (1, 2, 3 hoặc gõ /exit để thoát):");

                String choice = in.readLine();
                if (choice == null || choice.trim().equalsIgnoreCase("/exit")) {
                    break;
                }

                currentRoom = server.getRoom(choice.trim());
                if (currentRoom == null) {
                    out.println("Phòng không hợp lệ, vui lòng chọn lại!");
                    continue;
                }

                // Vào phòng chat
                currentRoom.addClient(this);
                out.println("Bạn đã vào phòng [" + currentRoom.getName() + "]. (Gõ /quit để đổi phòng, /exit để thoát)");

                // Lắng nghe tin nhắn chat trong phòng
                String msg;
                while ((msg = in.readLine()) != null) {
                    msg = msg.trim();
                    if (msg.equalsIgnoreCase("/quit")) {
                        currentRoom.removeClient(this);
                        currentRoom = null;
                        out.println("Bạn đã rời phòng.");
                        break; // Quay lại vòng lặp chọn phòng
                    }
                    if (msg.equalsIgnoreCase("/exit")) {
                        return; // Thoát hẳn khỏi chương trình
                    }
                    // Gửi tin nhắn cho những người khác trong cùng phòng
                    currentRoom.broadcast("[" + clientName + "]: " + msg, this);
                }
            }
        } catch (Exception e) {
            // Client ngắt kết nối
        } finally {
            if (currentRoom != null) {
                currentRoom.removeClient(this);
            }
            try {
                socket.close();
            } catch (Exception ignored) {}
        }
    }
}
