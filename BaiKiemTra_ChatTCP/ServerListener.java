package BaiKiemTra_ChatTCP;

import java.io.BufferedReader;
import java.net.Socket;

/**
 * Tách riêng file xử lý luồng nhận tin nhắn chạy ngầm ở phía Client (implements Runnable).
 */
public class ServerListener implements Runnable {
    private Socket socket;
    private BufferedReader in;

    public ServerListener(Socket socket, BufferedReader in) {
        this.socket = socket;
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String msg;
            // Liên tục đọc tin nhắn từ Server gửi về và in ra màn hình
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (Exception ignored) {
        }
    }
}
