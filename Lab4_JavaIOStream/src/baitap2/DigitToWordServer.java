
package baitap2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitToWordServer {
    private static final int PORT = 5002;
    private static final String[] DIGIT_WORDS = {
        "Không", "Một", "Hai", "Ba", "Bốn",
        "Năm", "Sáu", "Bảy", "Tám", "Chín"
    };

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : PORT;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("TCP Digit-to-Word Server đang lắng nghe trên cổng " + port);
            while (true) {
                try (Socket socket = server.accept()) {
                    System.out.println("Client đã kết nối: " + socket.getRemoteSocketAddress());
                    handleClient(socket);
                } catch (IOException e) {
                    System.err.println("Lỗi phiên làm việc với client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Không thể khởi động server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                // Kiểm tra lệnh QUIT
                if (request.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                String response = processDigit(request);
                out.println(response);
            }
        }
    }

    public static String processDigit(String input) {
        // Yêu cầu: dữ liệu phải là ĐÚNG 1 chữ số, không chứa khoảng trắng hay ký tự khác
        if (input != null && input.length() == 1) {
            char c = input.charAt(0);
            if (c >= '0' && c <= '9') {
                return DIGIT_WORDS[c - '0'];
            }
        }
        return "ERR INVALID_DIGIT";
    }
}
