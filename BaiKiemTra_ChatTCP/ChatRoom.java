package BaiKiemTra_ChatTCP;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý thông tin 1 phòng chat và danh sách client trong phòng đó.
 */
public class ChatRoom {
    private String name;
    private List<ClientHandler> clients = new ArrayList<>();

    public ChatRoom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Thêm người vào phòng
    public synchronized void addClient(ClientHandler client) {
        clients.add(client);
        broadcast("[Hệ thống] " + client.getClientName() + " đã vào phòng " + name, client);
    }

    // Rút người ra khỏi phòng
    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcast("[Hệ thống] " + client.getClientName() + " đã rời phòng " + name, client);
    }

    // Gửi tin nhắn cho những người khác trong cùng phòng
    public synchronized void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}
