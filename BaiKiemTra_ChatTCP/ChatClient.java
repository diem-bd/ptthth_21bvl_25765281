package BaiKiemTra_ChatTCP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Chương trình Client kết nối tới Server:
 * - Luồng chính (Main Thread): Đọc dữ liệu người dùng gõ từ bàn phím và gửi lên Server.
 * - Luồng phụ (ServerListener): Tách riêng để lắng nghe tin nhắn Server trả về.
 */
public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 6789);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

            // Khởi động luồng phụ chạy ngầm để lắng nghe tin nhắn từ Server
            Thread listenerThread = new Thread(new ServerListener(socket, in));
            listenerThread.setDaemon(true);
            listenerThread.start();

            // Luồng chính: đọc bàn phím và gửi lên Server
            String line;
            while ((line = console.readLine()) != null) {
                out.println(line);
                if (line.trim().equalsIgnoreCase("/exit")) {
                    Thread.sleep(300);
                    break;
                }
            }
            socket.close();
        } catch (Exception e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
    }
}
