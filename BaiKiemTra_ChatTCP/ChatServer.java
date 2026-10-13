package BaiKiemTra_ChatTCP;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Server chính: Mở cổng TCP 6789, tạo danh sách phòng, lắng nghe các Client kết nối.
 */
public class ChatServer {
    public static final int PORT = 6789;
    private Map<String, ChatRoom> rooms = new HashMap<>();

    public ChatServer() {
        // Tạo 3 phòng theo yêu cầu đề bài
        ChatRoom study = new ChatRoom("Study");
        ChatRoom game = new ChatRoom("Game");
        ChatRoom traiNghiem = new ChatRoom("TraiNghiem");

        // Cho phép chọn bằng số (1, 2, 3) hoặc bằng tên chữ
        rooms.put("1", study);
        rooms.put("study", study);

        rooms.put("2", game);
        rooms.put("game", game);

        rooms.put("3", traiNghiem);
        rooms.put("trainghiem", traiNghiem);
    }

    public ChatRoom getRoom(String key) {
        return rooms.get(key.toLowerCase());
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        System.out.println("=== CHAT SERVER ĐÃ CHẠY TRÊN CỔNG " + PORT + " ===");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Đợi Client kết nối tới
                Socket clientSocket = serverSocket.accept();

                // Tạo đối tượng xử lý và cấp luồng riêng cho Client này
                ClientHandler handler = new ClientHandler(clientSocket, server);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
